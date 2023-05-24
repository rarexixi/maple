package org.xi.maple.scheduler.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xi.maple.common.mapper.UpdateByIdMapper;
import org.xi.maple.scheduler.persistence.entity.EngineInstanceEntity;

/**
 * @author xishihao
 */
@Mapper
public interface EngineInstanceMapper extends UpdateByIdMapper<EngineInstanceEntity> {

    /**
     * 获取空闲引擎
     *
     * @param cluster        集群
     * @param clusterQueue   队列
     * @param engineCategory 引擎类型
     * @param engineVersion  引擎版本
     * @param group          用户组
     * @return 引擎实例信息
     */
    EngineInstanceEntity getFreeEngine(@Param("cluster") String cluster,
                                       @Param("clusterQueue") String clusterQueue,
                                       @Param("engineCategory") String engineCategory,
                                       @Param("engineVersion") String engineVersion,
                                       @Param("group") String group);
}