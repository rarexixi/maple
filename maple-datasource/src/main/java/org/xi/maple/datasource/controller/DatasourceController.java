package org.xi.maple.datasource.controller;

import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.models.PageList;
import org.xi.maple.common.utils.ExcelUtils;
import org.xi.maple.datasource.model.request.DatasourceAddRequest;
import org.xi.maple.datasource.model.request.DatasourcePatchRequest;
import org.xi.maple.datasource.model.request.DatasourceQueryRequest;
import org.xi.maple.datasource.model.request.DatasourceSaveRequest;
import org.xi.maple.datasource.model.response.DatasourceDetailResponse;
import org.xi.maple.datasource.model.response.DatasourceListItemResponse;
import org.xi.maple.datasource.service.DatasourceService;
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
@RequestMapping("/datasource")
@RestController
@Validated
public class DatasourceController {

    private final DatasourceService datasourceService;

    @Autowired
    public DatasourceController(DatasourceService datasourceService) {
        this.datasourceService = datasourceService;
    }

    @PostMapping("/add")
    public ResponseEntity<DatasourceDetailResponse> add(@Validated @RequestBody @SetFieldTypes(types = {"create"}) DatasourceAddRequest datasource) {
        DatasourceDetailResponse detail = datasourceService.add(datasource);
        return ResponseEntity.created(URI.create("")).body(detail);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Integer> delete(@Validated @SetFieldTypes(types = {"update"}) DatasourcePatchRequest patchRequest) {
        Integer count = datasourceService.delete(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/disable")
    public ResponseEntity<Integer> disable(@Validated @SetFieldTypes(types = {"update"}) DatasourcePatchRequest patchRequest) {
        Integer count = datasourceService.disable(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/enable")
    public ResponseEntity<Integer> enable(@Validated @SetFieldTypes(types = {"update"}) DatasourcePatchRequest patchRequest) {
        Integer count = datasourceService.enable(patchRequest);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/update")
    public ResponseEntity<DatasourceDetailResponse> updateById(@Validated @RequestBody @SetFieldTypes(types = {"update"}) DatasourceSaveRequest datasource) {
        DatasourceDetailResponse detail = datasourceService.updateById(datasource);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/detail")
    public ResponseEntity<DatasourceDetailResponse> getById(@RequestParam("id") @NotNull(message = "Id不能为空") @Min(value = 1, message = "Id必须大于0") Integer id) {
        DatasourceDetailResponse detail = datasourceService.getById(id);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/list")
    public ResponseEntity<List<DatasourceListItemResponse>> getList(DatasourceQueryRequest queryRequest) {
        return ResponseEntity.ok(datasourceService.getList(queryRequest));
    }

    @GetMapping("/page-list")
    public ResponseEntity<PageList<DatasourceListItemResponse>> getPageList(
            DatasourceQueryRequest queryRequest,
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "50") @Min(value = 1, message = "分页大小必须大于0") Integer pageSize
    ) {
        return ResponseEntity.ok(datasourceService.getPageList(queryRequest, pageNum, pageSize));
    }

    @GetMapping("/export")
    public ResponseEntity<?> export(HttpServletResponse response, DatasourceQueryRequest queryRequest,
                                    @RequestParam(value = "exportName", defaultValue = "数据源配置", required = false) String exportName) throws IOException {

        ExcelUtils.export(response, datasourceService.getList(queryRequest), DatasourceListItemResponse.class, exportName, "数据源配置");
        return null;
    }
}
