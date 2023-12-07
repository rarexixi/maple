package org.xi.maple.scheduler.exception;

public class ClusterNotConfiguredExceptionMaple extends MapleK8sException {
    public ClusterNotConfiguredExceptionMaple(Throwable t) {
        super(t);
    }

    public ClusterNotConfiguredExceptionMaple(String message) {
        super(message);
    }
}
