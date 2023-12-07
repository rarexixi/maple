package org.xi.maple.scheduler.exception;

public class EngineTypeNotSupportExceptionMaple extends MapleK8sException {
    public EngineTypeNotSupportExceptionMaple(Throwable t) {
        super(t);
    }

    public EngineTypeNotSupportExceptionMaple(String message) {
        super(message);
    }
}
