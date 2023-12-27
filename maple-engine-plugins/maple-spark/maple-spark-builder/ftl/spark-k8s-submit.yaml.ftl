apiVersion: "sparkoperator.k8s.io/v1beta2"
kind: SparkApplication
metadata:
  name: ${mapleAppName}-${mapleId}
  namespace: ${namespace}
  labels:
    from-app: maple-exec
    maple-id: "${mapleId}"
    maple-app-name: "${mapleAppName}"
    submit-user-group: "${submitUserGroup}"
    submit-user: "${submitUser}"
spec:
  sparkConfigMap:
  hadoopConfigMap:
  type: Scala   # Scala, Java, Python, or R
  mode: cluster
  image:
  sparkVersion: 3.3.2
  mainClass:
  mainApplicationFile:
  imagePullSecrets:
  arguments:
  restartPolicy:
    type: Never
  batchScheduler: volcano
  batchSchedulerOptions:
      priorityClassName:
      queue:
  timeToLiveSeconds: 864000
  sparkConf:
    "spark.sql.files.maxPartitionBytes": "134217728"
    "spark.sql.adaptive.enabled": "true"
    "spark.sql.adaptive.coalescePartitions.enabled": "true"
    "spark.sql.adaptive.coalescePartitions.initialPartitionNum": "10"
    "spark.sql.adaptive.advisoryPartitionSizeInBytes": "67108864"
    "spark.speculation": "true"
    "spark.speculation.multiplier": "2"
    "spark.speculation.quantile": "0.9"
    "spark.speculation.minTaskRuntime": "60s"
    "spark.dynamicAllocation.shuffleTracking.enabled": "true"
    "spark.kryoserializer.buffer.max": "256m"
    "spark.kubernetes.executor.enablePollingWithResourceVersion": "true"
    "spark.kubernetes.executor.apiPollingInterval": "300000"
    "spark.sql.analyzer.failAmbiguousSelfJoin": "false"
    "spark.driver.extraClassPath": ""
    "spark.eventLog.enabled": "true"
    "spark.eventLog.dir": "hdfs://hadoop-cluster/spark/benchmark/logs/"
    "spark.kubernetes.file.upload.path": "local:///tmp/"
    "spark.kubernetes.executor.podNamePrefix": "maple-spark-${mapleId}"
    "spark.ui.proxyBase": "/ui/${namespace}/maple-spark-${mapleId}-ui-svc"
    "spark.ui.proxyRedirectUri": ""
    "spark.ui.port": "8080"
    <#list sparkConfSelf as confSelf>
    ${confSelf}
    </#list>
  driver:
    env:
    <#list envs as key, value>
      - name: ${key}
        value: ${value}
    </#list>
    cores: 2
    coreLimit: "2000m"
    memory: "${driverMemory}"
    javaOptions: ""
    labels:
      from-app: maple-exec
      maple-id: "${mapleId}"
      maple-app-name: "${mapleAppName}"
      submit-user-group: "${submitUserGroup}"
      submit-user: "${submitUser}"
    serviceAccount: spark
    hostAliases:
    volumeMounts:
    annotations:
    tolerations:
  executor:
    cores: ${executorCores}
    coreLimit: "${executorCoreLimit}"
    instances: ${executorNum}
    memory: "${executorMemory}"
    <#if memoryOverhead ??>
    memoryOverhead: "${memoryOverhead}"
    </#if>
    javaOptions: ""
    labels:
      version: 3.3.2
      role: executor
    serviceAccount: spark
    hostAliases:
    volumeMounts:
    annotations:
    tolerations:
