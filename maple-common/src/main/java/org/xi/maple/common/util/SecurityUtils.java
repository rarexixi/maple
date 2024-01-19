package org.xi.maple.common.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityUtils {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private static byte[] getSha256Bytes(String key, String s) throws NoSuchAlgorithmException, InvalidKeyException {
        return getSha256Bytes(key, s.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] getSha256Bytes(String key, byte[] bytes) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        sha256_HMAC.init(secret_key);
        return sha256_HMAC.doFinal(bytes);
    }

    public static String encrypt(String key, String s) throws NoSuchAlgorithmException, InvalidKeyException {
        return Base64.getEncoder().encodeToString(getSha256Bytes(key, s));
    }

    public static String encrypt(String key, byte[] bytes) throws NoSuchAlgorithmException, InvalidKeyException {
        return Base64.getEncoder().encodeToString(getSha256Bytes(key, bytes));
    }

    public static Boolean valid(String key, String s, String encryptStr) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] receivedHash = getSha256Bytes(key, s);
        return MessageDigest.isEqual(receivedHash, Base64.getDecoder().decode(encryptStr));
    }
}
