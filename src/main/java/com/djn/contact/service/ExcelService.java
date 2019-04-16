package com.djn.contact.service;



import com.djn.contact.mapper.ExcelMapper;
import com.djn.contact.model.Admin;
import com.djn.contact.model.ContactsListModel;
import com.djn.contact.util.Base64Utils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    ExcelMapper excelMapper;

    public  String addExcel(Admin admin) {
        String msg = "err";
        try {
            excelMapper.addExcel(admin);
            String id = String.valueOf(admin.getId());

            String invitationCode = Base64Utils.encode(id, admin.getPhoneNumber());
            msg = invitationCode;
            admin.setInvitationCode(invitationCode);
            excelMapper.addInvitationCode(admin.getId(),invitationCode);
            return msg;
        } catch (DataAccessException e) {
            return msg;
        }
    }

    public  String findContactsNameById(int id){
        return excelMapper.findContactsNameById(id);
    }

    public Boolean checkPermission(String password,String invitationCode){
        return excelMapper.checkPermission(password,invitationCode);
    }

    public List<ContactsListModel> findExcelListByAdmin(String openId){
        return excelMapper.findExcelListByAdmin(openId);
    }

    public  boolean deleteExecl(String invitationCode){
        String temp = Base64Utils.decode(invitationCode);
        int id =Integer.parseInt(temp.substring(0,5)) ;
        String contactsName =excelMapper.findContactsNameById(id);
        String filePath = "/Users/jason/Desktop/testexcel/"+contactsName+".xls";
        File file = new File(filePath);
        if (file.exists()){
            if(file.delete()){
               excelMapper.deleteExcel(invitationCode);
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public void updatePassword(String password, String invitationCode){
        excelMapper.updatePassword(password,invitationCode);
    }
}
