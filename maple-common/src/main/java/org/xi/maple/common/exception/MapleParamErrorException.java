package org.xi.maple.common.exception;

/**
 * @author xishihao
 */
public class MapleParamErrorException extends MapleException {
    public MapleParamErrorException() {
        super();
    }

    public MapleParamErrorException(String message) {
        super(message);
    }

    public MapleParamErrorException(Throwable t) {
        super(t);
    }

    public MapleParamErrorException(String message, Throwable t) {
        super(message, t);
    }
}
