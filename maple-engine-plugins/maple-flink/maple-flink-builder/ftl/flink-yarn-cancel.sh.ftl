#!/bin/bash
<#if engine.envs??>
<#list engine.envs as key, value>
export ${key}=${value}
</#list>
</#if>
JOB_ID=$(flink list -t yarn-application -Dyarn.application.id=${execInfo.APPLICATION_ID} | grep RUNNING | tr -s ' ' | cut -d ' ' -f 4)
${engine.engineHome}/bin/flink cancel \
  -t yarn-application \
  -Dyarn.application.id=${execInfo.APPLICATION_ID} \
  $JOB_ID