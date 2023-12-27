package org.xi.maple.common.constant;

public interface EngineExecutionStatus {

    String CREATED = "CREATED";               // 新增，刚加入数据库，默认状态
    String ACCEPTED = "ACCEPTED";             // 添加系统队列
    String STARTING = "STARTING";             // 提交集群/开始启动
    String START_FAILED = "START_FAILED";     // 提交集群失败/启动失败
    String RUNNING = "RUNNING";               // 运行中
    String SUCCEED = "SUCCEED";               // 运行成功
    String FAILED = "FAILED";                 // 运行失败
    String KILLED = "KILLED";                 // 强制杀死
    String CANCELED = "CANCELED";             // 作业停止/取消
    String LOST = "LOST";                     // 无法获取作业信息

    static boolean isFinalStatus(String status) {
        return START_FAILED.equals(status) || SUCCEED.equals(status) || FAILED.equals(status) || KILLED.equals(status) || LOST.equals(status);
    }

    static boolean canStartFailed(String status) {
        return CREATED.equals(status) || ACCEPTED.equals(status) || STARTING.equals(status);
    }
}
