#!/bin/bash
<#if engine.envs??>
<#list engine.envs as key, value>
export ${key}=${value}
</#list>
</#if>
${engine.engineHome}/bin/flink run-application \
    --target yarn-application \
    -Dyarn.application.name=FLINK-${execName}-${execId} \
    -Dyarn.application.queue=${resourceGroup.queue} \
    -Dyarn.appmaster.vcores=${runConf.jobManagerCores} \
    -Djobmanager.memory.process.size=${runConf.jobManagerMemory} \
    -Dyarn.containers.vcores=${runConf.taskManagerCores} \
    -Dtaskmanager.numberOfTaskSlots=${runConf.numberOfTaskSlots} \
    -Dtaskmanager.memory.process.size=${runConf.taskManagerMemory} \
    <#-- --classpath ${runConf.classpath} \ -->
    --detached \
    --allowNonRestoredState \
    --parallelism ${runConf.parallelism} \
    <#-- --restoreMode ${runConf.restoreMode} \ -->
    <#-- --fromSavepoint ${runConf.fromSavepoint} \ -->
    <#-- -Dyarn.provided.lib.dirs="${runConf.yarnFlinkLib}" \ -->
    -Dyarn.tags="maple-exec,maple-id-${execId}" \
<#if conf??>
<#list conf as key, value>
    -D${key}=${value} \
</#list>
</#if>
<#if jobType == "flink-jar">
    --class ${execConf.mainClass} \
    ${execConf.execFile} ${execConf.args}
<#elseif jobType == "flink-py">
    --python ${execConf.python} \
    --pyArchives ${execConf.pyArchives} \
    --pyClientExecutable ${execConf.pyClientExecutable} \
    --pyExecutable ${execConf.pyExecutable} \
    --pyFiles ${execConf.pyFiles} \
    --pyRequirements ${execConf.pyRequirements} \
    --pyModule ${execConf.pyModule} \
</#if>
