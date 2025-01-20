package org.xi.maple.persistence.controller;

import org.xi.maple.persistence.model.response.ApplicationDetailResp;
import org.xi.maple.persistence.service.ApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;

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

    // region 详情

    @GetMapping("/{appName}")
    public ResponseEntity<ApplicationDetailResp> getByAppName(
            @PathVariable("appName") @NotBlank(message = "appName(应用名称)不能为空") String appName
    ) {
        ApplicationDetailResp detail = applicationService.getByAppName(appName);
        return ResponseEntity.ok(detail);
    }

    // endregion 详情
}
