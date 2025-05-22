package org.xi.maple.builder.exception;

import org.xi.maple.common.exception.MapleException;

public class ConvertorNotExistException extends MapleException {

    public ConvertorNotExistException(String message) {
        super(message);
    }

    public ConvertorNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertorNotExistException(Throwable cause) {
        super(cause);
    }
}
