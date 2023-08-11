package org.xi.maple.builder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class Spark3EngineExecution {
    // --master MASTER_URL         spark://host:port, mesos://host:port, yarn, k8s://https://host:port, or local (Default: local[*]).
    @JsonProperty(value="master")
    private String master;
    // --deploy-mode DEPLOY_MODE   Whether to launch the driver program locally ("client") or on one of the worker machines inside the cluster ("cluster") (Default: client).
    @JsonProperty(value="deploy-mode")
    private String deployMode;
    // --class CLASS_NAME          Your application's main class (for Java / Scala apps).
    @JsonProperty(value="class")
    private String mainClass;
    // --name NAME                 A name of your application.
    @JsonProperty(value="name")
    private String name;
    // --jars JARS                 Comma-separated list of jars to include on the driver and executor classpaths.
    @JsonProperty(value="jars")
    private String jars;
    // --packages                  Comma-separated list of maven coordinates of jars to include on the driver and executor classpaths. Will search the local maven repo, then maven central and any additional remote repositories given by --repositories. The format for the coordinates should be groupId:artifactId:version.
    @JsonProperty(value="packages")
    private String packages;
    // --exclude-packages          Comma-separated list of groupId:artifactId, to exclude while resolving the dependencies provided in --packages to avoid dependency conflicts.
    @JsonProperty(value="exclude-packages")
    private String excludePackages;
    // --repositories              Comma-separated list of additional remote repositories to search for the maven coordinates given with --packages.
    @JsonProperty(value="repositories")
    private String repositories;
    // --py-files PY_FILES         Comma-separated list of .zip, .egg, or .py files to place on the PYTHONPATH for Python apps.
    @JsonProperty(value="py-files")
    private String pyFiles;
    // --files FILES               Comma-separated list of files to be placed in the working directory of each executor. File paths of these files in executors can be accessed via SparkFiles.get(fileName).
    @JsonProperty(value="files")
    private String files;
    // --archives ARCHIVES         Comma-separated list of archives to be extracted into the working directory of each executor.
    @JsonProperty(value="archives")
    private String archives;
    // --conf                      -c PROP=VALUE, Arbitrary Spark configuration property.
    @JsonProperty(value="conf")
    private Map<String, String> conf;
    // --properties-file FILE      Path to a file from which to load extra properties. If not specified, this will look for conf/spark-defaults.conf.
    @JsonProperty(value="properties-file")
    private String propertiesFile;
    // --driver-cores NUM          Number of cores used by the driver, only in cluster mode (Default: 1).
    @JsonProperty(value="driver-cores")
    private Integer driverCores;
    // --driver-memory MEM         Memory for driver (e.g. 1000M, 2G) (Default: 1024M).
    @JsonProperty(value="driver-memory")
    private String driverMemory;
    // --driver-java-options       Extra Java options to pass to the driver.
    @JsonProperty(value="driver-java-options")
    private String driverJavaOptions;
    // --driver-library-path       Extra library path entries to pass to the driver.
    @JsonProperty(value="driver-library-path")
    private String driverLibraryPath;
    // --driver-class-path         Extra class path entries to pass to the driver. Note that jars added with --jars are automatically included in the classpath.
    @JsonProperty(value="driver-class-path")
    private String driverClassPath;
    // --num-executors NUM         Number of executors to launch (Default: 2). If dynamic allocation is enabled, the initial number of executors will be at least NUM.
    @JsonProperty(value="num-executors")
    private Integer numExecutors;
    // --executor-cores NUM        Number of cores used by each executor. (Default: 1 in YARN and K8S modes, or all available cores on the worker in standalone mode).
    @JsonProperty(value="executor-cores")
    private Integer executorCores;
    // --executor-memory MEM       Memory per executor (e.g. 1000M, 2G) (Default: 1G).
    @JsonProperty(value="executor-memory")
    private String executorMemory;
    // --proxy-user NAME           User to impersonate when submitting the application. This argument does not work with --principal / --keytab.
    @JsonProperty(value="proxy-user")
    private String proxyUser;
    // --principal PRINCIPAL       Principal to be used to login to KDC.
    @JsonProperty(value="principal")
    private String principal;
    // --keytab KEYTAB             The full path to the file that contains the keytab for the principal specified above.
    @JsonProperty(value="keytab")
    private String keytab;
    // --queue QUEUE_NAME          The YARN queue to submit to (Default: "default").
    @JsonProperty(value="queue")
    private String queue;

    private String runParameters;
}
