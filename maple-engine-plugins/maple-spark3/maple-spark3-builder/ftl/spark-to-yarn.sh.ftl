<#list envs?keys as key>
export ${key}=${envs[key]}
</#list>

${sparkHome}/bin/spark-submit \
<#if master?? && (master?length > 0)>
  --master ${master} \
</#if>
<#if deployMode?? && (deployMode?length > 0)>
  --deploy-mode ${deployMode} \
</#if>
<#if mainClass?? && (mainClass?length > 0)>
  --class ${mainClass} \
</#if>
<#if name?? && (name?length > 0)>
  --name ${name} \
</#if>
<#if jars?? && (jars?length > 0)>
  --jars ${jars} \
</#if>
<#if packages?? && (packages?length > 0)>
  --packages ${packages} \
</#if>
<#if excludePackages?? && (excludePackages?length > 0)>
  --exclude-packages ${excludePackages} \
</#if>
<#if repositories?? && (repositories?length > 0)>
  --repositories ${repositories} \
</#if>
<#if pyFiles?? && (pyFiles?length > 0)>
  --py-files ${pyFiles} \
</#if>
<#if files?? && (files?length > 0)>
  --files ${files} \
</#if>
<#if archives?? && (archives?length > 0)>
  --archives ${archives} \
</#if>
<#if conf??>
<#list conf?keys as key>
    --conf ${key}=${conf[key]}
</#list>
</#if>
<#if propertiesFile?? && (propertiesFile?length > 0)>
  --properties-file ${propertiesFile} \
</#if>
<#if driverCores?? && (driverCores?length > 0)>
  --driver-cores ${driverCores} \
</#if>
<#if driverMemory?? && (driverMemory?length > 0)>
  --driver-memory ${driverMemory} \
</#if>
<#if driverJavaOptions?? && (driverJavaOptions?length > 0)>
  --driver-java-options ${driverJavaOptions} \
</#if>
<#if driverLibraryPath?? && (driverLibraryPath?length > 0)>
  --driver-library-path ${driverLibraryPath} \
</#if>
<#if driverClassPath?? && (driverClassPath?length > 0)>
  --driver-class-path ${driverClassPath} \
</#if>
<#if numExecutors?? && (numExecutors?length > 0)>
  --num-executors ${numExecutors} \
</#if>
<#if executorCores?? && (executorCores?length > 0)>
  --executor-cores ${executorCores} \
</#if>
<#if executorMemory?? && (executorMemory?length > 0)>
  --executor-memory ${executorMemory} \
</#if>
<#if proxyUser?? && (proxyUser?length > 0)>
  --proxy-user ${proxyUser} \
</#if>
<#if principal?? && (principal?length > 0)>
  --principal ${principal} \
</#if>
<#if keytab?? && (keytab?length > 0)>
  --keytab ${keytab} \
</#if>
<#if queue?? && (queue?length > 0)>
  --queue ${queue} \
</#if>

