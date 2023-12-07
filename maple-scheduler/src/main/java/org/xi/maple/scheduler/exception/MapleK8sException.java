package org.xi.maple.scheduler.exception;

public class MapleK8sException extends RuntimeException {
    public MapleK8sException(Throwable t) {
        super(t);
    }
    
    public MapleK8sException(String message) {
        super(message);
    }
}
