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
        BatchInsertMapper<${className}Entity>,
<#if (table.hasUniPk)>
    <#if (uniPkFieldType == "Integer") && (uniPkFieldName == "id")>
        DeleteByIdMapper,
        UpdateByIdMapper<${className}Entity>,
        SelectByIdMapper<${className}EntityExt>,
    <#else>
        DeleteByPkMapper<${uniPkFieldType}>,
        UpdateByPkMapper<${className}Entity, ${uniPkFieldType}>,
        SelectByPkMapper<${className}EntityExt, ${uniPkFieldType}>,
    </#if>
<#else>
        DeleteByPkConditionMapper,
        DeleteByConditionMapper<${className}UpdateCondition>,
        UpdateByConditionMapper<${className}Entity, ${className}UpdateCondition>,
</#if>
        SelectByConditionMapper<${className}Entity, ${className}SelectCondition>,
        CountByConditionMapper<${className}SelectCondition> {
}
