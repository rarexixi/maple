package org.xi.maple.common.exception;

/**
 * @author xishihao
 */
public class MapleValidException extends MapleException {

    public MapleValidException() {
        super();
    }

    public MapleValidException(String message) {
        super(message);
    }

    public MapleValidException(Throwable t) {
        super(t);
    }

    public MapleValidException(String message, Throwable t) {
        super(message, t);
    }
}
