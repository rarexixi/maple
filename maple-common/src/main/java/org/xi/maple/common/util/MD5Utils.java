package org.xi.maple.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MD5Utils {

    private static final String defaultEncoding = "UTF-8";

    public static String getMd5(String str) {
        return getMd5(str, defaultEncoding);
    }

    public static String getMd5(String data, String encoding)  {
        MessageDigest messagedigest;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

        try {
            byte[] baseStr = Base64.getEncoder().encode(messagedigest.digest(data.getBytes(encoding)));
            return new String(baseStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
