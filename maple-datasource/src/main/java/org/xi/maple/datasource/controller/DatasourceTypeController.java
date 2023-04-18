package org.xi.maple.datasource.controller;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.datasource.model.request.DatasourceTypeAddRequest;
import org.xi.maple.datasource.model.request.DatasourceTypePatchRequest;
import org.xi.maple.datasource.model.request.DatasourceTypeQueryRequest;
import org.xi.maple.datasource.model.request.DatasourceTypeSaveRequest;
import org.xi.maple.datasource.model.response.DatasourceTypeDetailResponse;
import org.xi.maple.datasource.model.response.DatasourceTypeListItemResponse;
import org.xi.maple.datasource.service.DatasourceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

@CrossOrigin
@RequestMapping("/datasource-type")
@RestController
@Validated
public class DatasourceTypeController {

    private final DatasourceTypeService datasourceTypeService;

    @Autowired
    public DatasourceTypeController(DatasourceTypeService datasourceTypeService) {
        this.datasourceTypeService = datasourceTypeService;
    }

    @PostMapping("/add")
    public ResponseEntity<DatasourceTypeDetailResponse> add(@Validated @RequestBody @SetFieldTypes(types = {"create"}) DatasourceTypeAddRequest datasourceType) {
        DatasourceTypeDetailResponse detail = datasourceTypeService.add(datasourceType);
        return ResponseEntity.created(URI.create("")).body(detail);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Integer> delete(@Validated @SetFieldTypes(types = {"update"}) DatasourceTypePatchRequest patchRequest) {
        Integer count = datasourceTypeService.delete(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/disable")
    public ResponseEntity<Integer> disable(@Validated @SetFieldTypes(types = {"update"}) DatasourceTypePatchRequest patchRequest) {
        Integer count = datasourceTypeService.disable(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable")
    public ResponseEntity<Integer> enable(@Validated @SetFieldTypes(types = {"update"}) DatasourceTypePatchRequest patchRequest) {
        Integer count = datasourceTypeService.enable(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/update")
    public ResponseEntity<DatasourceTypeDetailResponse> updateByTypeCode(
            @Validated @RequestBody @SetFieldTypes(types = {"update"}) DatasourceTypeSaveRequest datasourceType,
            @RequestParam("typeCode") @NotBlank(message = "typeCode(类型编码)不能为空") String typeCode
    ) {
        DatasourceTypeDetailResponse detail = datasourceTypeService.updateByTypeCode(datasourceType, typeCode);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/detail")
    public ResponseEntity<DatasourceTypeDetailResponse> getByTypeCode(@RequestParam("typeCode") @NotBlank(message = "类型编码不能为空") String typeCode) {
        DatasourceTypeDetailResponse detail = datasourceTypeService.getByTypeCode(typeCode);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/list")
    public ResponseEntity<List<DatasourceTypeListItemResponse>> getList(DatasourceTypeQueryRequest queryRequest) {
        return ResponseEntity.ok(datasourceTypeService.getList(queryRequest));
    }

    @GetMapping("/export")
    public ResponseEntity<?> export(HttpServletResponse response, DatasourceTypeQueryRequest queryRequest,
                                    @RequestParam(value = "exportName", defaultValue = "数据源类型", required = false) String exportName) throws IOException {
        String fileName = StringUtils.isBlank(exportName) ? "数据源配置" : exportName;
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "utf-8"));
        EasyExcel.write(response.getOutputStream(), DatasourceTypeListItemResponse.class).sheet().doWrite(datasourceTypeService.getList(queryRequest));
        return ResponseEntity.ok(null);
    }
}
