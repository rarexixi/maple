package org.xi.maple.rest.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.xi.maple.rest.service.ClusterService;
import org.xi.maple.redis.model.ClusterMessage;

@Service
public class ClusterServiceImpl implements ClusterService {

    private static final Logger logger = LoggerFactory.getLogger(ClusterServiceImpl.class);

    final RedisTemplate<String, Object> redisTemplate;

    public ClusterServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public int refresh(String clusterName) {
        sendRefreshClusterMsg(ClusterMessage.Type.UPDATE, clusterName);
        return 0;
    }

    private void sendRefreshClusterMsg(ClusterMessage.Type type, String clusterName) {
        try {
            redisTemplate.convertAndSend(ClusterMessage.CLUSTER_CHANNEL, new ClusterMessage(type, clusterName));
        } catch (Throwable t) {
            logger.error("发送集群刷新消息失败, cluster: {}, type: {}", clusterName, type, t);
        }
    }
}
