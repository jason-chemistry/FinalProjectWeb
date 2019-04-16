package com.djn.contact.mapper;

import com.djn.contact.model.Admin;
import com.djn.contact.model.ContactsListModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ExcelMapper {

    void addExcel(Admin admin) throws DataAccessException;
    String findContactsNameById(int id) throws DataAccessException;
    Boolean checkPermission(@Param("password") String password, @Param("invitationCode") String invitationCode);
    void addInvitationCode(@Param("id")int id,@Param("invitationCode") String invitationCode );
    List<ContactsListModel> findExcelListByAdmin(@Param("openId")String openId);
    void deleteExcel(@Param("invitationCode") String invitationCode);
    void updatePassword(@Param("password") String password, @Param("invitationCode") String invitationCode);
    List<Admin> needToDelete();
}
