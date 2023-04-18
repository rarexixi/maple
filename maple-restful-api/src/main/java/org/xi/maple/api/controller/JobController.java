package org.xi.maple.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.maple.api.model.request.SubmitJobRequest;
import org.xi.maple.api.service.JobService;

/**
 * 作业提交 Controller
 *
 * @author xishihao
 */
@RestController
@RequestMapping("job")
public class JobController {

    final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("submit")
    public ResponseEntity<Integer> submitJob(@RequestBody SubmitJobRequest submitJobRequest) {
        Integer id = jobService.submitJob(submitJobRequest);
        return ResponseEntity.ok(id);
    }

}
