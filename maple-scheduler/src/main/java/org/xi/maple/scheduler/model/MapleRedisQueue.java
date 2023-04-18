package org.xi.maple.scheduler.model;

import lombok.Data;

/**
 * @author xishihao
 */
@Data
public class MapleRedisQueue {
    private String queueName;
    private String lockName;
}
