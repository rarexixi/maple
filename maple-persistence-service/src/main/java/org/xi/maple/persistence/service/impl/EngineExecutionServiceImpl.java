package org.xi.maple.persistence.service.impl;

import org.xi.maple.common.exception.MapleDataInsertException;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.PageList;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateProcessRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.persistence.entity.EngineExecutionExtInfoEntity;
import org.xi.maple.persistence.utils.ObjectUtils;
import org.xi.maple.persistence.persistence.condition.EngineExecutionSelectCondition;
import org.xi.maple.persistence.persistence.entity.EngineExecutionEntity;
import org.xi.maple.persistence.persistence.entity.EngineExecutionEntityExt;
import org.xi.maple.persistence.persistence.mapper.EngineExecutionMapper;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueryRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionListItemResponse;
import org.xi.maple.persistence.service.EngineExecutionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 根据执行ID更新心跳时间
     *
     * @param id 引擎执行记录ID
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public int heartbeatById(Integer id) {
        return engineExecutionMapper.heartbeatById(id);
    }

    /**
     * 根据执行ID更新引擎执行状态
     *
     * @param updateRequest 更新引擎执行记录请求实体
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public int updateStatusById(EngineExecutionUpdateStatusRequest updateRequest) {
        return engineExecutionMapper.updateStatusById(updateRequest.getId(), updateRequest.getStatus());
    }

    /**
     * 根据执行ID更新引擎执行信息
     *
     * @param updateRequest 更新引擎执行记录请求实体
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public int updateProcessById(EngineExecutionUpdateProcessRequest updateRequest) {
        return engineExecutionMapper.updateProcessById(updateRequest.getId(), updateRequest.getProcessInfo());
    }

    /**
     * 根据执行ID获取引擎执行记录详情
     *
     * @param id 执行ID
     * @return 引擎执行记录详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public EngineExecutionDetailResponse getById(Integer id) {
        EngineExecutionEntityExt entity = engineExecutionMapper.detailById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("引擎执行记录不存在");
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
        PageInfo<EngineExecutionEntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> engineExecutionMapper.select(condition));

        List<EngineExecutionListItemResponse> list = ObjectUtils.copy(pageInfo.getList(), EngineExecutionListItemResponse.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }
}
