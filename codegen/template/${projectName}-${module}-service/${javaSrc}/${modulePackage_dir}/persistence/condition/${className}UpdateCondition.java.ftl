<#include "/include/table/properties.ftl">
package ${modulePackage}.persistence.condition;

import ${commonPackage}.model.ManipulateCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ${table.comment}更新条件
 *
 * @author ${author}
 */
@Getter
@Setter
@ToString
public class ${className}UpdateCondition extends ManipulateCondition {
    <#list pks as column>
    <#include "/include/column/properties.ftl">

    /**
     * ${columnFullComment}
     */
    private ${fieldType} ${fieldName};
    </#list>
}
