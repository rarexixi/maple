package org.xi.maple.mp.service.impl;

import org.xi.maple.common.constant.ValidConstant;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.mp.persistence.condition.JobFilterCondition;
import org.xi.maple.mp.persistence.condition.JobPkCondition;
import org.xi.maple.mp.persistence.entity.JobEntity;
import org.xi.maple.mp.persistence.entity.JobEntityExt;
import org.xi.maple.mp.persistence.mapper.JobMapper;
import org.xi.maple.mp.model.request.JobQueryReq;
import org.xi.maple.mp.model.request.JobSaveReq;
import org.xi.maple.mp.model.response.JobDetailResp;
import org.xi.maple.mp.model.response.JobItemResp;
import org.xi.maple.mp.service.JobService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 添加执行作业
     *
     * @param createReq 执行作业
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public JobDetailResp create(JobSaveReq createReq) {
        JobEntity entity = ObjectUtils.copy(createReq, JobEntity.class);
        jobMapper.insert(entity);
        return getById(entity.getId());
    }

    /**
     * 批量添加执行作业
     *
     * @param list 执行作业列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int batchCreate(List<JobSaveReq> list) {
        List<JobEntity> entityList = ObjectUtils.copy(list, JobEntity.class);
        return jobMapper.batchInsert(entityList);
    }

    // region 删除/启用/禁用

    /**
     * 删除执行作业
     *
     * @param idList 作业ID列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int deleteById(List<Integer> idList, BaseEntity baseEntity) {
        JobPkCondition condition = getPkCondition(idList);
        return jobMapper.deleteByCondition(condition);
    }

    /**
     * 禁用执行作业
     *
     * @param idList 作业ID列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int disableById(List<Integer> idList, BaseEntity baseEntity) {
        JobPkCondition condition = getPkCondition(idList);
        JobEntity entity = ObjectUtils.copy(baseEntity, JobEntity.class);
        entity.setDisabled(ValidConstant.INVALID);
        return jobMapper.patchByCondition(condition, entity);
    }

    /**
     * 启用执行作业
     *
     * @param idList 作业ID列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int enableById(List<Integer> idList, BaseEntity baseEntity) {
        JobPkCondition condition = getPkCondition(idList);
        JobEntity entity = ObjectUtils.copy(baseEntity, JobEntity.class);
        entity.setDisabled(ValidConstant.VALID);
        return jobMapper.patchByCondition(condition, entity);
    }

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新执行作业非空字段
     *
     * @param id 作业ID
     * @param saveReq 保存执行作业请求实体
     * @return 更新后的执行作业详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public JobDetailResp patchById(Integer id, JobSaveReq saveReq) {
        JobPkCondition condition = getPkCondition(id);
        JobEntity entity = ObjectUtils.copy(saveReq, JobEntity.class);
        jobMapper.patchByCondition(condition, entity);
        return getById(id);
    }

    /**
     * 根据更新执行作业所有字段
     *
     * @param id 作业ID
     * @param saveReq 保存执行作业请求实体
     * @return 更新后的执行作业详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public JobDetailResp updateById(Integer id, JobSaveReq saveReq) {
        JobEntity entity = ObjectUtils.copy(saveReq, JobEntity.class);
        jobMapper.updateById(id, entity);
        return getById(id);
    }

    // endregion 更新

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
        JobEntityExt entity = jobMapper.getById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("执行作业不存在");
        }
        return ObjectUtils.copy(entity, JobDetailResp.class);
    }

    // endregion 详情

    /**
     * 获取执行作业列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的执行作业列表
     */
    @Override
    public List<JobItemResp> getList(JobQueryReq queryReq) {
        JobFilterCondition condition = ObjectUtils.copy(queryReq, JobFilterCondition.class);
        List<JobEntity> list = jobMapper.select(condition, null, queryReq.getSort());
        return ObjectUtils.copy(list, JobItemResp.class);
    }

    /**
     * 分页获取执行作业列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的执行作业分页列表
     */
    @Override
    public PageList<JobItemResp> getPageList(JobQueryReq queryReq, Integer pageNum, Integer pageSize) {

        JobFilterCondition condition = ObjectUtils.copy(queryReq, JobFilterCondition.class);
        ISelect select = () -> jobMapper.select(condition, null, queryReq.getSort());
        PageInfo<JobEntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(select);

        List<JobItemResp> list = ObjectUtils.copy(pageInfo.getList(), JobItemResp.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }

    private JobPkCondition getPkCondition(Integer id) {
        JobPkCondition condition = new JobPkCondition();
        condition.setId(id);
        return condition;
    }

    private JobPkCondition getPkCondition(List<Integer> idList) {
        JobPkCondition condition = new JobPkCondition();
        if (idList.isEmpty()) {
            return null;
        } else if (idList.size() == 1) {
            condition.setId(idList.get(0));
        } else {
            condition.setIdIn(idList);
        }
        return condition;
    }
}
