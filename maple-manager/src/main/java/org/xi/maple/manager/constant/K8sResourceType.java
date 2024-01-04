package org.xi.maple.manager.constant;

import org.xi.maple.common.constant.EngineCategoryConstants;

public enum K8sResourceType {

    POD("pod"),
    QUEUE("queue"),
    SPARK(EngineCategoryConstants.SPARK),
    FLINK(EngineCategoryConstants.FLINK);

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
