package org.xi.maple.common.exception;

public class MapleClusterNotSupportException extends MapleK8sException {
    public MapleClusterNotSupportException(Throwable t) {
        super(t);
    }

    public MapleClusterNotSupportException(String message) {
        super(message);
    }
}
