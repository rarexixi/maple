<#include "/include/table/properties.ftl">
package ${modulePackage}.service.impl;

import ${commonPackage}.constant.DeletedConstant;
import ${commonPackage}.exception.MapleDataNotFoundException;
import ${commonPackage}.model.PageList;
import ${commonPackage}.util.ObjectUtils;
import ${modulePackage}.persistence.condition.${className}SelectCondition;
import ${modulePackage}.persistence.condition.${className}UpdateCondition;
import ${modulePackage}.persistence.entity.${className}Entity;
import ${modulePackage}.persistence.entity.${className}EntityExt;
import ${modulePackage}.persistence.mapper.${className}Mapper;
import ${modulePackage}.model.request.${className}AddRequest;
import ${modulePackage}.model.request.${className}PatchRequest;
import ${modulePackage}.model.request.${className}QueryRequest;
import ${modulePackage}.model.request.${className}SaveRequest;
import ${modulePackage}.model.response.${className}DetailResponse;
import ${modulePackage}.model.response.${className}ListItemResponse;
import ${modulePackage}.service.${className}Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * ${tableComment}业务逻辑
 *
 * @author ${author}
 */
@Service("${classNameFirstLower}Service")
public class ${className}ServiceImpl implements ${className}Service {

    final ${className}Mapper ${classNameFirstLower}Mapper;

    @Autowired
    public ${className}ServiceImpl(${className}Mapper ${classNameFirstLower}Mapper) {
        this.${classNameFirstLower}Mapper = ${classNameFirstLower}Mapper;
    }

    /**
     * 添加${tableComment}
     *
     * @param addRequest ${tableComment}
     * @return 受影响的行数
     * @author ${author}
     */
    @Override
    @Transactional
    public ${className}DetailResponse add(${className}AddRequest addRequest) {
        ${className}Entity entity = ObjectUtils.copy(addRequest, ${className}Entity.class);
        ${classNameFirstLower}Mapper.insert(entity);
        return getBy<#include "/include/table/pk_fun_names.ftl">(<#list pks as column><#include "/include/column/properties.ftl">entity.get${propertyName}()<#if (column?has_next)>, </#if></#list>);
    }

    /**
     * 批量添加${tableComment}
     *
     * @param list ${tableComment}列表
     * @return 受影响的行数
     * @author ${author}
     */
    @Override
    @Transactional
    public int batchAdd(Collection<${className}AddRequest> list) {
        List<${className}Entity> entityList = ObjectUtils.copy(list, ${className}Entity.class);
        return ${classNameFirstLower}Mapper.batchInsert(entityList);
    }

    <#-- region 删除/启用/禁用 -->
    /**
     * 删除${tableComment}
     *
     * @param patchRequest 删除条件请求
     * @return 受影响的行数
     * @author ${author}
     */
    @Override
    @Transactional
    public int delete(${className}PatchRequest patchRequest) {
        <#if (table.hasUniPk)>
        <#else>
            ${className}UpdateCondition condition = ObjectUtils.copy(patchRequest, ${className}UpdateCondition.class);
        </#if>
        return ${classNameFirstLower}Mapper.deleteBy<#if hasUniId>Id<#elseif (table.hasUniPk)>Pk<#else>Condition</#if>(<#if (table.hasUniPk)>patchRequest.get${uniPkPropertyName}()<#else>condition</#if>);
    }
    <#if table.validStatusColumn??>

    /**
     * 禁用${tableComment}
     *
     * @param patchRequest 禁用条件请求
     * @return 受影响的行数
     * @author ${author}
     */
    @Override
    @Transactional
    public int disable(${className}PatchRequest patchRequest) {
        <#if (table.hasUniPk)>
        <#else>
        ${className}UpdateCondition condition = ObjectUtils.copy(patchRequest, ${className}UpdateCondition.class);
        </#if>
        ${className}Entity entity = ObjectUtils.copy(patchRequest, ${className}Entity.class<#list pks as column><#include "/include/column/properties.ftl">, "${fieldName}"</#list>);
        entity.setDeleted(DeletedConstant.INVALID);
        return ${classNameFirstLower}Mapper.updateBy<#if hasUniId>Id<#elseif (table.hasUniPk)>Pk<#else>Condition</#if>(entity, <#if (table.hasUniPk)>patchRequest.get${uniPkPropertyName}()<#else>condition</#if>);
    }

    /**
     * 启用${tableComment}
     *
     * @param patchRequest 启用条件请求
     * @return 受影响的行数
     * @author ${author}
     */
    @Override
    @Transactional
    public int enable(${className}PatchRequest patchRequest) {
        <#if (table.hasUniPk)>
        <#else>
        ${className}UpdateCondition condition = ObjectUtils.copy(patchRequest, ${className}UpdateCondition.class);
        </#if>
        ${className}Entity entity = ObjectUtils.copy(patchRequest, ${className}Entity.class<#list pks as column><#include "/include/column/properties.ftl">, "${fieldName}"</#list>);
        entity.setDeleted(DeletedConstant.VALID);
        return ${classNameFirstLower}Mapper.updateBy<#if hasUniId>Id<#elseif (table.hasUniPk)>Pk<#else>Condition</#if>(entity, <#if (table.hasUniPk)>patchRequest.get${uniPkPropertyName}()<#else>condition</#if>);
    }
    </#if>
    <#-- endregion 删除/启用/禁用 -->

    <#-- region 更新 -->

    /**
     * 根据<#include "/include/table/pk_fun_comment.ftl">更新${tableComment}
     *
     * @param saveRequest 保存${tableComment}请求实体
     <#list pks as column>
     <#include "/include/column/properties.ftl">
     * @param ${fieldName} ${columnFullComment}
     </#list>
     * @return 更新后的${tableComment}详情
     * @author ${author}
     */
    @Override
    @Transactional
    public ${className}DetailResponse updateBy<#include "/include/table/pk_fun_names.ftl">(${className}SaveRequest saveRequest, <#include "/include/table/pk_params.ftl">) {
        <#if (table.hasUniPk)>
        <#else>
        ${className}UpdateCondition condition = new ${className}UpdateCondition();
        <#list pks as column>
        <#include "/include/column/properties.ftl">
        condition.set${propertyName}(${fieldName});
        </#list>
        </#if>
        ${className}Entity entity = ObjectUtils.copy(saveRequest, ${className}Entity.class);
        ${classNameFirstLower}Mapper.updateBy<#if hasUniId>Id<#elseif (table.hasUniPk)>Pk<#else>Condition</#if>(entity, <#if (table.hasUniPk)><#include "/include/table/pk_values.ftl"><#else>condition</#if>);
        ${className}DetailResponse result;
        if (<#list pks as column><#include "/include/column/properties.ftl">saveRequest.get${propertyName}() == null<#if (column?has_next)> || </#if></#list>) {
            result = getBy<#include "/include/table/pk_fun_names.ftl">(<#include "/include/table/pk_values.ftl">);
        } else {
            result = getBy<#include "/include/table/pk_fun_names.ftl">(<#list pks as column><#include "/include/column/properties.ftl">saveRequest.get${propertyName}()<#if (column?has_next)>, </#if></#list>);
        }
        return result;
    }
    <#-- endregion 更新 -->

    <#-- region 详情 -->

    /**
     * 根据<#include "/include/table/pk_fun_comment.ftl">获取${tableComment}详情
     *
     <#list pks as column>
     <#include "/include/column/properties.ftl">
     * @param ${fieldName} ${columnFullComment}
     </#list>
     * @return ${tableComment}详情
     * @author ${author}
     */
    @Override
    @Transactional(readOnly = true)
    public ${className}DetailResponse getBy<#include "/include/table/pk_fun_names.ftl">(<#include "/include/table/pk_params.ftl">) {
        ${className}EntityExt entity = ${classNameFirstLower}Mapper.detailBy<#if hasUniId>Id<#elseif (table.hasUniPk)>Pk<#else></#if>(<#include "/include/table/pk_values.ftl">);
        if (entity == null) {
            throw new MapleDataNotFoundException("${tableComment}不存在");
        }
        return ObjectUtils.copy(entity, ${className}DetailResponse.class);
    }
    <#-- endregion 详情 -->

    /**
     * 获取${tableComment}列表
     *
     * @param queryRequest 搜索条件
     * @return 符合条件的${tableComment}列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<${className}ListItemResponse> getList(${className}QueryRequest queryRequest) {
        ${className}SelectCondition condition = ObjectUtils.copy(queryRequest, ${className}SelectCondition.class);
        List<${className}Entity> list = ${classNameFirstLower}Mapper.select(condition);
        return ObjectUtils.copy(list, ${className}ListItemResponse.class);
    }

    /**
     * 分页获取${tableComment}列表
     *
     * @param queryRequest 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的${tableComment}分页列表
     */
    @Override
    @Transactional(readOnly = true)
    public PageList<${className}ListItemResponse> getPageList(${className}QueryRequest queryRequest, Integer pageNum, Integer pageSize) {

        ${className}SelectCondition condition = ObjectUtils.copy(queryRequest, ${className}SelectCondition.class);
        PageInfo<${className}EntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> ${classNameFirstLower}Mapper.select(condition));

        List<${className}ListItemResponse> list = ObjectUtils.copy(pageInfo.getList(), ${className}ListItemResponse.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }
}
