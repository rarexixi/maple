package org.xi.maple.common.exception;

import org.xi.maple.common.exception.MapleException;

public class MapleK8sException extends MapleException {
    public MapleK8sException() {
    }

    public MapleK8sException(Throwable t) {
        super(t);
    }

    public MapleK8sException(String message) {
        super(message);
    }

    public MapleK8sException(String message, Throwable t) {
        super(message, t);
    }
}
