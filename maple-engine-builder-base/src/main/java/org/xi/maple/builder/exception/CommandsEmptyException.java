package org.xi.maple.builder.exception;

import org.xi.maple.common.exception.MapleException;

public class CommandsEmptyException extends MapleException {

    public CommandsEmptyException(String message) {
        super(message);
    }

    public CommandsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandsEmptyException(Throwable cause) {
        super(cause);
    }
}
