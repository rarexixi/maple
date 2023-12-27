package org.xi.maple.common.constant;

public interface EngineExecutionStatus {
    // CREATED, ACCEPTED, STARTING, START_FAILED, RUNNING, SUCCEED, FAILED, KILLED, LOST
    String CREATED = "CREATED";
    String ACCEPTED = "ACCEPTED";
    String STARTING = "STARTING";
    String START_FAILED = "START_FAILED";
    String RUNNING = "RUNNING";
    String SUCCEED = "SUCCEED";
    String FAILED = "FAILED";
    String KILLED = "KILLED";
    String CANCELED = "CANCELED";
    String LOST = "LOST";

    static boolean isFinalStatus(String status) {
        return START_FAILED.equals(status) || SUCCEED.equals(status) || FAILED.equals(status) || KILLED.equals(status) || LOST.equals(status);
    }

    static boolean canStartFailed(String status) {
        return CREATED.equals(status) || ACCEPTED.equals(status) || STARTING.equals(status);
    }
}
