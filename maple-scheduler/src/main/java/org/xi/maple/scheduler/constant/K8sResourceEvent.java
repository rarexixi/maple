package org.xi.maple.scheduler.constant;

public enum K8sResourceEvent {

    ADD("ADD"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    final String name;

    K8sResourceEvent(String labelName) {
        this.name = labelName;
    }

    public String getName() {
        return name;
    }
}
