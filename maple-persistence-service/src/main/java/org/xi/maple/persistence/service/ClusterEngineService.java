package org.xi.maple.persistence.service;

import org.xi.maple.common.model.EngineConf;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfGetRequest;
import org.xi.maple.persistence.model.request.ClusterEngineQueryReq;
import org.xi.maple.persistence.model.request.ClusterEngineSaveReq;
import org.xi.maple.persistence.model.response.ClusterEngineDetailResp;
import org.xi.maple.persistence.model.response.ClusterEngineItemResp;

import java.util.List;

/**
 * 集群引擎业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface ClusterEngineService {

    /**
     * 添加集群引擎
     *
     * @param createReq 集群引擎
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterEngineDetailResp create(ClusterEngineSaveReq createReq);

    /**
     * 批量添加集群引擎
     *
     * @param list 集群引擎列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchCreate(List<ClusterEngineSaveReq> list);

    /**
     * 删除集群引擎
     *
     * @param id     引擎ID
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int deleteById(Integer id, BaseEntity entity);

    /**
     * 根据引擎ID更新集群引擎
     *
     * @param id      引擎ID
     * @param saveReq 保存集群引擎请求实体
     * @return 更新后的集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterEngineDetailResp patchById(Integer id, ClusterEngineSaveReq saveReq);

    /**
     * 根据引擎ID更新集群引擎
     *
     * @param id      引擎ID
     * @param saveReq 保存集群引擎请求实体
     * @return 更新后的集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterEngineDetailResp updateById(Integer id, ClusterEngineSaveReq saveReq);

    /**
     * 根据引擎ID获取集群引擎详情
     *
     * @param id 引擎ID
     * @return 集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterEngineDetailResp getById(Integer id);

    /**
     * 根据引擎ID获取集群引擎详情
     *
     * @param getRequest 查询请求
     * @return 集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    EngineConf getEngineConf(ClusterEngineDefaultConfGetRequest getRequest);

    /**
     * 获取集群引擎列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的集群引擎列表
     */
    List<ClusterEngineItemResp> getList(ClusterEngineQueryReq queryReq);

    /**
     * 分页获取集群引擎列表
     *
     * @param queryReq 搜索条件
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 符合条件的集群引擎分页列表
     */
    PageList<ClusterEngineItemResp> getPageList(ClusterEngineQueryReq queryReq, Integer pageNum, Integer pageSize);
}
