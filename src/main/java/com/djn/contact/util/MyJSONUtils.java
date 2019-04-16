package com.djn.contact.util;


import com.djn.contact.model.JsonMsgModel;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

import java.util.List;

public class MyJSONUtils {
    public static JSON contactsJson(String code, String msg, List data){

        JsonMsgModel jsonMsgModel = new JsonMsgModel(code, msg, data);
        JSONObject jsonObject = JSONObject.fromObject(jsonMsgModel);
        return  jsonObject;
    }
}
