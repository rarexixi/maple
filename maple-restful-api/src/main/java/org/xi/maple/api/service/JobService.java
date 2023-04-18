package org.xi.maple.api.service;

import org.xi.maple.api.model.request.SubmitJobRequest;

/**
 * @author xishihao
 */
public interface JobService {

    Integer submitJob(SubmitJobRequest jobReq);
}
