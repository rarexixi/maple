package org.xi.maple.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xishihao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapleClusterQueue {

    private Integer pendingApps;

    public static String getKey(String clusterName,String queueName) {
        return clusterName + "--" + queueName;
    }
}
