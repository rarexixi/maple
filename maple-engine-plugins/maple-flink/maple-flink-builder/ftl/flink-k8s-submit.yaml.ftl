apiVersion: flink.apache.org/v1beta1
kind: FlinkDeployment
metadata:
  name: ${deploymentName}
  namespace: ${k8sNamespace}
  labels:
    from-app: maple-exec
    maple-id: "${mapleId}"
    maple-app-name: "${mapleAppName}"
    submit-user-group: "${submitUserGroup}"
    submit-user: "${submitUser}"
spec:
  image: ${specImage}
  flinkVersion: ${flinkVersion}
  podTemplate:
    metadata:
      labels:
        from-app: maple-exec
        maple-id: "${mapleId}"
        maple-app-name: "${mapleAppName}"
        submit-user-group: "${submitUserGroup}"
        submit-user: "${submitUser}"
    spec:
      enableServiceLinks: false
      affinity:
      tolerations:
      volumes:
      initContainers:
      containers:
        - name: flink-main-container
          env:
          <#list envs as key, value>
            - name: ${key}
              value: ${value}
          </#list>
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
    taskmanager.numberOfTaskSlots: "${taskmanager.numberOfTaskSlots}"
    state.checkpoints.dir: hdfs://hadoop-cluster/flink-data/checkpoints/maple/${mapleId}
    state.savepoints.dir: hdfs://hadoop-cluster/flink-data/savepoints/maple/${mapleId}
    jobmanager.archive.fs.dir: hdfs://hadoop-cluster/flink-data/completed-jobs
    high-availability: kubernetes
    high-availability.storageDir: hdfs://hadoop-cluster/flink-data/ha # 这里需要注意权限
    <#list job.extConf?keys as key>
    <#if job.extConf[key]?is_boolean>
    ${key}: "${job.extConf[key]?then('true', 'false')}"
    <#else>
    ${key}: "${job.extConf[key]}"
    </#if>
    </#list>
  serviceAccount: flink
  imagePullPolicy: IfNotPresent
  jobManager:
    replicas: ${jobmanager.replicas}
    resource:
      memory: "${jobmanager.memory}G"
      cpu: ${jobmanager.cpu/jmCpuLimitFactor}
  taskManager:
    resource:
      memory: "${taskmanager.memory}G"
      cpu: ${taskmanager.cpu/tmCpuLimitFactor}
<#if job.runType="sql">
    jarURI: local:///opt/flink/extlib/flink-sql-exec.jar
    entryClass: org.maple.
    args:
    - "-sql"
    - "/opt/flink/extlib${job.sqlPath}"
    - "-jobName"
    - "${job.jobName}"
    <#if (udfList?? && udfList?size > 0)>
    - "-udf"
    - "[<#list udfList as udf><#if (udf?index > 0)>,</#if>{\"functionName\": \"${udf.functionName}\",\"className\": \"${udf.className?replace("'", "")}\"}</#list>]"
    </#if>
    savepointTriggerNonce: 0
    allowNonRestoredState: true
    state: running
    parallelism: ${parallelism}
<#elseif job.runType="py">
    jarURI: local:///opt/flink/opt/flink-python-1.16.2.jar
    entryClass: "org.apache.flink.client.python.PythonDriver"
    args:
    - "-pyclientexec"
    - "/usr/local/bin/python3"
    <#if job.pyfs != "">
    - "-pyfs"
    - "/opt/flink/extlib${job.pyfs}"
    </#if>
    <#if job.pym != "">
    - "-pym"
    - "${job.pym}"
    </#if>
    <#if (job.args?? && job.args?size > 0)>
    <#list job.args as arg>
    - "${arg}"
    </#list>
    </#if>
    parallelism: ${parallelism}
<#elseif job.runType="jar">
    jarURI: local:///opt/flink/extlib${job.jarURI}
    entryClass: "${job.mainClass}"
    <#if (job.args?? && job.args?size > 0)>
    args:
    <#list job.args as arg>
    - "${arg}"
    </#list>
    </#if>
    parallelism: ${parallelism}
</#if>
    <#if (checkpoint?? && ((checkpoint?trim) != ""))>
    initialSavepointPath: "${checkpoint}"
    </#if>
    upgradeMode: savepoint
