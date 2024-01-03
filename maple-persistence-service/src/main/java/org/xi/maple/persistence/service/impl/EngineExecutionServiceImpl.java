package org.xi.maple.persistence.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.exception.MapleDataInsertException;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.util.ObjectUtils;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueryRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionListItemResponse;
import org.xi.maple.persistence.persistence.condition.EngineExecutionSelectCondition;
import org.xi.maple.persistence.persistence.entity.EngineExecutionEntity;
import org.xi.maple.persistence.persistence.entity.EngineExecutionEntityExt;
import org.xi.maple.persistence.persistence.entity.EngineExecutionExtInfoEntity;
import org.xi.maple.persistence.persistence.mapper.EngineExecutionMapper;
import org.xi.maple.persistence.service.EngineExecutionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 引擎执行记录业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("engineExecutionService")
public class EngineExecutionServiceImpl implements EngineExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(EngineExecutionServiceImpl.class);

    final EngineExecutionMapper engineExecutionMapper;

    @Autowired
    public EngineExecutionServiceImpl(EngineExecutionMapper engineExecutionMapper) {
        this.engineExecutionMapper = engineExecutionMapper;
    }

    /**
     * 添加引擎执行记录
     *
     * @param addRequest 引擎执行记录
     * @return 引擎执行记录ID
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Transactional
    @Override
    public Integer add(EngineExecutionAddRequest addRequest) {
        EngineExecutionEntity entity = ObjectUtils.copy(addRequest, EngineExecutionEntity.class);
        int count = engineExecutionMapper.insert(entity);
        if (count > 0) {
            EngineExecutionExtInfoEntity extInfoEntity = ObjectUtils.copy(addRequest, EngineExecutionExtInfoEntity.class);
            extInfoEntity.setId(entity.getId());
            engineExecutionMapper.insertExt(extInfoEntity);
            return entity.getId();
        }
        throw new MapleDataInsertException("插入失败");
    }

    /**
     * 批量添加引擎执行记录
     *
     * @param list 引擎执行记录列表
     * @return 引擎执行记录ID列表
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Transactional
    @Override
    public List<Integer> batchAdd(Collection<EngineExecutionAddRequest> list) {
        if (list == null || list.size() == 0) {
            return new ArrayList<>(0);
        }
        List<EngineExecutionEntity> entityList = ObjectUtils.copy(list, EngineExecutionEntity.class);
        int count = engineExecutionMapper.batchInsert(entityList);
        if (count > 0) {
            List<Integer> result = new ArrayList<>(entityList.size());
            AtomicReference<Integer> i = new AtomicReference<>(0);
            List<EngineExecutionExtInfoEntity> entityExtList = ObjectUtils.copy(list, EngineExecutionExtInfoEntity.class, entity -> {
                int id = entityList.get(i.get()).getId();
                entity.setId(id);
                result.add(id);
                i.getAndSet(i.get() + 1);
            });
            engineExecutionMapper.batchInsertExt(entityExtList);
            return result;
        }
        throw new MapleDataInsertException("批量插入失败");
    }

    /**
     * 根据执行ID更新引擎执行状态
     *
     * @param updateRequest 更新引擎执行记录请求实体
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Transactional
    @CacheEvict(cacheNames = {"maple-execution"}, key = "#id")
    @Override
    public int updateStatusById(int id, EngineExecutionUpdateStatusRequest updateRequest) {
        EngineExecutionEntityExt entity = engineExecutionMapper.detailById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("引擎执行记录不存在");
        }
        EngineExecutionStatus oldStatus = EngineExecutionStatus.valueOf(entity.getStatus());
        if (oldStatus.isFinalStatus()) {
            logger.error("引擎执行已结束，id: {}", id);
            return 0;
        }

        EngineExecutionStatus newStatus = EngineExecutionStatus.valueOf(updateRequest.getStatus());
        switch (newStatus) {
            case CREATED:
                return 0;
            case ACCEPTED:
                if (!oldStatus.canAccept()) {
                    return 0;
                }
                break;
            case STARTING:
                if (!oldStatus.canStart()) {
                    return 0;
                }
                break;
            default:
                break;
        }
        return engineExecutionMapper.updateStatusById(id, updateRequest.getStatus());
    }

    /**
     * 根据执行ID更新引擎执行信息
     *
     * @param updateRequest 更新引擎执行记录请求实体
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Transactional
    @CacheEvict(cacheNames = {"maple-execution"}, key = "#id")
    @Override
    public int updateExtInfoById(int id, EngineExecutionUpdateRequest updateRequest) {
        EngineExecutionExtInfoEntity entity = ObjectUtils.copy(updateRequest, EngineExecutionExtInfoEntity.class);
        return engineExecutionMapper.updateExtInfoById(id, entity);
    }

    /**
     * 根据执行ID获取引擎执行记录详情
     *
     * @param id 执行ID
     * @return 引擎执行记录详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Cacheable(cacheNames = {"maple-execution"}, key = "#id")
    @Override
    public EngineExecutionDetailResponse getById(Integer id) {
        EngineExecutionEntityExt entity = engineExecutionMapper.detailById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("引擎执行记录不存在, id: " + id);
        }
        return ObjectUtils.copy(entity, EngineExecutionDetailResponse.class);
    }

    /**
     * 分页获取引擎执行记录列表
     *
     * @param queryRequest 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的引擎执行记录分页列表
     */
    @Override
    public PageList<EngineExecutionListItemResponse> getPageList(EngineExecutionQueryRequest queryRequest, Integer pageNum, Integer pageSize) {

        EngineExecutionSelectCondition condition = ObjectUtils.copy(queryRequest, EngineExecutionSelectCondition.class);
        try (Page<Object> page = PageHelper.startPage(pageNum, pageSize)) {
            PageInfo<EngineExecutionEntityExt> pageInfo = page.doSelectPageInfo(() -> engineExecutionMapper.select(condition));
            List<EngineExecutionListItemResponse> list = ObjectUtils.copy(pageInfo.getList(), EngineExecutionListItemResponse.class);
            return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
        }
    }
}
