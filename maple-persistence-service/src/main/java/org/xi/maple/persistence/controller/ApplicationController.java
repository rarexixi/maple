package org.xi.maple.persistence.controller;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.model.request.ApplicationQueryReq;
import org.xi.maple.persistence.model.request.ApplicationSaveReq;
import org.xi.maple.persistence.model.response.ApplicationDetailResp;
import org.xi.maple.persistence.model.response.ApplicationItemResp;
import org.xi.maple.persistence.service.ApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;
import java.util.List;

import static org.xi.maple.common.constant.SetFieldType.*;

@CrossOrigin
@RequestMapping(ApplicationController.BASE_URL)
@RestController
@Validated
public class ApplicationController {

    public static final String BASE_URL = "/api/applications";

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // region 创建

    @PostMapping
    public ResponseEntity<ApplicationDetailResp> create(@Validated({Jsr303ValidGroup.Post.class}) @RequestBody @SetFieldTypes(types = {CREATE}) ApplicationSaveReq application) {
        ApplicationDetailResp detail = applicationService.create(application);
        String detailPath = String.format("%s/%s", BASE_URL, detail.getAppName());
        return ResponseEntity.created(URI.create(detailPath)).body(detail);
    }

    // endregion 创建

    // region 删除/启用/禁用

    @DeleteMapping("/{appName}")
    public ResponseEntity<Integer> deleteByAppName(
            @PathVariable("appName") @NotBlank(message = "appName(应用名称)不能为空") String appName,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = applicationService.deleteByAppName(appName, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/disable/{appName}")
    public ResponseEntity<Integer> disableByAppName(
            @PathVariable("appName") @NotBlank(message = "appName(应用名称)不能为空") String appName,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = applicationService.disableByAppName(appName, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable/{appName}")
    public ResponseEntity<Integer> enableByAppName(
            @PathVariable("appName") @NotBlank(message = "appName(应用名称)不能为空") String appName,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = applicationService.enableByAppName(appName, baseEntity);
        return ResponseEntity.ok(count);
    }

    // endregion 删除/启用/禁用

    // region 更新

    @PutMapping("/{appName}")
    public ResponseEntity<ApplicationDetailResp> updateByAppName(
            @PathVariable("appName") @NotBlank(message = "appName(应用名称)不能为空") String appName,
            @Validated({Jsr303ValidGroup.Put.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) ApplicationSaveReq application
    ) {
        ApplicationDetailResp detail = applicationService.updateByAppName(appName, application);
        return ResponseEntity.ok(detail);
    }

    @PatchMapping("/{appName}")
    public ResponseEntity<ApplicationDetailResp> patchByAppName(
            @PathVariable("appName") @NotBlank(message = "appName(应用名称)不能为空") String appName,
            @Validated({Jsr303ValidGroup.Patch.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) ApplicationSaveReq application
    ) {
        ApplicationDetailResp detail = applicationService.patchByAppName(appName, application);
        return ResponseEntity.ok(detail);
    }

    // endregion 更新

    // region 详情

    @GetMapping("/{appName}")
    public ResponseEntity<ApplicationDetailResp> getByAppName(
            @PathVariable("appName") @NotBlank(message = "appName(应用名称)不能为空") String appName
    ) {
        ApplicationDetailResp detail = applicationService.getByAppName(appName);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情

    @GetMapping("/all")
    public ResponseEntity<List<ApplicationItemResp>> getList(ApplicationQueryReq queryReq) {
        return ResponseEntity.ok(applicationService.getList(queryReq));
    }

    @GetMapping
    public ResponseEntity<PageList<ApplicationItemResp>> getPageList(
            ApplicationQueryReq queryReq,
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "50") @Min(value = 1, message = "分页大小必须大于0") Integer pageSize
    ) {
        return ResponseEntity.ok(applicationService.getPageList(queryReq, pageNum, pageSize));
    }
}
