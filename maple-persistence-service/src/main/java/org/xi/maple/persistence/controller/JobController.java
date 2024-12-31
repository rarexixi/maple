package org.xi.maple.persistence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xi.maple.persistence.model.response.JobDetailResp;
import org.xi.maple.persistence.service.JobService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

    // region 详情

    @GetMapping("/{id}")
    public ResponseEntity<JobDetailResp> getById(
            @PathVariable("id") @NotNull(message = "id(作业ID)不能为空") @Min(value = 1, message = "id(作业ID)必须大于0") Integer id
    ) {
        JobDetailResp detail = jobService.getById(id);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情
}
