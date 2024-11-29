<#include "/include/table/properties.ftl">
package ${modulePackage}.service;

import ${commonPackage}.model.PageList;
import ${commonPackage}.model.BaseEntity;
import ${modulePackage}.model.request.${className}QueryReq;
import ${modulePackage}.model.request.${className}SaveReq;
import ${modulePackage}.model.response.${className}DetailResp;
import ${modulePackage}.model.response.${className}ItemResp;

import java.util.List;

/**
 * ${tableComment}业务逻辑
 *
 * @author ${author}
 */
public interface ${className}Service {

    /**
     * 添加${tableComment}
     *
     * @param createReq ${tableComment}
     * @return 受影响的行数
     * @author ${author}
     */
    ${className}DetailResp create(${className}SaveReq createReq);

    /**
     * 批量添加${tableComment}
     *
     * @param list ${tableComment}列表
     * @return 受影响的行数
     * @author ${author}
     */
    int batchCreate(List<${className}SaveReq> list);

    // region 删除<#if hasValidStatusColumn>/启用/禁用</#if>

    /**
     * 删除${tableComment}
     *
     <@pkTrav "batch_pk_params_comment" true "     "/>
     * @param entity
     * @return 受影响的行数
     * @author ${author}
     */
    int deleteBy<@pkTrav "pk_fun_names" />(<@pkTrav "batch_pk_params" true /> BaseEntity entity);
    <#if hasValidStatusColumn>

    /**
     * 禁用${tableComment}
     *
     <@pkTrav "batch_pk_params_comment" true "     "/>
     * @param entity
     * @return 受影响的行数
     * @author ${author}
     */
    int disableBy<@pkTrav "pk_fun_names" />(<@pkTrav "batch_pk_params" true /> BaseEntity entity);

    /**
     * 启用${tableComment}
     *
     <@pkTrav "batch_pk_params_comment" true "     "/>
     * @param entity
     * @return 受影响的行数
     * @author ${author}
     */
    int enableBy<@pkTrav "pk_fun_names" />(<@pkTrav "batch_pk_params" true /> BaseEntity entity);
    </#if>

    // endregion 删除<#if hasValidStatusColumn>/启用/禁用</#if>

    // region 更新

    /**
     * 根据<@pkTrav "pk_fun_comments" />更新${tableComment}非空字段
     *
     <@pkTrav "pk_params_comment" true "     "/>
     * @param saveReq 保存${tableComment}请求实体
     * @return 更新后的${tableComment}详情
     * @author ${author}
     */
    ${className}DetailResp patchBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_params" true /> ${className}SaveReq saveReq);

    /**
     * 根据<@pkTrav "pk_fun_comments" />更新${tableComment}所有字段
     *
     <@pkTrav "pk_params_comment" true "     "/>
     * @param saveReq 保存${tableComment}请求实体
     * @return 更新后的${tableComment}详情
     * @author ${author}
     */
    ${className}DetailResp updateBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_params" true /> ${className}SaveReq saveReq);

    // endregion 更新

    // region 详情

    /**
     * 根据<@pkTrav "pk_fun_comments" />获取${tableComment}详情
     *
     <@pkTrav "pk_params_comment" true "     "/>
     * @return ${tableComment}详情
     * @author ${author}
     */
    ${className}DetailResp getBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_params" false />);

    // endregion 详情

    /**
     * 获取${tableComment}列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的${tableComment}列表
     */
    List<${className}ItemResp> getList(${className}QueryReq queryReq);

    /**
     * 分页获取${tableComment}列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的${tableComment}分页列表
     */
    PageList<${className}ItemResp> getPageList(${className}QueryReq queryReq, Integer pageNum, Integer pageSize);
}
