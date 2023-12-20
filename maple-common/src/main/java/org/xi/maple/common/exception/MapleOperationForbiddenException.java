package org.xi.maple.common.exception;

/**
 * @author xishihao
 */
public class MapleOperationForbiddenException extends MapleException {
    public MapleOperationForbiddenException() {
        super();
    }

    public MapleOperationForbiddenException(String message) {
        super(message);
    }

    public MapleOperationForbiddenException(Throwable t) {
        super(t);
    }

    public MapleOperationForbiddenException(String message, Throwable t) {
        super(message, t);
    }
}
