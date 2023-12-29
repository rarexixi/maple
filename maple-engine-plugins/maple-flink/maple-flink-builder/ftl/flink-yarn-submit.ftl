<#list envs?keys as key>
export ${key}=${envs[key]}
</#list>
# ./bin/sql-client.sh -f /path/to/your/sql-queries.sql

${flinkHome}/bin/flink run-application \
    --classpath ${classpath} \
    --detached \
    --allowNonRestoredState \
    --parallelism ${parallelism} \
    --restoreMode ${restoreMode} \
    --fromSavepoint ${fromSavepoint} \
    --target yarn-application \
    -Dyarn.provided.lib.dirs="${yarnFlinkLib}" \
    -Dyarn.tags="maple-exec,maple-id-${mapleId}" \
    -Dyarn.application.name="${applicationName}" \
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
