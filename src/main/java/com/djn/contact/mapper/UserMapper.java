package com.djn.contact.mapper;


import com.djn.contact.model.Admin;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface UserMapper {

    List<Admin> findAdminByOpenId(String openId);




}
