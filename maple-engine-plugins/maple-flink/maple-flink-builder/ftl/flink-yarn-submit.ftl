<#list envs?keys as key>
export ${key}=${envs[key]}
</#list>
# ./bin/sql-client.sh -f /path/to/your/sql-queries.sql

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
    --parallelism ${parallelism} \
    --restoreMode ${restoreMode} \
    --fromSavepoint ${fromSavepoint} \
    -Dyarn.provided.lib.dirs="${yarnFlinkLib}" \
    -Dyarn.tags="maple-exec,maple-id-${mapleId}" \
<#if conf??>
<#list conf as key, value>
    -D${key}=${value} \
</#list>
</#if>
<#if runType == "jar">
    --class ${mainClass} \
    ${jarFile} ${args}
<#elseif runType == "py">
    --python ${python} \
    --pyArchives ${pyArchives} \
    --pyClientExecutable ${pyClientExecutable} \
    --pyExecutable ${pyExecutable} \
    --pyFiles ${pyFiles} \
    --pyRequirements ${pyRequirements} \
    --pyModule ${pyModule} \
</#if>
