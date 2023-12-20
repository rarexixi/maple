package org.xi.maple.common.exception;

public class MapleClusterNotConfiguredException extends MapleK8sException {
    public MapleClusterNotConfiguredException() {
        super();
    }

    public MapleClusterNotConfiguredException(Throwable t) {
        super(t);
    }

    public MapleClusterNotConfiguredException(String message) {
        super(message);
    }

    public MapleClusterNotConfiguredException(String message, Throwable t) {
        super(message, t);
    }
}
