package org.xi.maple.rest.service;

public interface SecurityService {

    boolean authenticate(String fromApp, String secret, Long timestamp, String secretStr);
}
