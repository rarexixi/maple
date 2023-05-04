package org.xi.maple.scheduler.model;

import lombok.Data;

import java.util.List;

/**
 * @author xishihao
 */
@Data
public class YarnScheduler {

    private Scheduler scheduler;

    @Data
    public static class Scheduler {

        private SchedulerInfo schedulerInfo;

        @Data
        public static class SchedulerInfo {

            private Queues queues;

            @Data
            public static class Queues {

                private List<Queue> queue;

                @Data
                public static class Queue {
                    private String queueName;
                    private double usedCapacity;

                    private Integer numActiveApplications;
                    private Integer numPendingApplications;
                    private Integer maxApplications;
                    private Integer maxApplicationsPerUser;

                    private Users users;

                    @Data
                    public static class Users {
                        private List<User> user;

                        @Data
                        public static class User {
                            private String username;
                            private Integer numActiveApplications;
                            private Integer numPendingApplications;
                        }
                    }
                }
            }
        }
    }
}
