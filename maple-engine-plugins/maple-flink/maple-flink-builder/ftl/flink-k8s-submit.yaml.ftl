apiVersion: flink.apache.org/v1beta1
kind: FlinkDeployment
metadata:
  name: FLINK-${execName}-${execId}
  namespace: ${job.namespace}
  labels:
    from-app: maple-exec
    maple-id: "${execId}"
    maple-app-name: "${execName}"
    submit-user-group: "${group}"
    submit-user: "${user}"
spec:
  image: ${engine.image}
  flinkVersion: ${engine.version}
  podTemplate:
    metadata:
      labels:
        from-app: maple-exec
        maple-id: "${execId}"
        maple-app-name: "${execName}"
        submit-user-group: "${group}"
        submit-user: "${user}"
    spec:
      enableServiceLinks: false
      affinity:
      tolerations:
      volumes:
      initContainers:
      containers:
        - name: flink-main-container
          <#if engine.envs??>
          env:
          <#list envs as key, value>
            - name: ${key}
              value: ${value}
          </#list>
          </#if>
          lifecycle:
            preStop:
              exec:
                command:
      hostAliases:
        - hostnames:
            - "hadoop-master"
          ip: 127.0.0.1
      imagePullSecrets:
  flinkConfiguration:
    rest.bind-port: "8080"
    rest.port: "8080"
    $internal.pipeline.job-id:
    pipeline.name:
    pipeline.classpaths: "${pipelineClasspaths}"
    taskmanager.numberOfTaskSlots: "${job.numberOfTaskSlots}"
    state.checkpoints.dir: hdfs://hadoop-cluster/flink-data/checkpoints/maple/${execId}
    state.savepoints.dir: hdfs://hadoop-cluster/flink-data/savepoints/maple/${execId}
    jobmanager.archive.fs.dir: hdfs://hadoop-cluster/flink-data/completed-jobs
    high-availability: kubernetes
    high-availability.storageDir: hdfs://hadoop-cluster/flink-data/ha # 这里需要注意权限
    <#list job.conf as key, value>
    <#if value?is_boolean>
    ${key}: "${value?then('true', 'false')}"
    <#else>
    ${key}: "${value}"
    </#if>
    </#list>
  serviceAccount: flink
  imagePullPolicy: IfNotPresent
  jobManager:
    replicas: ${job.jobManagerReplicas}
    resource:
      memory: "${job.jobManagerMemory}"
      cpu: ${job.jobManagerCores}
  taskManager:
    resource:
      memory: "${job.taskManagerMemory}"
      cpu: ${job.taskManagerCores}
<#if job.runType="sql">
    jarURI: local://${engine.engineHome}/extlib/flink-sql-exec.jar
    entryClass: org.maple.
    args:
    - "-sql"
    - "${engine.engineHome}/extlib${execFile}"
    - "-jobName"
    - "${execName}"
    savepointTriggerNonce: 0
    allowNonRestoredState: true
    state: running
<#elseif job.runType="py">
    jarURI: local://${engine.engineHome}/opt/flink-python-1.16.2.jar
    entryClass: "org.apache.flink.client.python.PythonDriver"
    args:
    - "-pyclientexec"
    - "/usr/local/bin/python3"
    <#if job.runConf.pyFiles != "">
    - "-pyfs"
    - "/opt/flink/extlib${job.runConf.pyFiles}"
    </#if>
    <#if job.runConf.pyModule != "">
    - "-pym"
    - "${job.runConf.pyModule}"
    </#if>
    <#if (job.runConf.args?? && job.runConf.args?size > 0)>
    <#list job.runConf.args as arg>
    - "${arg}"
    </#list>
    </#if>
<#elseif job.runType="jar">
    jarURI: local://${engine.engineHome}/extlib${execFile}
    entryClass: "${job.runConf.mainClass}"
    <#if (job.runConf.args?? && job.runConf.args?size > 0)>
    args:
    <#list job.runConf.args as arg>
    - "${arg}"
    </#list>
    </#if>
</#if>
    parallelism: ${job.parallelism}
    <#if (job.runConf.checkpoint?? && ((job.runConf.checkpoint?trim) != ""))>
    initialSavepointPath: "${job.runConf.checkpoint}"
    </#if>
    upgradeMode: savepoint
