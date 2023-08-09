<#include "/include/table/properties.ftl">
package ${modulePackage}.persistence.mapper;

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
        SelectByConditionMapper<${className}Entity, ${className}SelectCondition>,
<#if (table.hasUniPk)>
    <#list pks as column>
        <#include "/include/column/properties.ftl">
    </#list>
    <#if (fieldType == "Integer") && (fieldName == "id")>
        SelectByIdMapper<${className}EntityExt>
        UpdateByIdMapper<${className}Entity>
        DeleteByIdMapper
    <#else>
        SelectByPkMapper<${className}EntityExt, ${fieldType}>
        UpdateByPkMapper<${className}Entity, ${fieldType}>
        DeleteByPkMapper<${fieldType}>
    </#if>
<#else>
        UpdateByConditionMapper<${className}Entity, ${className}UpdateCondition>
        DeleteByConditionMapper<${className}DeleteeCondition>
</#if>
}
