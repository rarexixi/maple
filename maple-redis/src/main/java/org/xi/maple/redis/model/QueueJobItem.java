package org.xi.maple.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * redis 队列作业对象
 *
 * @author xishihao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueueJobItem {

    private Integer jobId;
    private Long timestamp;
}
