package org.xi.maple.manager.k8s;

import io.fabric8.kubernetes.client.informers.ResourceEventHandler;

public interface MapleResourceEventHandler<T> extends ResourceEventHandler<T> {

    default void onAdd(T obj) {
    }

    default void onUpdate(T oldObj, T newObj) {
    }

    default void onDelete(T obj, boolean deletedFinalStateUnknown) {
    }
}
