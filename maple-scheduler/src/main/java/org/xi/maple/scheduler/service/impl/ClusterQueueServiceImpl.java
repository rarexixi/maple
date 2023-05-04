package org.xi.maple.scheduler.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.xi.maple.redis.model.MapleClusterQueue;
import org.xi.maple.scheduler.service.ClusterQueueService;

import java.util.Map;

/**
 * @author xishihao
 */
@Service
public class ClusterQueueServiceImpl implements ClusterQueueService {

    final RedisTemplate<String, Object> redisTemplate;

    @Value("${cluster.queue.key}")
    private String queueKey;

    public ClusterQueueServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void cacheQueueInfos(Map<String, MapleClusterQueue> queues) {
        redisTemplate.opsForHash().putAll(queueKey, queues);
    }

    @Override
    public void cacheQueueInfo(String clusterName, String queue, MapleClusterQueue queueInfo) {
        String key = MapleClusterQueue.getKey(clusterName, queue);
        redisTemplate.opsForHash().put(queueKey, key, queueInfo);
    }

    @Override
    public MapleClusterQueue getCachedQueueInfo(String clusterName, String queue) {
        String key = MapleClusterQueue.getKey(clusterName, queue);
        HashOperations<String, String, MapleClusterQueue> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(queueKey, key);
    }
}
