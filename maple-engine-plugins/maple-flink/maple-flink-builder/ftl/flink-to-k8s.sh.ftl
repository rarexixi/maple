<#list envs?keys as key>
    export ${key}=${envs[key]}
</#list>
kubectl apply -f flink-sbumit-to-k8s.yaml -n ${namespace}
