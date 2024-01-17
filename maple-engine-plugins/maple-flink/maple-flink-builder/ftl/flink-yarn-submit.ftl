<#if engine.envs??>
<#list engine.envs as key, value>
export ${key}=${value}
</#list>
</#if>
${engine.engineHome}/bin/flink run-application \
    --target yarn-application \
    --yarnqueue ${job.yarnQueue} \           # yarn.application.queue
    --yarnname FLINK-${execName}-${execId} \ # yarn.application.name
    -Dyarn.appmaster.vcores=${job.jobManagerCores} \
    -Djobmanager.memory.process.size=${job.jobManagerMemory} \
    -Dyarn.containers.vcores=${job.taskManagerCores} \
    -Dtaskmanager.numberOfTaskSlots=${job.numberOfTaskSlots} \
    -Dtaskmanager.memory.process.size=${job.taskManagerMemory} \
    --classpath ${job.classpath} \
    --detached \
    --allowNonRestoredState \
    --parallelism ${job.parallelism} \
    --restoreMode ${job.restoreMode} \
    --fromSavepoint ${job.fromSavepoint} \
    -Dyarn.provided.lib.dirs="${job.yarnFlinkLib}" \
    -Dyarn.tags="maple-exec,maple-id-${execId}" \
<#if conf??>
<#list conf as key, value>
    -D${key}=${value} \
</#list>
</#if>
<#if job.runType == "jar">
    --class ${job.runConf.mainClass} \
    ${execFile} ${job.runConf.args}
<#elseif job.runType == "py">
    --python ${job.runConf.python} \
    --pyArchives ${job.runConf.pyArchives} \
    --pyClientExecutable ${job.runConf.pyClientExecutable} \
    --pyExecutable ${job.runConf.pyExecutable} \
    --pyFiles ${job.runConf.pyFiles} \
    --pyRequirements ${job.runConf.pyRequirements} \
    --pyModule ${job.runConf.pyModule} \
</#if>
