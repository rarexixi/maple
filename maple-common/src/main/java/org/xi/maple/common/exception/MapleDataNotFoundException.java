package org.xi.maple.common.exception;

/**
 * @author xishihao
 */
public class MapleDataNotFoundException extends MapleException {
    public MapleDataNotFoundException() {
        super();
    }

    public MapleDataNotFoundException(String message) {
        super(message);
    }

    public MapleDataNotFoundException(Throwable t) {
        super(t);
    }

    public MapleDataNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}
