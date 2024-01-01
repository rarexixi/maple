package org.xi.maple.persistence.service;

import org.xi.maple.common.model.EngineConf;
import org.xi.maple.common.model.PageList;
import org.xi.maple.persistence.model.request.*;
import org.xi.maple.persistence.model.response.ClusterEngineDetailResponse;
import org.xi.maple.persistence.model.response.ClusterEngineListItemResponse;

import java.util.Collection;
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
     * @param addRequest 集群引擎
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterEngineDetailResponse add(ClusterEngineAddRequest addRequest);

    /**
     * 批量添加集群引擎
     *
     * @param list 集群引擎列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchAdd(Collection<ClusterEngineAddRequest> list);

    /**
     * 删除集群引擎
     *
     * @param patchRequest 删除条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int delete(ClusterEnginePatchRequest patchRequest);

    /**
     * 根据引擎ID更新集群引擎
     *
     * @param saveRequest 保存集群引擎请求实体
     * @return 更新后的集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterEngineDetailResponse updateById(ClusterEngineSaveRequest saveRequest);

    /**
     * 根据引擎ID获取集群引擎详情
     *
     * @param id 引擎ID
     * @return 集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterEngineDetailResponse getById(Integer id);

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
     * @param queryRequest 搜索条件
     * @return 符合条件的集群引擎列表
     */
    List<ClusterEngineListItemResponse> getList(ClusterEngineQueryRequest queryRequest);

    /**
     * 分页获取集群引擎列表
     *
     * @param queryRequest 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的集群引擎分页列表
     */
    PageList<ClusterEngineListItemResponse> getPageList(ClusterEngineQueryRequest queryRequest, Integer pageNum, Integer pageSize);
}
