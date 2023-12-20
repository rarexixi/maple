package org.xi.maple.common.exception;

public class MapleClusterNotSupportException extends MapleK8sException {
    public MapleClusterNotSupportException() {
    }

    public MapleClusterNotSupportException(Throwable t) {
        super(t);
    }

    public MapleClusterNotSupportException(String message) {
        super(message);
    }

    public MapleClusterNotSupportException(String message, Throwable t) {
        super(message, t);
    }
}
