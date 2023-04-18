package org.xi.maple.scheduler.model;

import lombok.Data;

/**
 * @author xishihao
 */
@Data
public class EngineInstance {
    private Integer id;
    private String status;
    private Integer cleaned;
}
