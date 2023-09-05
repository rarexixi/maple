<#list envs?keys as key>
export ${key}=${envs[key]}
</#list>
kubectl apply -f spark-sbumit-to-k8s.yaml -n ${namespace}
