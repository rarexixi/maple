package org.xi.maple.builder.exception;

import org.xi.maple.common.exception.MapleException;

public class GenerateCommandsException extends MapleException {

    public GenerateCommandsException(String message) {
        super(message);
    }

    public GenerateCommandsException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenerateCommandsException(Throwable cause) {
        super(cause);
    }
}
