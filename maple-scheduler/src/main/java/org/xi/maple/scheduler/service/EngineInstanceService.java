package org.xi.maple.scheduler.service;

import org.xi.maple.scheduler.model.EngineInstance;
import org.xi.maple.scheduler.model.Job;

import java.util.List;

/**
 * @author xishihao
 */
public interface EngineInstanceService {

    List<EngineInstance> getProblematicEngines();

    int finishCleaningJobs(Integer engineId);
}
