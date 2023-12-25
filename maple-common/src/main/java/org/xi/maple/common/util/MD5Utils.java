package org.xi.maple.common.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MD5Utils {

    public static String getMd5(String str) {
        return getMd5(str, StandardCharsets.UTF_8.name());
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
