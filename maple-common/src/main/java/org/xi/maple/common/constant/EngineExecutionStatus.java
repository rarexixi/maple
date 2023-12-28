package org.xi.maple.common.constant;

public enum EngineExecutionStatus {
    CREATED("新建任务", true, true, true, true),
    ACCEPTED("加入队列", false, true, true, false),
    STARTING("提交集群/开始启动", false, false, true, false),
    START_FAILED("提交集群失败/启动失败", false, false, true, true),
    RUNNING("运行中", false, false, false, false),
    SUCCEED("运行成功", false, false, false, true),
    FAILED("运行失败", false, false, false, true),
    KILLED("强制杀死", false, false, false, true),
    CANCELED("作业停止/取消", false, false, false, true),
    UNKNOWN("未知", false, false, false, true);

    EngineExecutionStatus(String desc, boolean canAccept, boolean canStart, boolean canStartFailed, boolean isFinalStatus) {
        this.desc = desc;
        this.canAccept = canAccept;
        this.canStart = canStart;
        this.canStartFailed = canStartFailed;
        this.isFinalStatus = isFinalStatus;
    }

    final String desc;
    final boolean canAccept;
    final boolean canStart;
    final boolean canStartFailed;
    final boolean isFinalStatus;

    public boolean canAccept() {
        return canAccept;
    }

    public boolean canStart() {
        return canStart;
    }

    public boolean canStartFailed() {
        return canStartFailed;
    }

    public boolean isFinalStatus() {
        return isFinalStatus;
    }
}
