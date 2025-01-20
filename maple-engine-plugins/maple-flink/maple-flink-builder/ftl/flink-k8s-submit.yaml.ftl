apiVersion: flink.apache.org/v1beta1
kind: FlinkDeployment
metadata:
  name: FLINK-${execName}-${execId}
  namespace: ${runConf.namespace}
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
    taskmanager.numberOfTaskSlots: "${runConf.numberOfTaskSlots}"
    state.checkpoints.dir: hdfs://hadoop-cluster/flink-data/checkpoints/maple/${execId}
    state.savepoints.dir: hdfs://hadoop-cluster/flink-data/savepoints/maple/${execId}
    jobmanager.archive.fs.dir: hdfs://hadoop-cluster/flink-data/completed-jobs
    high-availability: kubernetes
    high-availability.storageDir: hdfs://hadoop-cluster/flink-data/ha # 这里需要注意权限
    <#list runConf.conf as key, value>
    <#if value?is_boolean>
    ${key}: "${value?then('true', 'false')}"
    <#else>
    ${key}: "${value}"
    </#if>
    </#list>
  serviceAccount: flink
  imagePullPolicy: IfNotPresent
  jobManager:
    replicas: ${runConf.jobManagerReplicas}
    resource:
      memory: "${runConf.jobManagerMemory}"
      cpu: ${runConf.jobManagerCores}
  taskManager:
    resource:
      memory: "${runConf.taskManagerMemory}"
      cpu: ${runConf.taskManagerCores}
<#if jobType="sql">
    jarURI: local://${engine.engineHome}/extlib/flink-sql-exec.jar
    entryClass: org.maple.
    args:
    - "-sql"
    - "${engine.engineHome}/extlib${execConf.execFile}"
    - "-jobName"
    - "${execName}"
    savepointTriggerNonce: 0
    allowNonRestoredState: true
    state: running
<#elseif jobType="py">
    jarURI: local://${engine.engineHome}/opt/flink-python-1.16.2.jar
    entryClass: "org.apache.flink.client.python.PythonDriver"
    args:
    - "-pyclientexec"
    - "/usr/local/bin/python3"
    <#if execConf.pyFiles != "">
    - "-pyfs"
    - "/opt/flink/extlib${execConf.pyFiles}"
    </#if>
    <#if execConf.pyModule != "">
    - "-pym"
    - "${execConf.pyModule}"
    </#if>
    <#if (execConf.args?? && execConf.args?size > 0)>
    <#list execConf.args as arg>
    - "${arg}"
    </#list>
    </#if>
<#elseif jobType="jar">
    jarURI: local://${engine.engineHome}/extlib${execConf.execFile}
    entryClass: "${execConf.mainClass}"
    <#if (execConf.args?? && execConf.args?size > 0)>
    args:
    <#list execConf.args as arg>
    - "${arg}"
    </#list>
    </#if>
</#if>
    parallelism: ${runConf.parallelism}
    <#if (execConf.checkpoint?? && ((execConf.checkpoint?trim) != ""))>
    initialSavepointPath: "${execConf.checkpoint}"
    </#if>
    upgradeMode: savepoint
