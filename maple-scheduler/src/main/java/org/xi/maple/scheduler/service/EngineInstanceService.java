package org.xi.maple.scheduler.service;

import org.xi.maple.scheduler.model.EngineInstance;
import org.xi.maple.scheduler.model.Job;

import java.util.List;

/**
 * @author xishihao
 */
public interface EngineInstanceService {

    /**
     * 获取故障引擎列表
     *
     * @return 故障引擎列表
     */
    List<EngineInstance> getProblematicEngines();

    /**
     * 完成清理作业
     *
     * @param engineId 引擎ID
     * @return 更新结果
     */
    int finishCleaningJobs(Integer engineId);

    /**
     * 获取空闲引擎
     *
     * @param cluster        集群
     * @param queue          队列
     * @param engineCategory 引擎类型
     * @param engineVersion  引擎版本
     * @param group          分组
     * @return 引擎实例
     */
    EngineInstance getFreeEngine(String cluster, String queue, String engineCategory, String engineVersion, String group);

}
