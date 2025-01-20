package org.xi.maple.common.model;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClusterMessage implements Serializable {

    public static final String CLUSTER_CHANNEL = "maple-cluster-channel";

    private Type type;
    private Integer clusterId;

    @Getter
    public enum Type {
        ADD("add"),
        UPDATE("updage"),
        DELETE("delete");

        Type(String value) {
            this.value = value;
        }

        private final String value;

        private static final String DELIMITER = ":";
    }
}