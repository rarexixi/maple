package org.xi.maple.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClusterMessage {

    public static final String CLUSTER_CHANNEL = "maple-cluster-channel";

    private Type type;
    private String clusterName;

    public enum Type {
        ADD("add"),
        UPDATE("updage"),
        DELETE("delete");

        Type(String value) {
            this.value = value;
        }

        private final String value;

        private static final String DELIMITER = ":";

        public String getValue() {
            return value;
        }

        public String getMessage(String clusterName) {
            return String.format("%s%s%s", this.value, DELIMITER, clusterName);
        }
    }
}
