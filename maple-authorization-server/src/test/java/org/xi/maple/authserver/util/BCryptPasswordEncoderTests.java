package org.xi.maple.authserver.util;

import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderTests {

    @Test
    public void encodeTest() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String admin = passwordEncoder.encode("admin");
        System.out.println(admin);
        Assert.isTrue(passwordEncoder.matches("admin", admin));
    }

}
