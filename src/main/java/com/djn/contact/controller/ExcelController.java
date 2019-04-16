package com.djn.contact.controller;


import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.djn.contact.mapper.ExcelMapper;
import com.djn.contact.model.Admin;
import com.djn.contact.model.ContactsListModel;
import com.djn.contact.model.ContactsModel;
import com.djn.contact.service.ExcelService;
import com.djn.contact.util.Base64Utils;
import com.djn.contact.util.MD5Utils;
import com.djn.contact.util.MyJSONUtils;
import com.djn.contact.util.SmsUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.json.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExcelController {

    @Autowired
    ExcelService excelService;

    //创建通讯录
    @RequestMapping(value = "/GenerateExcel",method = RequestMethod.GET)
    public void GenerateExcel (HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {
        String password;
        String contactsName;
        String adminName;
        Timestamp ts;
        String phoneNumber;
        resp.setContentType("text/html;charset=utf-8");
        req.setCharacterEncoding("utf-8");
        Writer out = resp.getWriter();
        password = req.getParameter("password");
        contactsName = req.getParameter("contactsName");
        adminName = req.getParameter("adminName");
        phoneNumber = req.getParameter("phoneNumber");
        String openId = req.getParameter("openId");
        String md5_password = MD5Utils.encode(password);
        long time = System.currentTimeMillis();
        String path = contactsName +"+"+ time;
        //判断请求中的数据是否使空字符或者使空格组成的字符串
        if(!password.trim().isEmpty() && !contactsName.trim().isEmpty() && !adminName.trim().isEmpty()){
            ts = new Timestamp(time);
            Admin admin = new Admin();
            admin.setAdminName(adminName);
            admin.setContactsName(path);
            admin.setOpenId(openId);
            admin.setPhoneNumber(phoneNumber);
            admin.setPassword(md5_password);
            admin.setCreateTime(ts);
            admin.setUpdateTime(ts);
            String filePath = "/Users/jason/Desktop/testexcel/"+path+".xls";
            String invitationCode = excelService.addExcel(admin);
            WritableWorkbook WritableWorkbook = Workbook.createWorkbook(new File(filePath));
            WritableSheet sheet = WritableWorkbook.createSheet("第一页", 0);
            sheet.setColumnView( 1 , 12);
            Label label1 = new Label(0,0,"姓名");
            Label label2 = new Label(1,0,"电话号码");
            Label label3 = new Label(2,0,"地址");
            Label label4 = new Label(3,0,"公司");
            Label label5 = new Label(4,0,"邮箱");
            try {
                sheet.addCell(label1);
                sheet.addCell(label2);
                sheet.addCell(label3);
                sheet.addCell(label4);
                sheet.addCell(label5);
                WritableWorkbook.write();
                WritableWorkbook.close();
                File file = new File(filePath);
                if (file.exists() && !invitationCode.equals("err")){
                    JSON json = MyJSONUtils.contactsJson("0", invitationCode, null);
                    out.write(json.toString());
                    out.flush();
                    try {
                        SendSmsResponse response = SmsUtils.sendSms1(phoneNumber, adminName, invitationCode, password);
                        System.out.println("短信接口返回的数据----------------");
                        System.out.println("Code=" + response.getCode());
                        System.out.println("Message=" + response.getMessage());
                        System.out.println("RequestId=" + response.getRequestId());
                        System.out.println("BizId=" + response.getBizId());
                    } catch (ClientException e) {
                        e.printStackTrace();
                    }

                }else {
                    JSON json = MyJSONUtils.contactsJson("1", "创建失败，请重新再试", null);
                    out.write(json.toString());
                    out.flush();
                }
                out.close();

            } catch (WriteException e) {
                e.printStackTrace();
            }
        }else {
            JSON json = MyJSONUtils.contactsJson("1", "创建失败，请查看输入是否正确", null);
            out.write(json.toString());
       }
   }

    //导入通讯录
    @RequestMapping(value = "/LoadExcel",method = RequestMethod.GET)
    public void LoadExcel(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        String invitationCode = req.getParameter("invitationCode");
        String password = req.getParameter("password");
        String md5_password = MD5Utils.encode(password);
        if(excelService.checkPermission(md5_password,invitationCode)){
            try {

                String temp = Base64Utils.decode(invitationCode);
                String id = temp.substring(0,5);
                String path=excelService.findContactsNameById(Integer.valueOf(id));
                String filepath = "/Users/jason/Desktop/testexcel/"+path+".xls";
                Workbook rworkbook = Workbook.getWorkbook(new File(filepath));
                List<ContactsModel> contactsList = new ArrayList<>();
                Sheet sheet = rworkbook.getSheet(0);
                for(int i=1;i<sheet.getRows();i++){
                    ContactsModel contactsModel = new ContactsModel();
                    for(int j=0;j<sheet.getColumns();j++){

                        if(j==0){
                            String name = sheet.getCell(j, i).getContents(); //获取姓名列
                            contactsModel.setName(name);
                        }

                        if(j==1) {
                            String phoneNumber = sheet.getCell(j, i).getContents(); //获取号码列
                            contactsModel.setPhoneNumber(phoneNumber);
                        }
                        if(j==2){
                            String location = sheet.getCell(j, i).getContents(); //获取号码列
                            contactsModel.setLocation(location);
                        }
                        if(j==3){
                            String company = sheet.getCell(j, i).getContents(); //获取号码列
                            contactsModel.setCompany(company);
                        }
                        if(j==4){
                            String email = sheet.getCell(j, i).getContents(); //获取号码列
                            contactsModel.setEmail(email);
                        }
                    }

                    contactsList.add(contactsModel);

                }

                JSON json = MyJSONUtils.contactsJson("0", "通讯录解析成功", contactsList);
                out.write(json.toString());

            } catch (BiffException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
            JSON json = MyJSONUtils.contactsJson("1", "通讯录解析失败，请检查验证码和密码是否正确", null);
            out.write(json.toString());
        }
    }

    //获取管理员通讯录列表
    @RequestMapping(value = "/GetExcelList",method = RequestMethod.GET)
    public void GetExcelList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        String openId = req.getParameter("openId");
        List<ContactsListModel> excelListByAdmin = excelService.findExcelListByAdmin(openId);
        if(excelListByAdmin!=null){
            for(ContactsListModel contactsListModel:excelListByAdmin){
                String[] split = contactsListModel.getContactsName().split("\\+");//"+需要转义"
                contactsListModel.setContactsName(split[0]);
            }
            JSON contactsJson = MyJSONUtils.contactsJson("1", "获取通讯录列表成功", excelListByAdmin);
            System.out.println(contactsJson);
            out.write(contactsJson.toString());
            out.flush();
            out.close();
        }else{
            JSON contactsJson = MyJSONUtils.contactsJson("0", "获取通讯录列表失败", null);
            System.out.println(contactsJson);
            out.write(contactsJson.toString());
            out.flush();
            out.close();
        }
    }

    //删除通讯录
    @RequestMapping(value = "/DeleteExcel",method = RequestMethod.GET)
    public void DeleteExcel(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        PrintWriter out = resp.getWriter();
        String invitationCode = req.getParameter("invitationCode");
        boolean result =excelService.deleteExecl(invitationCode);

        if(result){
            JSON contactsJson = MyJSONUtils.contactsJson("0", "通讯录删除成功", null);
            out.write(contactsJson.toString());
            out.flush();
            out.close();
        }else {
            JSON contactsJson = MyJSONUtils.contactsJson("1", "通讯录删除失败，请稍后再试", null);
            out.write(contactsJson.toString());
            out.flush();
            out.close();
        }
    }

    //修改密码
    @RequestMapping(value = "/ModifyPwd",method = RequestMethod.GET)
    public void ModifyPwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        String invitationCode = req.getParameter("invitationCode");
        String password = req.getParameter("password");
        String md5_password = MD5Utils.encode(password);
        excelService.updatePassword(md5_password,invitationCode);

        JSON contactsJson = MyJSONUtils.contactsJson("0", "密码修改成功", null);
        out.write(contactsJson.toString());
        out.flush();
        out.close();
    }

    //下载通讯录文件
    @RequestMapping(value = "/DownloadExcel",method = RequestMethod.GET)
    public void DownloadExcel(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        String invitationCode = req.getParameter("invitationCode");
        String password = req.getParameter("password");
        System.out.println(invitationCode + password);
        String md5_password = MD5Utils.encode(password);
        String temp = Base64Utils.decode(invitationCode);
        String id = temp.substring(0,5);
        String path = excelService.findContactsNameById(Integer.parseInt(id));
        String fileName = path+".xls";
        if(excelService.checkPermission(md5_password,invitationCode)){
            JSON json = MyJSONUtils.contactsJson("0", fileName, null);
            out.write(json.toString());
            out.flush();
            out.close();
        }else {
            JSON json = MyJSONUtils.contactsJson("1", "下载失败,请检查邀请码和密码是否正确", null);
            out.write(json.toString());
            out.flush();
            out.close();

        }
    }

    //下载我的通讯录
    @RequestMapping(value = "/DownloadMyExcel",method = RequestMethod.GET)
    public void DownloadMyExcel(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        String invitationCode = req.getParameter("invitationCode");
        String password = req.getParameter("password");
        System.out.println(invitationCode + password);
        String temp = Base64Utils.decode(invitationCode);
        String id = temp.substring(0,5);
        String path = excelService.findContactsNameById(Integer.parseInt(id));
        String fileName = path+".xls";
        if(excelService.checkPermission(password,invitationCode)){
            JSON json = MyJSONUtils.contactsJson("0", fileName, null);
            out.write(json.toString());
            out.flush();
            out.close();
        }else {
            JSON json = MyJSONUtils.contactsJson("1", "下载失败,请检查邀请码和密码是否正确", null);
            out.write(json.toString());
            out.flush();
            out.close();

        }
    }

    //下载历史记录中通讯录
    @RequestMapping(value = "/DownloadHistoryExcel",method = RequestMethod.GET)
    public void DownloadHistoryExcel(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        String invitationCode = req.getParameter("invitationCode");
        String password = req.getParameter("password");
        System.out.println(invitationCode + password);
        String temp = Base64Utils.decode(invitationCode);
        String id = temp.substring(0,5);
        String path = excelService.findContactsNameById(Integer.parseInt(id));
        String fileName = path+".xls";
            JSON json = MyJSONUtils.contactsJson("0", fileName, null);
            out.write(json.toString());
            out.flush();
            out.close();

    }


}
