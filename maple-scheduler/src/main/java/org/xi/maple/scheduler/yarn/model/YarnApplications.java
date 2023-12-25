package org.xi.maple.scheduler.yarn.model;

import lombok.Data;

import java.util.List;

@Data
public class YarnApplications {
    private Apps apps;

    @Data
    public static class Apps {
        private List<YarnApp> app;
    }
}