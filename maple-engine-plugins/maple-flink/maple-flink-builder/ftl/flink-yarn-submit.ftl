<#list envs?keys as key>
export ${key}=${envs[key]}
</#list>

./bin/sql-client.sh -f /path/to/your/sql-queries.sql

${flinkHome}/bin/flink run-application \
  -t yarn-application \
  -Dyarn.provided.lib.dirs="${yarnFlinkLib}" \
  hdfs://myhdfs/jars/my-application.jar
yarn.tags
