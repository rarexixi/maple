package org.xi.maple.common.exception;

public class MapleEngineTypeNotSupportException extends MapleK8sException {
    public MapleEngineTypeNotSupportException(Throwable t) {
        super(t);
    }

    public MapleEngineTypeNotSupportException(String message) {
        super(message);
    }
}
