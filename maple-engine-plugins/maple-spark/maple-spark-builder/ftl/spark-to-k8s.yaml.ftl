<#list envs?keys as key>
export ${key}=${envs[key]}
</#list>