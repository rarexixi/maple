package org.xi.maple.mp.model.request;

import lombok.Data;
import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.common.util.JsonUtils;

import javax.validation.constraints.*;
import java.util.Map;

@Data
public class ClusterEngineSaveReq extends BaseEntity {

    /**
     * 引擎ID
     */
    @NotNull(message = "id(引擎ID)不能为空", groups = {Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    private Integer id;

    /**
     * 所属集群
     */
    private String clusterId;

    /**
     * 引擎名称
     */
    private String name;

    /**
     * 引擎版本
     */
    private String version;

    /**
     * 引擎目录
     */
    private String engineHome;

    /**
     * 引擎配置
     */
    @NotBlank(message = "engineConf(引擎配置)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String engineConf;

    public void setEngineConf(Map<String, ?> engineConf) {
        this.engineConf = JsonUtils.toJsonString(engineConf, "{}");
    }
}
