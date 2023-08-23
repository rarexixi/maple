package org.xi.maple.common.constant;

public interface EngineExecutionStatus {
    String SUBMITTED = "SUBMITTED";
    String ACCEPTED = "ACCEPTED";
    String STARTING = "STARTING";
    String STARTED_FAILED = "STARTED_FAILED";
    String RUNNING = "RUNNING";
    String SUCCEED = "SUCCEED";
    String FAILED = "FAILED";
    String KILLED = "KILLED";
    String LOST = "LOST";

    static boolean isFinalStatus(String status) {
        return STARTED_FAILED.equals(status) || SUCCEED.equals(status) || FAILED.equals(status) || KILLED.equals(status) || LOST.equals(status);
    }

    static boolean canStartFailed(String status) {
        return SUBMITTED.equals(status) || ACCEPTED.equals(status) || STARTING.equals(status);
    }
}
