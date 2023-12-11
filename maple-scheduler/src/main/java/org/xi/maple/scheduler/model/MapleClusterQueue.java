package org.xi.maple.scheduler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xishihao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapleClusterQueue implements Serializable {

    private Integer pendingApps;

    public static String getKey(String clusterName, String queueName) {
        return clusterName + "-" + queueName;
    }
}
