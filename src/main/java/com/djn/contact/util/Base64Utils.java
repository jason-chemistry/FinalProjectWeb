package com.djn.contact.util;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;


public class Base64Utils {

    private static final Base64 BASE64 = new Base64();
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public static String encode(String id,String phoneNumber) {
            if (!isEmpty(id) && !isEmpty(phoneNumber)) {
                new Base64();
                String target = id+ phoneNumber.substring(7);
                byte[] bytes = BASE64.encode(target.getBytes(DEFAULT_CHARSET));
                return new String(bytes, DEFAULT_CHARSET);
            }
            return id;
    }

    public static String decode(String source) {
        if (!isEmpty(source)) {
            byte[] bytes = BASE64.decode(source.getBytes(DEFAULT_CHARSET));
            String target = new String(bytes, DEFAULT_CHARSET);
            return target;
        }

        return source;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

//    public static void main(String[] args) {
//        String target = Base64Utils.encode("admin","2018-05-20 16:26:25");
//        System.out.println("encode : " + target);
////        String source = Base64Utils.decode("WlRFSUNUbWF4eWFuZw==");
////        System.out.println("source : " + source);
//    }

}
