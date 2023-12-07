package org.xi.maple.scheduler.constant;

public enum K8sResourceType {

    POD("pod"),
    QUEUE("queue"),
    SPARK("spark"),
    FLINK("flink");

    final String name;

    K8sResourceType(String name) {
        this.name = name;
    }

    public boolean is(String name) {
        return this.name.equals(name);
    }

    public String getName() {
        return name;
    }
}
