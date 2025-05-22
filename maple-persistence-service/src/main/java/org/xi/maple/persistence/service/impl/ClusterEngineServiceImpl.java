package org.xi.maple.persistence.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.EngineConf;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfGetRequest;
import org.xi.maple.persistence.persistence.entity.ClusterEngineEntity;
import org.xi.maple.persistence.persistence.mapper.ClusterEngineMapper;
import org.xi.maple.persistence.service.ClusterEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 集群引擎业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("clusterEngineService")
public class ClusterEngineServiceImpl implements ClusterEngineService {

    final ClusterEngineMapper clusterEngineMapper;

    @Autowired
    public ClusterEngineServiceImpl(ClusterEngineMapper clusterEngineMapper) {
        this.clusterEngineMapper = clusterEngineMapper;
    }

    @Override
    public EngineConf getEngineConf(Integer id, ClusterEngineDefaultConfGetRequest getRequest) {
        ClusterEngineEntity entity = clusterEngineMapper.getById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("集群引擎不存在");
        }
        EngineConf engineConf = JsonUtils.parseObject(entity.getEngineConf(), EngineConf.class, new EngineConf());
        engineConf.setEngineHome(entity.getEngineHome());
        engineConf.setVersion(entity.getVersion());

        String groupDefaultConf = clusterEngineMapper.getByTypeAndName(id, "group", getRequest.getUserGroup());
        if (StringUtils.isNotBlank(groupDefaultConf)) {
            EngineConf groupConf = JsonUtils.parseObject(groupDefaultConf, EngineConf.class, new EngineConf());
            engineConf.getConfs().putAll(groupConf.getConfs());
            engineConf.getEnvs().putAll(groupConf.getEnvs());
        }
        String userDefaultConf = clusterEngineMapper.getByTypeAndName(id, "user", getRequest.getUser());
        if (StringUtils.isNotBlank(userDefaultConf)) {
            EngineConf userConf = JsonUtils.parseObject(userDefaultConf, EngineConf.class, new EngineConf());
            engineConf.getConfs().putAll(userConf.getConfs());
            engineConf.getEnvs().putAll(userConf.getEnvs());
        }

        return engineConf;
    }
}
