package org.xi.maple.mp.service;

import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.mp.model.request.ClusterEngineQueryReq;
import org.xi.maple.mp.model.request.ClusterEngineSaveReq;
import org.xi.maple.mp.model.response.ClusterEngineDetailResp;
import org.xi.maple.mp.model.response.ClusterEngineItemResp;

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

    // region 删除

    /**
     * 删除集群引擎
     *
     * @param idList 引擎ID列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int deleteById(List<Integer> idList, BaseEntity entity);

    // endregion 删除

    // region 更新

    /**
     * 根据更新集群引擎非空字段
     *
     * @param id 引擎ID
     * @param saveReq 保存集群引擎请求实体
     * @return 更新后的集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterEngineDetailResp patchById(Integer id, ClusterEngineSaveReq saveReq);

    /**
     * 根据更新集群引擎所有字段
     *
     * @param id 引擎ID
     * @param saveReq 保存集群引擎请求实体
     * @return 更新后的集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterEngineDetailResp updateById(Integer id, ClusterEngineSaveReq saveReq);

    // endregion 更新

    // region 详情

    /**
     * 根据获取集群引擎详情
     *
     * @param id 引擎ID
     * @return 集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterEngineDetailResp getById(Integer id);

    // endregion 详情

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
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的集群引擎分页列表
     */
    PageList<ClusterEngineItemResp> getPageList(ClusterEngineQueryReq queryReq, Integer pageNum, Integer pageSize);
}
