package org.xi.maple.common.exception;

/**
 * @author xishihao
 */
public class MapleException extends RuntimeException {

    public MapleException() {
        super();
    }

    public MapleException(String message) {
        super(message);
    }

    public MapleException(Throwable t) {
        super(t);
    }

    public MapleException(String message, Throwable t) {
        super(message, t);
    }
}
