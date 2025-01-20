<#if engine.envs??>
<#list engine.envs as key, value>
export ${key}=${value}
</#list>
</#if>
${engine.engineHome}/bin/spark-submit \
    --master yarn \
    --deploy-mode cluster \
    --queue ${runConf.queue} \
    --name SPARK-${execName}-${execId} \
    --driver-cores ${runConf.driverCores} \
    --driver-memory ${runConf.driverMemory} \
    --num-executors ${runConf.numExecutors} \
    --executor-cores ${runConf.executorCores} \
    --executor-memory ${runConf.executorMemory} \
    <#if runConf.driverJavaOptions?? && (runConf.driverJavaOptions?length > 0)>
    --driver-java-options ${runConf.driverJavaOptions} \
    </#if>
    <#if runConf.driverClassPath?? && (runConf.driverClassPath?length > 0)>
    --driver-class-path ${runConf.driverClassPath} \
    </#if>
    <#if runConf.jars?? && (runConf.jars?length > 0)>
    --jars ${runConf.jars} \
    </#if>
    <#if runConf.files?? && (runConf.files?length > 0)>
    --files ${runConf.files} \
    </#if>
    <#if runConf.archives?? && (runConf.archives?length > 0)>
    --archives ${runConf.archives} \
    </#if>
    --conf spark.yarn.tags=maple-exec,maple-id-${execId} \
<#if runConf.conf??>
    <#list runConf.conf as key, value>
    --conf ${key}=${value} \
    </#list>
</#if>
<#if jobType == "data_calc">
    --class xxx.xxx.xxx maple-spark-data-calc.jar ${execConf.execFile}
<#elseif jobType == "sql">
    --class xxx.xxx.xxx maple-spark-data-calc.jar ${execConf.execFile}
<#elseif jobType == "scala">
    --class xxx.xxx.xxx maple-spark-data-calc.jar ${execConf.execFile}
<#elseif jobType == "py">
    --py-files ${execConf.pyFiles} ${execConf.execFile} ${execConf.args}
<#elseif jobType == "jar">
    --class ${execConf.mainClass} \
    ${execConf.execFile} ${execConf.args}
</#if>

