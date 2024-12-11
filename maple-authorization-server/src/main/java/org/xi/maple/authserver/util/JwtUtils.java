package org.xi.maple.authserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Map;

@Setter
public class JwtUtils {
    private static final String ALGORITHM = "RSA";
    private final PrivateKey PRIVATE_KEY;
    private final PublicKey PUBLIC_KEY;
    private Long expiration = 3600000L;

    public JwtUtils(String privateKey, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] priKey = Base64.decodeBase64(privateKey);
        PRIVATE_KEY = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(priKey));

        byte[] pubKey = Base64.decodeBase64(publicKey);
        PUBLIC_KEY = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(pubKey));
    }

    public String createJwt(String subject, Map<String, Object> claims) {
        long timestamp = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(timestamp))
                .expiration(new Date(timestamp + expiration)) // 有效期为1小时
                .signWith(PRIVATE_KEY)
                .compact();
    }

    public Claims parseJWT(String jwt) {
        return Jwts.parser()
                .verifyWith(PUBLIC_KEY).build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
