package org.xi.maple.persistence.controller;

import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.model.PageList;
import org.xi.maple.persistence.model.request.ApplicationAddRequest;
import org.xi.maple.persistence.model.request.ApplicationPatchRequest;
import org.xi.maple.persistence.model.request.ApplicationQueryRequest;
import org.xi.maple.persistence.model.request.ApplicationSaveRequest;
import org.xi.maple.persistence.model.response.ApplicationDetailResponse;
import org.xi.maple.persistence.model.response.ApplicationListItemResponse;
import org.xi.maple.persistence.service.ApplicationService;

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
@RequestMapping("/application")
@RestController
@Validated
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApplicationDetailResponse> add(@Validated @RequestBody @SetFieldTypes(types = {"create"}) ApplicationAddRequest application) {
        ApplicationDetailResponse detail = applicationService.add(application);
        return ResponseEntity.created(URI.create("")).body(detail);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Integer> delete(@Validated @SetFieldTypes(types = {"update"}) ApplicationPatchRequest patchRequest) {
        Integer count = applicationService.delete(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/disable")
    public ResponseEntity<Integer> disable(@Validated @SetFieldTypes(types = {"update"}) ApplicationPatchRequest patchRequest) {
        Integer count = applicationService.disable(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable")
    public ResponseEntity<Integer> enable(@Validated @SetFieldTypes(types = {"update"}) ApplicationPatchRequest patchRequest) {
        Integer count = applicationService.enable(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/update")
    public ResponseEntity<ApplicationDetailResponse> updateByAppName(
            @Validated @RequestBody @SetFieldTypes(types = {"update"}) ApplicationSaveRequest application,
            @RequestParam("appName") @NotBlank(message = "appName(应用名称)不能为空") String appName
    ) {
        ApplicationDetailResponse detail = applicationService.updateByAppName(application, appName);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/detail")
    public ResponseEntity<ApplicationDetailResponse> getByAppName(@RequestParam("appName") @NotBlank(message = "应用名称不能为空") String appName) {
        ApplicationDetailResponse detail = applicationService.getByAppName(appName);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ApplicationListItemResponse>> getList(ApplicationQueryRequest queryRequest) {
        return ResponseEntity.ok(applicationService.getList(queryRequest));
    }
}
