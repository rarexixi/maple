package org.xi.maple.authserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

public class JwtUtilsTests {

    String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCrwNJ4KTjk6e2acrEFSmGhdKEfBmt64bJ/xt3cpI+QbHCg9xY0FSxVArVGUT6nwMjpfML6Ppg5mA2y+5F8Er2HyFqo0Z7aTIgPTqc6jKQZTQ0/pFPoDR4O0PmxcEJTj9Sd+bImQ96fGS/TW/BJ4CVeFeaRBwM8s85/kixK9mhi1qbxwVBZ6W/JrmF8HiVN0GWB+mTXe6rP+LDe26ULU9FOUwCx4SkkknBz5Xa5BqaX3ov9c+n9MntqBKfgxRBe6LwngKRM7yHbP5pE2Xn1XvEYPsoU+/2DGM/LfNPgbBdptN7sxm0iBIxPIO7eKmyb47GjZ2kRhLGRfc1NIuhbvBOzAgMBAAECggEBAJCjrttgo7grxFwYEDnLXRLSHEn7ZxIx9dejiv2gXAKdP/BehQVRe2cQA8b27pPpR4lR9wU1J48BHTBIqUlaouAxex/GgEeIweBaNXyaKivgdrLM2ZiHS3PgyxaB2mlyq/Rp7nxJFbbYSC1vtJkSOYXVXblutWwuZE2MtN5gSqJIjqURn3OT4C2X0sftn6WrUuqLBxRDqGjtcrHZjb9b2q9C0XM0ZMVocRCINmoJu6OkG2NR46+qj5KNSHz7pFwbbf6tVrWLah1naPHpYhdk5KSuJJAKxpxdzUHg3AiCu40kwvZSmUACmfu9ki8144q+aZ9nljrrI0ydlhiEcwuVliECgYEA33ToP4xpFrjjUJ4Lqr9jPUBxwym74eD63Q8oMbehvY2ylnOq079qPv/She70FHZ4JTUFwfp7J107t5k6LJFgM+skDUOS3bgqDe0hrOChTfIZWOIM3GMqMbxp/YQZvLErrqGtqyECAv6TIYW/x+gHdM+pdHEY8UNQ6w/NGMcaynECgYEAxMRDwt3lj0vI7QN0w7nxwOhXajOezW9M9fxns7kHXSjqN/GWAr+WRTfJdD3S9fY6AvKnF9REAtIzvjmPyoUYiDBcSWTw7y4mKCah6+xLLdXru/g/PKAYSBdw1x/PSlfVEX+VKabSITLetyM2nqtv5oszVTqiIZfVkQZTUBHdamMCgYAuQiZeGXQb1xR/uOgTZeFmabPjQf16CjgEkUGx4SDje7Tvvmwrv6F//zeWsFvo7Il8slhJys+522wkWt0XItU2ikQBVu7NZXWXQnLJNgtOdoLWypz89q5ic23BpJorXMX9Nc0c8w1Wj1roAqcr/BWnEeIzj8IpF9Vlg/TiXz1lkQKBgAsy6HBk1LXtKEhlE/Y31QyXrLH8siMbHJQ7g2N1OngQ5hO9Cj6pzNGvtSWKh0E8fFzClsQCbsSCVjOTbx2lqZnGttnuhs/2HnHWkQyubDD1pe8S1/aJXhhsTR3RT6j5isTuL/0o43jEPmzJ8kfhlgLXFVradXPYAm24oHFEMwflAoGAKl7G71eOpo/b46dpP/RXkg912I6ECcGNhaL1l1mwXWaQ9FSSmhPxIuTen+j7HrlgXwtIVojbfI1FDqhK6/lneTXe1RJtwe+T9pbs80Uj6rgcsHb0z/v7hB/LuhM1J2GMmRjIphXUEHISkC+Tt97RMhNHPZu3Ebgkwfci7QUd5ks=";
    String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq8DSeCk45OntmnKxBUphoXShHwZreuGyf8bd3KSPkGxwoPcWNBUsVQK1RlE+p8DI6XzC+j6YOZgNsvuRfBK9h8haqNGe2kyID06nOoykGU0NP6RT6A0eDtD5sXBCU4/UnfmyJkPenxkv01vwSeAlXhXmkQcDPLPOf5IsSvZoYtam8cFQWelvya5hfB4lTdBlgfpk13uqz/iw3tulC1PRTlMAseEpJJJwc+V2uQaml96L/XPp/TJ7agSn4MUQXui8J4CkTO8h2z+aRNl59V7xGD7KFPv9gxjPy3zT4GwXabTe7MZtIgSMTyDu3ipsm+Oxo2dpEYSxkX3NTSLoW7wTswIDAQAB";

    @Test
    public void createJwtTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        JwtUtils jwtUtils = new JwtUtils(privateKey, publicKey);
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "xishihao");

        String jwt = jwtUtils.createJwt("1234567890", claims);
        System.out.println(jwt);
        Assert.assertNotNull(jwt);

        Claims x = jwtUtils.parseJWT(jwt);
        String username = x.get("username", String.class);
        Assert.assertEquals(username, "xishihao");
        Assert.assertEquals(x.getSubject(), "1234567890");
    }

    @Test
    public void parseJWTTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        JwtUtils jwtUtils = new JwtUtils(privateKey, publicKey);
        String jwt = "eyJhbGciOiJSUzI1NiJ9.eyJ1c2VybmFtZSI6Inhpc2hpaGFvIiwic3ViIjoiMTIzNDU2Nzg5MCIsImlhdCI6MTczMzEzMDA3NCwiZXhwIjoxNzMzMTMzNjc0fQ.fRg4A3ndi2Yq6S8SIGmfhuqTx9mEnFJp3o1xy5lTtDlIHF_aJTVrFedGqOCuCQh0cmZdP09jHqLAxypPm-XrTxIuRcx613oOky-LhgQ-Tb_uNzF48leVXHD-_90fE6jQsPjSHVu8sjYnmfYD_EDibZ5un4xLyHgaK5muNHgq5-RxPOesZLCr20Nh9zo1xZ9HgbXUEgVwnSCA2Q2VkTANKfAQou7baW8p6JBSp6p94s9kXi0aY8x6EAKjwZMD4X-L5N9t7uabzIasPgKecMOCK7-7PVprBZ27TAN3z0Zbgk_7IvTWrcBLcTDh891KOerV08OzHxzHrKh2rAxXW2Y52Q";
        Claims x = jwtUtils.parseJWT(jwt);
        String username = x.get("username", String.class);
        Assert.assertEquals(username, "xishihao");
        Assert.assertEquals(x.getSubject(), "1234567890");
    }

    @Test
    public void genSecretTest() {
        KeyPair keyPair = Keys.keyPairFor(io.jsonwebtoken.SignatureAlgorithm.RS256);
        System.out.println(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));
        System.out.println(Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
    }
}
