package com.djn.contact.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {

    public static String encode(String str) {
        String result = DigestUtils.md5Hex(str);
        return result;
    }
}
