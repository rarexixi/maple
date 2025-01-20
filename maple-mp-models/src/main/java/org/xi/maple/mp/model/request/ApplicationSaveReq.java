package org.xi.maple.mp.model.request;

import lombok.Data;
import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.model.BaseEntity;

import javax.validation.constraints.*;

@Data
public class ApplicationSaveReq extends BaseEntity {

    /**
     * 应用名称
     */
    @NotBlank(message = "appName(应用名称)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    private String appName;

    /**
     * 应用访问密钥
     */
    private String accessKey;

    /**
     * 允许请求的IP
     */
    private String legalHosts;

    /**
     * 回调接口
     */
    private String webhooks;
}
