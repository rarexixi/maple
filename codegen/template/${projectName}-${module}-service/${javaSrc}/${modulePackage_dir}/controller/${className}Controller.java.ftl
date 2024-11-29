<#include "/include/table/properties.ftl">
package ${modulePackage}.controller;

import ${commonPackage}.annotation.Jsr303ValidGroup;
import ${commonPackage}.annotation.SetFieldTypes;
import ${commonPackage}.model.PageList;
import ${commonPackage}.model.BaseEntity;
import ${modulePackage}.model.request.${className}QueryReq;
import ${modulePackage}.model.request.${className}SaveReq;
import ${modulePackage}.model.response.${className}DetailResp;
import ${modulePackage}.model.response.${className}ItemResp;
import ${modulePackage}.service.${className}Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;
import java.util.List;

import static ${commonPackage}.constant.SetFieldType.*;

@CrossOrigin
@RequestMapping(${className}Controller.BASE_URL)
@RestController
@Validated
public class ${className}Controller {

    public static final String BASE_URL = "/api/${tablePluralPath}";

    private final ${className}Service ${classNameFirstLower}Service;

    @Autowired
    public ${className}Controller(${className}Service ${classNameFirstLower}Service) {
        this.${classNameFirstLower}Service = ${classNameFirstLower}Service;
    }

    // region 创建

    @PostMapping
    public ResponseEntity<${className}DetailResp> create(@Validated({Jsr303ValidGroup.Post.class}) @RequestBody @SetFieldTypes(types = {CREATE}) ${className}SaveReq ${classNameFirstLower}) {
        ${className}DetailResp detail = ${classNameFirstLower}Service.create(${classNameFirstLower});
        String detailPath = String.format("%s<#list pks as column><#include "/include/column/properties.ftl">/%s</#list>", BASE_URL<#list pks as column><#include "/include/column/properties.ftl">, detail.get${propertyName}()</#list>);
        return ResponseEntity.created(URI.create(detailPath)).body(detail);
    }

    // endregion 创建

    // region 删除<#if hasValidStatusColumn>/启用/禁用</#if>

    @DeleteMapping("<@pkTrav "batch_pk_path"/>")
    public ResponseEntity<Integer> deleteBy<@pkTrav "pk_fun_names" />(
            <@pkTrav "batch_pk_req_params" true "            "/>
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = ${classNameFirstLower}Service.deleteBy<@pkTrav "pk_fun_names" />(<@pkTrav "batch_pk_values" true /> baseEntity);
        return ResponseEntity.ok(count);
    }
    <#if hasValidStatusColumn>

    @PatchMapping("/disable<@pkTrav "batch_pk_path"/>")
    public ResponseEntity<Integer> disableBy<@pkTrav "pk_fun_names" />(
            <@pkTrav "batch_pk_req_params" true "            "/>
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = ${classNameFirstLower}Service.disableBy<@pkTrav "pk_fun_names" />(<@pkTrav "batch_pk_values" true /> baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable<@pkTrav "batch_pk_path"/>")
    public ResponseEntity<Integer> enableBy<@pkTrav "pk_fun_names" />(
            <@pkTrav "batch_pk_req_params" true "            "/>
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = ${classNameFirstLower}Service.enableBy<@pkTrav "pk_fun_names" />(<@pkTrav "batch_pk_values" true /> baseEntity);
        return ResponseEntity.ok(count);
    }
    </#if>

    // endregion 删除<#if hasValidStatusColumn>/启用/禁用</#if>

    // region 更新

    @PatchMapping("<@pkTrav "pk_path"/>")
    public ResponseEntity<${className}DetailResp> patchBy<@pkTrav "pk_fun_names" />(
            <@pkTrav type="pk_req_params" hasMoreParams=true leftOffset="            "/>
            @Validated({Jsr303ValidGroup.Patch.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) ${className}SaveReq ${classNameFirstLower}
    ) {
        ${className}DetailResp detail = ${classNameFirstLower}Service.patchBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_values" true /> ${classNameFirstLower});
        return ResponseEntity.ok(detail);
    }

    @PutMapping("<@pkTrav "pk_path"/>")
    public ResponseEntity<${className}DetailResp> updateBy<@pkTrav "pk_fun_names" />(
            <@pkTrav type="pk_req_params" hasMoreParams=true leftOffset="            "/>
            @Validated({Jsr303ValidGroup.Put.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) ${className}SaveReq ${classNameFirstLower}
    ) {
        ${className}DetailResp detail = ${classNameFirstLower}Service.updateBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_values" true /> ${classNameFirstLower});
        return ResponseEntity.ok(detail);
    }

    // endregion 更新

    // region 详情

    @GetMapping("<@pkTrav "pk_path"/>")
    public ResponseEntity<${className}DetailResp> getBy<@pkTrav "pk_fun_names" />(
            <@pkTrav type="pk_req_params" hasMoreParams=false leftOffset="            "/>
    ) {
        ${className}DetailResp detail = ${classNameFirstLower}Service.getBy<@pkTrav "pk_fun_names" />(<@pkTrav "pk_values" />);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情

    @GetMapping("/all")
    public ResponseEntity<List<${className}ItemResp>> getList(${className}QueryReq queryReq) {
        return ResponseEntity.ok(${classNameFirstLower}Service.getList(queryReq));
    }

    @GetMapping
    public ResponseEntity<PageList<${className}ItemResp>> getPageList(
            ${className}QueryReq queryReq,
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "50") @Min(value = 1, message = "分页大小必须大于0") Integer pageSize
    ) {
        return ResponseEntity.ok(${classNameFirstLower}Service.getPageList(queryReq, pageNum, pageSize));
    }
    <#-- 导出数据接口，暂时删除
    @GetMapping("/export")
    public ResponseEntity<?> export(
            HttpServletResponse response,
            ${className}QueryReq queryReq,
            @RequestParam(value = "exportName", defaultValue = "${tableComment}", required = false) String exportName
    ) throws IOException {

        ExcelUtils.export(response, ${classNameFirstLower}Service.getList(queryReq), ${className}ItemResp.class, exportName, "${tableComment}");
        return null;
    }
    -->
}
