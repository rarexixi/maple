package org.xi.maple.common.util;

import org.apache.commons.lang3.StringUtils;
import org.xi.maple.common.exception.MapleValidException;

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
        Mac sha256_HMAC = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        sha256_HMAC.init(secret_key);
        return sha256_HMAC.doFinal(s.getBytes(StandardCharsets.UTF_8));
    }

    public static String encrypt(String key, String s) throws NoSuchAlgorithmException, InvalidKeyException {
        return Base64.getEncoder().encodeToString(getSha256Bytes(key, s));
    }

    public static Boolean valid(String key, String s, String encryptStr) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] receivedHash = getSha256Bytes(key, s);
        return MessageDigest.isEqual(receivedHash, Base64.getDecoder().decode(encryptStr));
    }

    public static void checkSecurity(String secretKey, String secret, Long timestamp, String... fieldValues) {
        if (StringUtils.isBlank(secretKey)) {
            throw new MapleValidException("应用不存在/设置不正确");
        }
        if (System.currentTimeMillis() - timestamp > 1000 * 60 * 5) {
            throw new MapleValidException("请求已过期");
        }

        String secretStr = timestamp + ";#" + String.join("#;", fieldValues);
        try {
            if (!SecurityUtils.valid(secretKey, secretStr, secret)) {
                throw new MapleValidException("参数验证失败");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new MapleValidException("参数验证失败", e);
        }
    }
}
