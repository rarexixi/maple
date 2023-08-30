<#include "/include/table/properties.ftl">
package ${modulePackage}.persistence.mapper;

import ${commonPackage}.mapper.*;
import ${modulePackage}.persistence.condition.${className}SelectCondition;
import ${modulePackage}.persistence.condition.${className}UpdateCondition;
import ${modulePackage}.persistence.entity.${className}Entity;
import ${modulePackage}.persistence.entity.${className}EntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * ${table.comment}数据访问
 *
 * @author ${author}
 */
@Mapper
public interface ${className}Mapper extends
        InsertMapper<${className}Entity>,
<#if (table.hasUniPk)>
    <#list pks as column>
        <#include "/include/column/properties.ftl">
    </#list>
    <#if (fieldType == "Integer") && (fieldName == "id")>
        DeleteByIdMapper,
        UpdateByIdMapper<${className}Entity>,
        SelectByIdMapper<${className}EntityExt>,
    <#else>
        DeleteByPkMapper<${fieldType}>,
        UpdateByPkMapper<${className}Entity, ${fieldType}>,
        SelectByPkMapper<${className}EntityExt, ${fieldType}>,
    </#if>
<#else>
        DeleteByPkConditionMapper,
        DeleteByConditionMapper<${className}UpdateCondition>,
        UpdateByConditionMapper<${className}Entity, ${className}UpdateCondition>,
</#if>
        SelectByConditionMapper<${className}Entity, ${className}SelectCondition>,
        CountByConditionMapper<${className}SelectCondition> {
}
