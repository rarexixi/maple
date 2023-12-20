package org.xi.maple.common.exception;

/**
 * @author xishihao
 */
public class MapleDataExistException extends MapleException {

    public MapleDataExistException() {
        super();
    }

    public MapleDataExistException(String message) {
        super(message);
    }

    public MapleDataExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapleDataExistException(Throwable cause) {
        super(cause);
    }
}
