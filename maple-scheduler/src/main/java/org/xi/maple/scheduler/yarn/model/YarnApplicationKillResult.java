package org.xi.maple.scheduler.yarn.model;

import lombok.Data;

/**
 * @author xishihao
 */
@Data
public class YarnApplicationKillResult {

    private String state = "KILLED";
}
