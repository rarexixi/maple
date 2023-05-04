package org.xi.maple.common.constant;

/**
 * @author xishihao
 */
public interface JobPriorityConstants {
    int VERY_LOW = 1;
    int LOW = 2;
    int NORMAL = 3;
    int HIGH = 4;
    int VERY_HIGH = 5;

    /**
     * 增加优先级
     *
     * @param priority 原始优先级
     * @return 增加后的优先级
     */
    static int increasePriority(int priority) {
        return Math.min(priority + 1, VERY_HIGH);
    }
}
