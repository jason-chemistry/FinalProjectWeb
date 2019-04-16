package com.djn.contact.controller;

import com.djn.contact.mapper.UserMapper;
import com.djn.contact.service.ExcelService;
import com.djn.contact.service.UserService;
import com.djn.contact.util.Base64Utils;
import com.djn.contact.util.HttpUtils;
import com.djn.contact.util.MD5Utils;
import com.djn.contact.util.MyJSONUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
@RestController
public class UserController {
    private String APPID = "wx806055287aa7ad9b";
    private String APPSECRET = "4abdef1ccb6323153c8068e59b356cdd";
    private String CODE = "";
    private String WX_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=APPSECRET&js_code=CODE&grant_type=authorization_code";

    @Autowired
    UserService userService;

    @Autowired
    ExcelService excelService;

    //获取微信openId
    @RequestMapping(value = "/GetOpenId",method = RequestMethod.GET)
    public void getOpenId (HttpServletResponse response, HttpServletRequest request)  throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("gbk");
        PrintWriter out = response.getWriter();
        CODE = request.getParameter("code");
        System.out.println(CODE);
        String requestUrl = WX_URL.replace("APPID", APPID).
                replace("APPSECRET", APPSECRET).
                replace("CODE", CODE);

        String result = HttpUtils.GET(requestUrl);
        JSONObject resultObject = JSONObject.fromObject(result);
        String openid = resultObject.getString("openid");
        out.write(openid);
    }
    //上传个人信息到固定通讯录
    @RequestMapping(value = "/UploadPersonalInfo",method = RequestMethod.GET)
    public void UploadPersonalInfo (HttpServletResponse resp, HttpServletRequest req)  throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String phoneNumber = req.getParameter("phoneNumber");
        String name = req.getParameter("name");
        String invitationCode = req.getParameter("invitationCode");
        String password = req.getParameter("password");
        String company=req.getParameter("company");
        String location=req.getParameter("location");
        String email=req.getParameter("email");
        String md5_passoword = MD5Utils.encode(password);

        if(excelService.checkPermission(md5_passoword,invitationCode)){
            int flag = 0;//用于记录重名的行
            //从邀请码中获取文件名字
            String temp = Base64Utils.decode(invitationCode);
            String id = temp.substring(0,5);
            String path=excelService.findContactsNameById(Integer.valueOf(id));
            String filepath = "/Users/jason/Desktop/testexcel/"+path+".xls";
            try {
                Workbook rwb = Workbook.getWorkbook(new File(filepath));
                WritableWorkbook wwb = Workbook.createWorkbook(new File(filepath), rwb);
                Sheet sheet = rwb.getSheet(0);
                WritableSheet ws = wwb.getSheet(0);
                Double phone = Double.valueOf(phoneNumber); //转化为excel表所要求的number类型

                for (int i = 1; i < sheet.getRows(); i++) {
                    String nameItem = sheet.getCell(0, i).getContents();
                    //重名判断
                    if (name.equals(nameItem)) {
                        flag = i;
                        break;
                    }
                }

                if (flag != 0) {
                    WritableCell wc_name = ws.getWritableCell(0, flag);
                    WritableCell wc_phoneNumber = ws.getWritableCell(1, flag);
                    WritableCell wc_location= ws.getWritableCell(2, flag);
                    WritableCell wc_company = ws.getWritableCell(3, flag);
                    WritableCell wc_email = ws.getWritableCell(4, flag);

                    Label label = (Label) wc_name;
                    Number number = (Number) wc_phoneNumber;
                    Label label1 = (Label) wc_location;
                    Label label2 = (Label) wc_company;
                    Label label3 = (Label) wc_email;

                    label.setString(name);
                    number.setValue(phone);
                    label1.setString(location);
                    label2.setString(company);
                    label3.setString(email);
                } else {
                    int rows = ws.getRows();

                    Label label = new Label(0, rows, name);
                    Number number = new Number(1, rows, phone);
                    Label label1 = new Label(2, rows, location);
                    Label label2 = new Label(3, rows, company);
                    Label label3 = new Label(4, rows, email);
                    try {
                        ws.addCell(label);

                        ws.addCell(number);
                        ws.addCell(label1);
                        ws.addCell(label2);
                        ws.addCell(label3);

                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }

                wwb.write();
                wwb.close();
                rwb.close();

                JSON json = MyJSONUtils.contactsJson("0", "个人信息上传成功", null);
                out.write(json.toString());
                out.flush();
                out.close();

            } catch (BiffException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }else{
            JSON json = MyJSONUtils.contactsJson("1", "个人信息上传失败,请检查邀请码和密码是否正确", null);
            out.write(json.toString());
            out.flush();
            out.close();
        }
    }





}