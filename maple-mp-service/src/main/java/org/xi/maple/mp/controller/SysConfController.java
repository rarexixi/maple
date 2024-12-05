package org.xi.maple.mp.controller;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.mp.model.request.SysConfQueryReq;
import org.xi.maple.mp.model.request.SysConfSaveReq;
import org.xi.maple.mp.model.response.SysConfDetailResp;
import org.xi.maple.mp.model.response.SysConfItemResp;
import org.xi.maple.mp.service.SysConfService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;
import java.util.List;

import static org.xi.maple.common.constant.SetFieldType.*;

@CrossOrigin
@RequestMapping(SysConfController.BASE_URL)
@RestController
@Validated
public class SysConfController {

    public static final String BASE_URL = "/api/sys-conf";

    private final SysConfService sysConfService;

    @Autowired
    public SysConfController(SysConfService sysConfService) {
        this.sysConfService = sysConfService;
    }

    // region 创建

    @PostMapping
    public ResponseEntity<SysConfDetailResp> create(@Validated({Jsr303ValidGroup.Post.class}) @RequestBody @SetFieldTypes(types = {CREATE}) SysConfSaveReq sysConf) {
        SysConfDetailResp detail = sysConfService.create(sysConf);
        String detailPath = String.format("%s/%s", BASE_URL, detail.getConfKey());
        return ResponseEntity.created(URI.create(detailPath)).body(detail);
    }

    // endregion 创建

    // region 删除/启用/禁用

    @DeleteMapping("/{confKeyList}")
    public ResponseEntity<Integer> deleteByConfKey(
            @PathVariable("confKeyList") @Validated List<@NotBlank(message = "confKey(配置键)不能为空") String> confKeyList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = sysConfService.deleteByConfKey(confKeyList, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/disable/{confKeyList}")
    public ResponseEntity<Integer> disableByConfKey(
            @PathVariable("confKeyList") @Validated List<@NotBlank(message = "confKey(配置键)不能为空") String> confKeyList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = sysConfService.disableByConfKey(confKeyList, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable/{confKeyList}")
    public ResponseEntity<Integer> enableByConfKey(
            @PathVariable("confKeyList") @Validated List<@NotBlank(message = "confKey(配置键)不能为空") String> confKeyList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = sysConfService.enableByConfKey(confKeyList, baseEntity);
        return ResponseEntity.ok(count);
    }

    // endregion 删除/启用/禁用

    // region 更新

    @PatchMapping("/{confKey}")
    public ResponseEntity<SysConfDetailResp> patchByConfKey(
            @PathVariable("confKey") @NotBlank(message = "confKey(配置键)不能为空") String confKey,
            @Validated({Jsr303ValidGroup.Patch.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) SysConfSaveReq sysConf
    ) {
        SysConfDetailResp detail = sysConfService.patchByConfKey(confKey, sysConf);
        return ResponseEntity.ok(detail);
    }

    @PutMapping("/{confKey}")
    public ResponseEntity<SysConfDetailResp> updateByConfKey(
            @PathVariable("confKey") @NotBlank(message = "confKey(配置键)不能为空") String confKey,
            @Validated({Jsr303ValidGroup.Put.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) SysConfSaveReq sysConf
    ) {
        SysConfDetailResp detail = sysConfService.updateByConfKey(confKey, sysConf);
        return ResponseEntity.ok(detail);
    }

    // endregion 更新

    // region 详情

    @GetMapping("/{confKey}")
    public ResponseEntity<SysConfDetailResp> getByConfKey(
            @PathVariable("confKey") @NotBlank(message = "confKey(配置键)不能为空") String confKey
    ) {
        SysConfDetailResp detail = sysConfService.getByConfKey(confKey);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情

    @GetMapping("/all")
    public ResponseEntity<List<SysConfItemResp>> getList(SysConfQueryReq queryReq) {
        return ResponseEntity.ok(sysConfService.getList(queryReq));
    }

    @GetMapping
    public ResponseEntity<PageList<SysConfItemResp>> getPageList(
            SysConfQueryReq queryReq,
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "50") @Min(value = 1, message = "分页大小必须大于0") Integer pageSize
    ) {
        return ResponseEntity.ok(sysConfService.getPageList(queryReq, pageNum, pageSize));
    }
}
