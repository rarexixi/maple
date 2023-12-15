package org.xi.maple.common.exception;

public class MapleClusterNotConfiguredException extends MapleK8sException {
    public MapleClusterNotConfiguredException(Throwable t) {
        super(t);
    }

    public MapleClusterNotConfiguredException(String message) {
        super(message);
    }
}
