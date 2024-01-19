package org.xi.maple.common.exception;

public class MapleAuthenticationException extends MapleException {

    public MapleAuthenticationException() {
    }

    public MapleAuthenticationException(Throwable t) {
        super(t);
    }

    public MapleAuthenticationException(String message) {
        super(message);
    }

    public MapleAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
