<#include "/include/table/properties.ftl">
package ${modulePackage}.service.impl;

import ${commonPackage}.constant.ValidConstant;
import ${commonPackage}.exception.MapleDataNotFoundException;
import ${commonPackage}.model.PageList;
import ${commonPackage}.model.BaseEntity;
import ${commonStarterPackage}.util.ObjectUtils;
import ${modulePackage}.persistence.condition.${className}FilterCondition;
import ${modulePackage}.persistence.condition.${className}PkCondition;
import ${modulePackage}.persistence.entity.${className}Entity;
import ${modulePackage}.persistence.entity.${className}EntityExt;
import ${modulePackage}.persistence.mapper.${className}Mapper;
import ${modulePackage}.model.request.${className}QueryReq;
import ${modulePackage}.model.request.${className}SaveReq;
import ${modulePackage}.model.response.${className}DetailResp;
import ${modulePackage}.model.response.${className}ItemResp;
import ${modulePackage}.service.${className}Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param createReq ${tableComment}
     * @return 受影响的行数
     * @author ${author}
     */
    @Override
    @Transactional
    public ${className}DetailResp create(${className}SaveReq createReq) {
        ${className}Entity entity = ObjectUtils.copy(createReq, ${className}Entity.class);
        ${classNameFirstLower}Mapper.insert(entity);
        return getBy<@pkTrav "pk_fun_names" />(<#list pks as column><#include "/include/column/properties.ftl">entity.get${propertyName}()<#if (column?has_next)>, </#if></#list>);
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
    public int batchCreate(List<${className}SaveReq> list) {
        List<${className}Entity> entityList = ObjectUtils.copy(list, ${className}Entity.class);
        return ${classNameFirstLower}Mapper.batchInsert(entityList);
    }

    // region 删除<#if hasValidStatusColumn>/启用/禁用</#if>

    /**
     * 删除${tableComment}
     *
     <@pkTrav "batch_pk_params_comment" true "     "/>
     * @param baseEntity
     * @return 受影响的行数
     * @author ${author}
     */
    @Override
    @Transactional
    public int deleteBy<@pkTrav "pk_fun_names" />(<@pkTrav "batch_pk_params" true /> BaseEntity baseEntity) {
        ${className}PkCondition condition = getPkCondition(<@pkTrav "batch_pk_values" />);
        return ${classNameFirstLower}Mapper.deleteByCondition(condition);
    }
    <#if hasValidStatusColumn>

    /**
     * 禁用${tableComment}
     *
     <@pkTrav "batch_pk_params_comment" true "     "/>
     * @param baseEntity
     * @return 受影响的行数
     * @author ${author}
     */
    @Override
    @Transactional
    public int disableBy<@pkTrav "pk_fun_names" />(<@pkTrav "batch_pk_params" true /> BaseEntity baseEntity) {
        ${className}PkCondition condition = getPkCondition(<@pkTrav "batch_pk_values" />);
        ${className}Entity entity = ObjectUtils.copy(baseEntity, ${className}Entity.class);
        entity.set${validStatusPropertyName}(ValidConstant.INVALID);
        return ${classNameFirstLower}Mapper.patchByCondition(condition, entity);
    }

    /**
     * 启用${tableComment}
     *
     <@pkTrav "batch_pk_params_comment" true "     "/>
     * @param baseEntity
     * @return 受影响的行数
     * @author ${author}
     */
    @Override
    @Transactional
    public int enableBy<@pkTrav "pk_fun_names" />(<@pkTrav "batch_pk_params" true /> BaseEntity baseEntity) {
        ${className}PkCondition condition = getPkCondition(<@pkTrav "batch_pk_values" />);
        ${className}Entity entity = ObjectUtils.copy(baseEntity, ${className}Entity.class);
        entity.set${validStatusPropertyName}(ValidConstant.VALID);
        return ${classNameFirstLower}Mapper.patchByCondition(condition, entity);
    }
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
    @Override
    @Transactional
    public ${className}DetailResp patchBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_params" true /> ${className}SaveReq saveReq) {
        ${className}PkCondition condition = getPkCondition(<@pkTrav "pk_values" />);
        ${className}Entity entity = ObjectUtils.copy(saveReq, ${className}Entity.class);
        ${classNameFirstLower}Mapper.patchByCondition(condition, entity);
        return getBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_values" />);
    }

    /**
     * 根据<@pkTrav "pk_fun_comments" />更新${tableComment}所有字段
     *
     <@pkTrav "pk_params_comment" true "     "/>
     * @param saveReq 保存${tableComment}请求实体
     * @return 更新后的${tableComment}详情
     * @author ${author}
     */
    @Override
    @Transactional
    public ${className}DetailResp updateBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_params" true /> ${className}SaveReq saveReq) {
        ${className}Entity entity = ObjectUtils.copy(saveReq, ${className}Entity.class);
        ${classNameFirstLower}Mapper.updateBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_values" true /> entity);
        return getBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_values" />);
    }

    // endregion 更新

    // region 详情

    /**
     * 根据<@pkTrav "pk_fun_comments" />获取${tableComment}详情
     *
     <#list pks as column>
     <#include "/include/column/properties.ftl">
     * @param ${fieldName} ${columnFullComment}
     </#list>
     * @return ${tableComment}详情
     * @author ${author}
     */
    @Override
    public ${className}DetailResp getBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_params" />) {
        ${className}EntityExt entity = ${classNameFirstLower}Mapper.getBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_values" />);
        if (entity == null) {
            throw new MapleDataNotFoundException("${tableComment}不存在");
        }
        return ObjectUtils.copy(entity, ${className}DetailResp.class);
    }

    // endregion 详情

    /**
     * 获取${tableComment}列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的${tableComment}列表
     */
    @Override
    public List<${className}ItemResp> getList(${className}QueryReq queryReq) {
        ${className}FilterCondition condition = ObjectUtils.copy(queryReq, ${className}FilterCondition.class);
        List<${className}Entity> list = ${classNameFirstLower}Mapper.select(condition, null, queryReq.getSort());
        return ObjectUtils.copy(list, ${className}ItemResp.class);
    }

    /**
     * 分页获取${tableComment}列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的${tableComment}分页列表
     */
    @Override
    public PageList<${className}ItemResp> getPageList(${className}QueryReq queryReq, Integer pageNum, Integer pageSize) {

        ${className}FilterCondition condition = ObjectUtils.copy(queryReq, ${className}FilterCondition.class);
        ISelect select = () -> ${classNameFirstLower}Mapper.select(condition, null, queryReq.getSort());
        PageInfo<${className}EntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(select);

        List<${className}ItemResp> list = ObjectUtils.copy(pageInfo.getList(), ${className}ItemResp.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }

    private ${className}PkCondition getPkCondition(<@pkTrav "pk_params" />) {
        ${className}PkCondition condition = new ${className}PkCondition();
        <#list pks as column>
        <#include "/include/column/properties.ftl">
        condition.set${propertyName}(${fieldName});
        </#list>
        return condition;
    }
    <#if (table.hasUniPk)>

    private ${className}PkCondition getPkCondition(<@pkTrav "batch_pk_params" />) {
        ${className}PkCondition condition = new ${className}PkCondition();
        <#list pks as column>
        <#include "/include/column/properties.ftl">
        if (${fieldName}List.isEmpty()) {
            return null;
        } else if (${fieldName}List.size() == 1) {
            condition.set${propertyName}(${fieldName}List.get(0));
        } else {
            condition.set${propertyName}In(${fieldName}List);
        }
        </#list>
        return condition;
    }
    </#if>
}
