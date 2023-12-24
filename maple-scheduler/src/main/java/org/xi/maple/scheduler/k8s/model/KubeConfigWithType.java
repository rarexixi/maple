package org.xi.maple.scheduler.k8s.model;

import io.fabric8.kubernetes.client.Config;
import lombok.Data;

@Data
public class KubeConfigWithType {
    private String type;
    private String kubeConfigFile;
    private String kubeConfigContent;
    private Config config;
}