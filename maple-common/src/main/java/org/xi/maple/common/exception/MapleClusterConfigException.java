package org.xi.maple.common.exception;

public class MapleClusterConfigException extends MapleK8sException {
    public MapleClusterConfigException() {
        super();
    }

    public MapleClusterConfigException(Throwable t) {
        super(t);
    }

    public MapleClusterConfigException(String message) {
        super(message);
    }

    public MapleClusterConfigException(String message, Throwable t) {
        super(message, t);
    }
}
