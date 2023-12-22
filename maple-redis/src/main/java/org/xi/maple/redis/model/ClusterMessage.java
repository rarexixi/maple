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

    public static ClusterMessage getClusterMessage(String message) {
        String[] msg = message.split(Type.DELIMITER);
        if (msg.length != 2) {
            throw new IllegalArgumentException("Invalid message: " + message);
        }
        if (Type.ADD.value.equals(msg[0])) {
            return new ClusterMessage(Type.ADD, msg[1]);
        }
        if (Type.UPDATE.value.equals(msg[0])) {
            return new ClusterMessage(Type.ADD, msg[1]);
        }
        if (Type.DELETE.value.equals(msg[0])) {
            return new ClusterMessage(Type.ADD, msg[1]);
        }
        throw new IllegalArgumentException("Unknown message type: " + msg[0]);
    }

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
