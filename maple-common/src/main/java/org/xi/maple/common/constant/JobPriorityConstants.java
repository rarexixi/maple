package org.xi.maple.common.constant;

/**
 * @author xishihao
 */
public enum JobPriorityConstants {

    VERY_LOW(1),
    LOW(2),
    NORMAL(3),
    HIGH(4),
    VERY_HIGH(5);

    JobPriorityConstants(int value) {
        this.value = value;
    }

    int value = 3;

    /**
     * 增加优先级
     *
     * @param priority 原始优先级
     * @return 增加后的优先级
     */
    static int increasePriority(int priority) {
        return Math.min(priority + 1, VERY_HIGH.value);
    }
}
