package org.xi.maple.mp.persistence.entity;

import java.math.BigDecimal;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 计算引擎扩展实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ClusterEngineEntityExt extends ClusterEngineEntity {

    /**
     * 所属集群
     */
    private String clusterText;

    private void setClusterText (String clusterText) {
        this.clusterText = clusterText;
    }

    private String getClusterText() {
        return clusterText;
    }
}
