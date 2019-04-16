package com.djn.contact.model;

import java.util.List;

public class JsonMsgModel {
    private String code;
    private String msg;
    private List data;

    public JsonMsgModel(String code, String msg, List data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonMsgModel() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
