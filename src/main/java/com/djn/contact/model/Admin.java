package com.djn.contact.model;

import java.sql.Timestamp;

public class Admin {
    private int id;
    private String openId;
    private String password;
    private String adminName;

    private String contactsName;
    private String phoneNumber;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String invitationCode;
    private String delTime;

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", password='" + password + '\'' +
                ", adminName='" + adminName + '\'' +
                ", contactsName='" + contactsName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", invitationCode='" + invitationCode + '\'' +
                ", delTime='" + delTime + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getDelTime() {
        return delTime;
    }

    public void setDelTime(String delTime) {
        this.delTime = delTime;
    }
}
