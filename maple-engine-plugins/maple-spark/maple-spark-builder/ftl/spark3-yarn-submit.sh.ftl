<#if engine.envs??>
<#list engine.envs as key, value>
export ${key}=${value}
</#list>
</#if>
${engine.engineHome}/bin/spark-submit \
    --master yarn \
    --deploy-mode cluster \
    --queue ${job.queue} \
    --name SPARK-${execName}-${execId} \
    --driver-cores ${job.driverCores} \
    --driver-memory ${job.driverMemory} \
    --num-executors ${job.numExecutors} \
    --executor-cores ${job.executorCores} \
    --executor-memory ${job.executorMemory} \
    <#if job.driverJavaOptions?? && (job.driverJavaOptions?length > 0)>
    --driver-java-options ${job.driverJavaOptions} \
    </#if>
    <#if job.driverClassPath?? && (job.driverClassPath?length > 0)>
    --driver-class-path ${job.driverClassPath} \
    </#if>
    <#if job.jars?? && (job.jars?length > 0)>
    --jars ${job.jars} \
    </#if>
    <#if job.files?? && (job.files?length > 0)>
    --files ${job.files} \
    </#if>
    <#if job.archives?? && (job.archives?length > 0)>
    --archives ${job.archives} \
    </#if>
    --conf spark.yarn.tags=maple-exec,maple-id-${execId} \
<#if job.conf??>
    <#list job.conf as key, value>
    --conf ${key}=${value} \
    </#list>
</#if>
<#if job.runType == "data_calc">
    --class xxx.xxx.xxx maple-spark-data-calc.jar ${execFile}
<#elseif job.runType == "sql">
    --class xxx.xxx.xxx maple-spark-data-calc.jar ${execFile}
<#elseif job.runType == "scala">
    --class xxx.xxx.xxx maple-spark-data-calc.jar ${execFile}
<#elseif job.runType == "py">
    --py-files ${job.runConf.pyFiles} ${execFile} ${job.runConf.args}
<#elseif job.runType == "jar">
    --class ${job.runConf.mainClass} \
    ${execFile} ${job.runConf.args}
</#if>

