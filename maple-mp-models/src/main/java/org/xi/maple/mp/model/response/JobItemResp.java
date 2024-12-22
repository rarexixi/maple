package org.xi.maple.mp.model.response;

import lombok.Data;
import org.xi.maple.common.util.JsonUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Data
public class JobItemResp implements Serializable {

    /**
     * 作业ID
     */
    private Integer id;

    /**
     * 作业名
     */
    private String jobName;

    /**
     * 作业说明
     */
    private String description;

    /**
     * 作业类型
     */
    private String jobType;

    /**
     * 引擎ID
     */
    private Integer engineId;

    /**
     * 作业负责人
     */
    private String owner;

    /**
     * 执行配置
     */
    private String runConf;

    /**
     * 作业配置
     */
    private String jobConf;

    /**
     * 是否禁用
     */
    private Integer disabled;

    /**
     * 创建人
     */
    private Integer createdBy;

    /**
     * 修改人
     */
    private Integer updatedBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    public Map<String, Object> getRunConf() {
        return (Map<String, Object>) JsonUtils.parseObject(runConf, Map.class, Collections.emptyMap());
    }

    public Map<String, Object> getJobConf() {
        return (Map<String, Object>) JsonUtils.parseObject(jobConf, Map.class, Collections.emptyMap());
    }
}
