package org.xi.maple.persistence.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.persistence.model.response.JobDetailResp;
import org.xi.maple.persistence.persistence.entity.JobEntity;
import org.xi.maple.persistence.persistence.mapper.JobMapper;
import org.xi.maple.persistence.service.JobService;
import org.xi.maple.service.util.ObjectUtils;

/**
 * 执行作业业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("jobService")
public class JobServiceImpl implements JobService {

    final JobMapper jobMapper;

    @Autowired
    public JobServiceImpl(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }


    // region 详情

    /**
     * 根据获取执行作业详情
     *
     * @param id 作业ID
     * @return 执行作业详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public JobDetailResp getById(Integer id) {
        JobEntity entity = jobMapper.getById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("执行作业不存在");
        }
        return ObjectUtils.copy(entity, JobDetailResp.class);
    }

    // endregion 详情
}
