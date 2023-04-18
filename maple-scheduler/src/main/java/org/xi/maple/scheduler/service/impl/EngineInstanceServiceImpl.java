package org.xi.maple.scheduler.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xi.maple.scheduler.model.EngineInstance;
import org.xi.maple.scheduler.service.EngineInstanceService;

import java.util.List;

/**
 * @author xishihao
 */
@Service
public class EngineInstanceServiceImpl implements EngineInstanceService {

    private static final Logger logger = LoggerFactory.getLogger(EngineInstanceServiceImpl.class);

    @Override
    public List<EngineInstance> getProblematicEngines() {
        return null;
    }

    @Override
    public int finishCleaningJobs(Integer engineId) {
        return 0;
    }
}
