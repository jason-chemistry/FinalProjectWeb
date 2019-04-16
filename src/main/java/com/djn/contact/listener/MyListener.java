package com.djn.contact.listener;



import com.aliyuncs.exceptions.ClientException;
import com.djn.contact.mapper.ExcelMapper;
import com.djn.contact.model.Admin;
import com.djn.contact.service.ExcelService;
import com.djn.contact.util.SmsUtils;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@WebListener
public class MyListener implements ServletContextListener {

    @Autowired
    ExcelMapper excelMapper;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<Admin> admins = excelMapper.needToDelete();
                System.out.println(admins.get(0).toString());
                for (Admin admin: admins){
                    String name = admin.getAdminName();
                    String temp_time = admin.getDelTime();
                    String phoneNumber = admin.getPhoneNumber();
                    String time = temp_time.substring(0,10)+"凌晨";
                    String real_contacts = admin.getContactsName();
                    String[] split = real_contacts.split("\\+");//"+需要转义"
                    String contacts = split[0];
                    System.out.println(name+" "+time+" "+contacts);
                    String filePath = "/Users/jason/Desktop/testexcel/" + real_contacts + ".xls";
                    File file = new File(filePath);
                    if (file.exists()) {
                        if (file.delete()) {
                            try {
                                excelMapper.deleteExcel(admin.getInvitationCode());
                                SmsUtils.sendSms2(name,time,contacts,phoneNumber);
                            } catch (ClientException e) {
                                e.printStackTrace();
                            }

                        } else {
                            System.out.println("删除失败");
                        }
                    } else {
                        System.out.println("文件不存在");
                    }
                }


            }
        }, 2000,3*1000*60);
    }


}
