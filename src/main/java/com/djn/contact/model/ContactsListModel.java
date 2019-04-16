package com.djn.contact.model;

public class ContactsListModel {
    private  int id;
    private String contactsName;
    private String invitationCode;
    private String password;


    @Override
    public String toString() {
        return "ContactsListModel{" +
                "id=" + id +
                ", contactsName='" + contactsName + '\'' +
                ", invitationCode='" + invitationCode + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContactsListModel() {
    }

    public String getContactsName() {
        return contactsName;
    }



    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
