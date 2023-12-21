package org.xi.maple.datacalc.api.service;

public interface ClusterService {

    int refresh();

    int refresh(String clusterName);
}
