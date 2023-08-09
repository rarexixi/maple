<#include "/include/table/properties.ftl">
package ${modulePackage}.controller;

import ${commonPackage}.annotation.SetFieldTypes;
import ${commonPackage}.model.PageList;
import ${modulePackage}.model.request.${className}AddRequest;
import ${modulePackage}.model.request.${className}PatchRequest;
import ${modulePackage}.model.request.${className}QueryRequest;
import ${modulePackage}.model.request.${className}SaveRequest;
import ${modulePackage}.model.response.${className}DetailResponse;
import ${modulePackage}.model.response.${className}ListItemResponse;
import ${modulePackage}.service.${className}Service;

<#--import org.apache.shiro.authz.annotation.RequiresPermissions;-->
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;

@CrossOrigin
@RequestMapping("/${tablePath}")
@RestController
@Validated
public class ${className}Controller {

    private final ${className}Service ${classNameFirstLower}Service;

    @Autowired
    public ${className}Controller(${className}Service ${classNameFirstLower}Service) {
        this.${classNameFirstLower}Service = ${classNameFirstLower}Service;
    }

    @PostMapping("/add")
    public ResponseEntity<${className}DetailResponse> add(@Validated @RequestBody @SetFieldTypes(types = {"create"}) ${className}AddRequest ${classNameFirstLower}) {
        ${className}DetailResponse detail = ${classNameFirstLower}Service.add(${classNameFirstLower});
        return ResponseEntity.created(URI.create("")).body(detail);
    }
    <#-- region 删除/启用/禁用 -->
    <#if (table.hasUniPk)>

    @DeleteMapping("/delete")
    public ResponseEntity<Integer> delete(@Validated @SetFieldTypes(types = {"update"}) ${className}PatchRequest patchRequest) {
        Integer count = ${classNameFirstLower}Service.delete(patchRequest);
        return ResponseEntity.ok(count);
    }

    <#if table.validStatusColumn??>
    @PatchMapping("/disable")
    public ResponseEntity<Integer> disable(@Validated @SetFieldTypes(types = {"update"}) ${className}PatchRequest patchRequest) {
        Integer count = ${classNameFirstLower}Service.disable(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable")
    public ResponseEntity<Integer> enable(@Validated @SetFieldTypes(types = {"update"}) ${className}PatchRequest patchRequest) {
        Integer count = ${classNameFirstLower}Service.enable(patchRequest);
        return ResponseEntity.ok(count);
    }
    </#if>
    </#if>
    <#-- endregion 删除/启用/禁用 -->

    <#-- region 更新 -->
    <#if (table.hasAutoIncUniPk)>

    @PatchMapping("/update")
    public ResponseEntity<${className}DetailResponse> updateBy<#include "/include/table/pk_fun_names.ftl">(@Validated @RequestBody @SetFieldTypes(types = {"update"}) ${className}SaveRequest ${classNameFirstLower}) {
        ${className}DetailResponse detail = ${classNameFirstLower}Service.updateBy<#include "/include/table/pk_fun_names.ftl">(${classNameFirstLower});
        return ResponseEntity.ok(detail);
    }
    <#else>

    @PatchMapping("/update")
    public ResponseEntity<${className}DetailResponse> updateBy<#include "/include/table/pk_fun_names.ftl">(
            @Validated @RequestBody @SetFieldTypes(types = {"update"}) ${className}SaveRequest ${classNameFirstLower},
            <#list pks as column>
            <#include "/include/column/properties.ftl">
            @RequestParam("${fieldName}") @${isString ? string('NotBlank','NotNull')}(message = "${fieldName}(${columnComment})不能为空") ${fieldType} ${fieldName}<#if (column?has_next)>,</#if>
            </#list>
    ) {
        ${className}DetailResponse detail = ${classNameFirstLower}Service.updateBy<#include "/include/table/pk_fun_names.ftl">(${classNameFirstLower}, <#include "/include/table/pk_values.ftl">);
        return ResponseEntity.ok(detail);
    }
    </#if>
    <#-- endregion 更新 -->

    <#-- region 详情 -->
    <#if (table.hasUniPk)>

    @GetMapping("/detail")
    public ResponseEntity<${className}DetailResponse> getBy<#include "/include/table/pk_fun_names.ftl">(@RequestParam("${uniPkFieldName}") <#if uniPkIsString>@NotBlank(message = "${uniPkComment}不能为空")<#else>@NotNull(message = "${uniPkComment}不能为空") @Min(value = 1, message = "${uniPkComment}必须大于0")</#if> ${uniPkFieldType} ${uniPkFieldName}) {
        ${className}DetailResponse detail = ${classNameFirstLower}Service.getBy<#include "/include/table/pk_fun_names.ftl">(${uniPkFieldName});
        return ResponseEntity.ok(detail);
    }
    <#else>

    @GetMapping("/detail")
    public ResponseEntity<${className}DetailResponse> getBy<#include "/include/table/pk_fun_names.ftl">(
            <#list pks as column>
            <#include "/include/column/properties.ftl">
            @RequestParam("${fieldName}") @${isString ? string('NotBlank','NotNull')}(message = "${fieldName}(${columnComment})不能为空") ${fieldType} ${fieldName}<#if (column?has_next)>,</#if>
            </#list>
    ) {
        ${className}DetailResponse detail = ${classNameFirstLower}Service.getBy<#include "/include/table/pk_fun_names.ftl">(<#include "/include/table/pk_values.ftl">);
        return ResponseEntity.ok(detail);
    }
    </#if>
    <#-- endregion 详情 -->

    @GetMapping("/list")
    public ResponseEntity<List<${className}ListItemResponse>> getList(${className}QueryRequest queryRequest) {
        return ResponseEntity.ok(${classNameFirstLower}Service.getList(queryRequest));
    }

    @GetMapping("/page-list")
    public ResponseEntity<PageList<${className}ListItemResponse>> getPageList(
            ${className}QueryRequest queryRequest,
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "50") @Min(value = 1, message = "分页大小必须大于0") Integer pageSize
    ) {
        return ResponseEntity.ok(${classNameFirstLower}Service.getPageList(queryRequest, pageNum, pageSize));
    }
<#--
    @GetMapping("/export")
    public ResponseEntity<?> export(HttpServletResponse response, ${className}QueryRequest queryRequest,
                                    @RequestParam(value = "exportName", defaultValue = "${tableComment}", required = false) String exportName) throws IOException {

        ExcelUtils.export(response, ${classNameFirstLower}Service.getList(queryRequest), ${className}ListItemResponse.class, exportName, "${tableComment}");
        return null;
    }-->
}
