<#list envs?keys as key>
export ${key}=${envs[key]}
</#list>

${flinkHome}/bin/flink cancel \
  -t yarn-application \
  -Dyarn.application.id=${applicationId} <jobId>
