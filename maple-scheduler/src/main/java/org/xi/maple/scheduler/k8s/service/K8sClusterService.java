package org.xi.maple.scheduler.k8s.service;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.StatusDetails;
import org.springframework.web.multipart.MultipartFile;
import org.xi.maple.scheduler.service.ClusterService;

import java.util.List;

/**
 * @author xishihao
 */
public interface K8sClusterService extends ClusterService {

    /**
     * 部署引擎到K8s集群
     *
     * @param clusterName 集群名称
     * @param yamlFile    yaml文件
     * @return 部署结果
     */
    List<HasMetadata> deployEngine(String clusterName, MultipartFile yamlFile);

    /**
     * 删除引擎
     *
     * @param clusterName 集群名称
     * @param yamlFile    yaml文件
     * @return 删除结果
     */
    List<StatusDetails> deleteEngine(String clusterName, MultipartFile yamlFile);

    /**
     * 部署引擎到K8s集群
     *
     * @param clusterName 集群名称
     * @param yaml        yaml 内容
     * @return 部署结果
     */
    List<HasMetadata> deployEngine(String clusterName, String yaml);

    /**
     * 删除引擎
     *
     * @param clusterName 集群名称
     * @param yaml        yaml 内容
     * @return 删除结果
     */
    List<StatusDetails> deleteEngine(String clusterName, String yaml);

    /**
     * 删除引擎
     *
     * @param clusterName 集群名称
     * @param namespace   命名空间
     * @param type        引擎类型
     * @param name        引擎名称
     * @return 删除结果
     */
    List<StatusDetails> deleteEngine(String clusterName, String namespace, String type, String name);
}
