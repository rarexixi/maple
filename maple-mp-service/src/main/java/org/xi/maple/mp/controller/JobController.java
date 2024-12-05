package org.xi.maple.mp.controller;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.mp.model.request.JobQueryReq;
import org.xi.maple.mp.model.request.JobSaveReq;
import org.xi.maple.mp.model.response.JobDetailResp;
import org.xi.maple.mp.model.response.JobItemResp;
import org.xi.maple.mp.service.JobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.net.URI;
import java.util.List;

import static org.xi.maple.common.constant.SetFieldType.*;

@CrossOrigin
@RequestMapping(JobController.BASE_URL)
@RestController
@Validated
public class JobController {

    public static final String BASE_URL = "/api/jobs";

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // region 创建

    @PostMapping
    public ResponseEntity<JobDetailResp> create(@Validated({Jsr303ValidGroup.Post.class}) @RequestBody @SetFieldTypes(types = {CREATE}) JobSaveReq job) {
        JobDetailResp detail = jobService.create(job);
        String detailPath = String.format("%s/%s", BASE_URL, detail.getId());
        return ResponseEntity.created(URI.create(detailPath)).body(detail);
    }

    // endregion 创建

    // region 删除/启用/禁用

    @DeleteMapping("/{idList}")
    public ResponseEntity<Integer> deleteById(
            @PathVariable("idList") @Validated List<@NotNull(message = "id(作业ID)不能为空") @Min(value = 1, message = "id(作业ID)必须大于0") Integer> idList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = jobService.deleteById(idList, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/disable/{idList}")
    public ResponseEntity<Integer> disableById(
            @PathVariable("idList") @Validated List<@NotNull(message = "id(作业ID)不能为空") @Min(value = 1, message = "id(作业ID)必须大于0") Integer> idList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = jobService.disableById(idList, baseEntity);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable/{idList}")
    public ResponseEntity<Integer> enableById(
            @PathVariable("idList") @Validated List<@NotNull(message = "id(作业ID)不能为空") @Min(value = 1, message = "id(作业ID)必须大于0") Integer> idList,
            @SetFieldTypes(types = {UPDATE}) BaseEntity baseEntity
    ) {
        Integer count = jobService.enableById(idList, baseEntity);
        return ResponseEntity.ok(count);
    }

    // endregion 删除/启用/禁用

    // region 更新

    @PatchMapping("/{id}")
    public ResponseEntity<JobDetailResp> patchById(
            @PathVariable("id") @NotNull(message = "id(作业ID)不能为空") @Min(value = 1, message = "id(作业ID)必须大于0") Integer id,
            @Validated({Jsr303ValidGroup.Patch.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) JobSaveReq job
    ) {
        JobDetailResp detail = jobService.patchById(id, job);
        return ResponseEntity.ok(detail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobDetailResp> updateById(
            @PathVariable("id") @NotNull(message = "id(作业ID)不能为空") @Min(value = 1, message = "id(作业ID)必须大于0") Integer id,
            @Validated({Jsr303ValidGroup.Put.class}) @RequestBody @SetFieldTypes(types = {UPDATE}) JobSaveReq job
    ) {
        JobDetailResp detail = jobService.updateById(id, job);
        return ResponseEntity.ok(detail);
    }

    // endregion 更新

    // region 详情

    @GetMapping("/{id}")
    public ResponseEntity<JobDetailResp> getById(
            @PathVariable("id") @NotNull(message = "id(作业ID)不能为空") @Min(value = 1, message = "id(作业ID)必须大于0") Integer id
    ) {
        JobDetailResp detail = jobService.getById(id);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情

    @GetMapping("/all")
    public ResponseEntity<List<JobItemResp>> getList(JobQueryReq queryReq) {
        return ResponseEntity.ok(jobService.getList(queryReq));
    }

    @GetMapping
    public ResponseEntity<PageList<JobItemResp>> getPageList(
            JobQueryReq queryReq,
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "50") @Min(value = 1, message = "分页大小必须大于0") Integer pageSize
    ) {
        return ResponseEntity.ok(jobService.getPageList(queryReq, pageNum, pageSize));
    }
}
