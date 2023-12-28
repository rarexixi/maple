package org.xi.maple.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.util.JsonUtils;

public class Test {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class En {
        private String name;
        private EngineExecutionStatus aa;
    }

    public static void main(String[] args) {
        System.out.println(EngineExecutionStatus.valueOf("nn"));
    }
}
