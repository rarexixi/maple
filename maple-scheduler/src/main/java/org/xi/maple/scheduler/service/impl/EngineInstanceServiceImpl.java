package org.xi.maple.scheduler.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xi.maple.scheduler.model.EngineInstance;
import org.xi.maple.scheduler.service.EngineInstanceService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xishihao
 */
@Service
public class EngineInstanceServiceImpl implements EngineInstanceService {

    private static final Logger logger = LoggerFactory.getLogger(EngineInstanceServiceImpl.class);

    /**
     * 获取故障引擎列表
     *
     * @return 故障引擎列表
     */
    @Override
    public List<EngineInstance> getProblematicEngines() {
        return new ArrayList<>();
    }

    /**
     * 完成清理作业
     *
     * @param engineId 引擎ID
     * @return
     */
    @Override
    public int finishCleaningJobs(Integer engineId) {
        return 0;
    }

    @Override
    public EngineInstance getFreeEngine(String cluster, String queue, String engineCategory, String engineVersion, String group) {
        return null;
    }
}
