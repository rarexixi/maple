
${sparkHome}/bin/spark-submit \
<#if params??>
<#list params?keys as key>
    --${key} ${params[key]}
</#list>
</#if>
<#if conf??>
<#list conf?keys as key>
    --conf ${key}=${conf[key]}
</#list>
</#if>
