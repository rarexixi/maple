<#include "/include/table/properties.ftl">
package ${modulePackage}.persistence.mapper;

import ${commonPackage}.mapper.*;
import ${modulePackage}.persistence.condition.${className}FilterCondition;
import ${modulePackage}.persistence.condition.${className}PkCondition;
import ${modulePackage}.persistence.entity.${className}Entity;
import ${modulePackage}.persistence.entity.${className}EntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ${table.comment}数据访问
 *
 * @author ${author}
 */
@Mapper
public interface ${className}Mapper extends
        InsertMapper<${className}Entity>,
        BatchInsertMapper<${className}Entity>,
        DeleteByConditionMapper<${className}PkCondition>,
        PatchByConditionMapper<${className}Entity, ${className}PkCondition>,
        SelectByConditionMapper<${className}Entity, ${className}FilterCondition>,
        CountByConditionMapper<${className}FilterCondition> {

    int updateBy<@pkTrav "pk_fun_names" />(<#list pks as column><#include "/include/column/properties.ftl">@Param("${fieldName}") ${fieldType} ${fieldName}, </#list>@Param("entity") ${className}Entity entity);

    ${className}EntityExt getBy<@pkTrav "pk_fun_names" />(<#list pks as column><#include "/include/column/properties.ftl">@Param("${fieldName}") ${fieldType} ${fieldName}<#if (column?has_next)>, </#if></#list>);
}
