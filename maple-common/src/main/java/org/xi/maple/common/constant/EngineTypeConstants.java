package org.xi.maple.common.constant;

/**
 * @author xishihao
 */
public interface EngineTypeConstants {

    /**
     * 常驻引擎
     */
    String RESIDENT = "resident";

    /**
     * 单次引擎
     */
    String ONCE = "once";

    /**
     * 判断是否是单次执行的引擎
     *
     * @param engineType 引擎类型
     * @return 是否是单次执行的引擎
     */
    static boolean isOnce(String engineType) {
        return ONCE.equals(engineType);
    }

    /**
     * 获取作业对应的引擎类型
     *
     * @param jobType 作业类型
     * @return 引擎类型
     */
    static String getEngineTypeByJob(String jobType) {
        return JobTypeConstants.ONCE.equals(jobType) ? ONCE : RESIDENT;
    }
}
