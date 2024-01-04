package org.xi.maple.builder.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class Spark3YarnDataModel implements Serializable {
    private String sparkHome;

    // --queue QUEUE_NAME          The YARN queue to submit to (Default: "default").
    private String queue;

    // --driver-cores NUM          Number of cores used by the driver, only in cluster mode (Default: 1).
    private Integer driverCores;
    // --driver-memory MEM         Memory for driver (e.g. 1000M, 2G) (Default: 1024M).
    private String driverMemory;
    // --num-executors NUM         Number of executors to launch (Default: 2). If dynamic allocation is enabled, the initial number of executors will be at least NUM.
    private Integer numExecutors;
    // --executor-cores NUM        Number of cores used by each executor. (Default: 1 in YARN and K8S modes, or all available cores on the worker in standalone mode).
    private Integer executorCores;
    // --executor-memory MEM       Memory per executor (e.g. 1000M, 2G) (Default: 1G).
    private String executorMemory;

    // --driver-java-options       Extra Java options to pass to the driver.
    private String driverJavaOptions;
    // --driver-class-path         Extra class path entries to pass to the driver. Note that jars added with --jars are automatically included in the classpath.
    private String driverClassPath;

    // --jars JARS                 Comma-separated list of jars to include on the driver and executor classpaths.
    private String jars;
    // --files FILES               Comma-separated list of files to be placed in the working directory of each executor. File paths of these files in executors can be accessed via SparkFiles.get(fileName).
    private String files;
    // --archives ARCHIVES         Comma-separated list of archives to be extracted into the working directory of each executor.
    private String archives;

    // --conf                      -c PROP=VALUE, Arbitrary Spark configuration property.
    private Map<String, String> conf;

    // data_calc, sql, scala, py, jar
    private String runType;

    private Map<String, Object> runConf;
}
