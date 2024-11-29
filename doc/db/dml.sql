replace into maple.maple_datasource_type (type_code, type_name, icon, classifier, versions, configurations)
values ('mysql', 'MySQL', '', '数据库', '5.6,5.7,8.0',
        '[
          {
            "keyCode": "address",
            "keyName": "地址",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "username",
            "keyName": "用户名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "^[0-9A-Za-z_-]+$",
            "description": ""
          },
          {
            "keyCode": "password",
            "keyName": "密码",
            "dataType": "PASSWORD",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "databaseName",
            "keyName": "数据库名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "driverClassName",
            "keyName": "驱动类名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "com.mysql.jdbc.Driver",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "params",
            "keyName": "连接参数",
            "dataType": "JSON",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "JSON格式: {\\"param\\":\\"value\\"}"
          }
        ]'),
       ('tidb', 'TiDB', '', '数据库', '4,5,6.1,6.5',
        '[
          {
            "keyCode": "address",
            "keyName": "地址",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "host1:port1[,host2:port2...]"
          },
          {
            "keyCode": "username",
            "keyName": "用户名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "^[0-9A-Za-z_-]+$",
            "description": ""
          },
          {
            "keyCode": "password",
            "keyName": "密码",
            "dataType": "PASSWORD",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "databaseName",
            "keyName": "数据库名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "driverClassName",
            "keyName": "驱动类名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "com.mysql.jdbc.Driver",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "params",
            "keyName": "连接参数",
            "dataType": "JSON",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "JSON格式: {\\"param\\":\\"value\\"}"
          }
        ]'),
       ('doris', 'Doris', '', '数据库', '1.1,1.2',
        '[
          {
            "keyCode": "address",
            "keyName": "地址",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "host1:port1[,host2:port2...]"
          },
          {
            "keyCode": "username",
            "keyName": "用户名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "^[0-9A-Za-z_-]+$",
            "description": ""
          },
          {
            "keyCode": "password",
            "keyName": "密码",
            "dataType": "PASSWORD",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "databaseName",
            "keyName": "数据库名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "driverClassName",
            "keyName": "驱动类名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "com.mysql.jdbc.Driver",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "params",
            "keyName": "连接参数",
            "dataType": "JSON",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "JSON格式: {\\"param\\":\\"value\\"}"
          }
        ]'),
       ('clickhouse', 'ClickHouse', '', '数据库', '1.1,18,19,20,21,22',
        '[
          {
            "keyCode": "address",
            "keyName": "地址",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "host1:port1[,host2:port2...]"
          },
          {
            "keyCode": "username",
            "keyName": "用户名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "^[0-9A-Za-z_-]+$",
            "description": ""
          },
          {
            "keyCode": "password",
            "keyName": "密码",
            "dataType": "PASSWORD",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "databaseName",
            "keyName": "数据库名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "driverClassName",
            "keyName": "驱动类名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "ru.yandex.clickhouse.ClickHouseDriver",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "params",
            "keyName": "连接参数",
            "dataType": "JSON",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "JSON格式: {\\"param\\":\\"value\\"}"
          }
        ]'),
       ('postgresql', 'PostgreSQL', '', '数据库', '10,11,12,13,14,15',
        '[
          {
            "keyCode": "address",
            "keyName": "地址",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "host1:port1[,host2:port2...]"
          },
          {
            "keyCode": "username",
            "keyName": "用户名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "^[0-9A-Za-z_-]+$",
            "description": ""
          },
          {
            "keyCode": "password",
            "keyName": "密码",
            "dataType": "PASSWORD",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "databaseName",
            "keyName": "数据库名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "driverClassName",
            "keyName": "驱动类名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "org.postgresql.Driver",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "params",
            "keyName": "连接参数",
            "dataType": "JSON",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "JSON格式: {\\"param\\":\\"value\\"}"
          }
        ]'),
       ('db2', 'DB2', '', '数据库', '9.7,10,11,12,13',
        '[
          {
            "keyCode": "address",
            "keyName": "地址",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "host1:port1[,host2:port2...]"
          },
          {
            "keyCode": "username",
            "keyName": "用户名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "^[0-9A-Za-z_-]+$",
            "description": ""
          },
          {
            "keyCode": "password",
            "keyName": "密码",
            "dataType": "PASSWORD",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "databaseName",
            "keyName": "数据库名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "driverClassName",
            "keyName": "驱动类名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "com.ibm.db2.jcc.DB2Driver",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "params",
            "keyName": "连接参数",
            "dataType": "JSON",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "JSON格式: {\\"param\\":\\"value\\"}"
          }
        ]'),
       ('oracle', 'Oracle', '', '数据库', '9i,10g,11g,12c,18c,19c,21c',
        '[
          {
            "keyCode": "address",
            "keyName": "地址",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "host1:port1[,host2:port2...]"
          },
          {
            "keyCode": "username",
            "keyName": "用户名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "^[0-9A-Za-z_-]+$",
            "description": ""
          },
          {
            "keyCode": "password",
            "keyName": "密码",
            "dataType": "PASSWORD",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "sid",
            "keyName": "SID",
            "dataType": "STRING",
            "versions": "9i,10g",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "serviceName",
            "keyName": "service_name",
            "dataType": "STRING",
            "versions": "11g,12c,18c,19c,21c",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "server",
            "keyName": "server",
            "dataType": "STRING",
            "versions": "11g,12c,18c,19c,21c",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "instance",
            "keyName": "实例名",
            "dataType": "STRING",
            "versions": "11g,12c,18c,19c,21c",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "driverClassName",
            "keyName": "驱动类名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "oracle.jdbc.driver.OracleDriver",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "params",
            "keyName": "连接参数",
            "dataType": "JSON",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "JSON格式: {\\"param\\":\\"value\\"}"
          }
        ]'),
       ('sqlserver', 'SqlServer', '', '数据库', '2016,2017,2019,2022',
        '[
          {
            "keyCode": "address",
            "keyName": "地址",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": "host1:port1[,host2:port2...][\\\\instance]"
          },
          {
            "keyCode": "username",
            "keyName": "用户名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "^[0-9A-Za-z_-]+$",
            "description": ""
          },
          {
            "keyCode": "password",
            "keyName": "密码",
            "dataType": "PASSWORD",
            "versions": "*",
            "defaultValue": "",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "driverClassName",
            "keyName": "驱动类名",
            "dataType": "STRING",
            "versions": "*",
            "defaultValue": "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "nullable": true,
            "valueRegex": "",
            "description": ""
          },
          {
            "keyCode": "params",
            "keyName": "连接参数",
            "dataType": "JSON",
            "versions": "*",
            "defaultValue": "{\\"encrypt\\":\\"true\\",\\"trustServerCertificate\\":\\"true\\"}",
            "nullable": true,
            "valueRegex": "",
            "description": "JSON格式: {\\"param\\":\\"value\\"}"
          }
        ]');

replace into maple.maple_datasource (id, name, description, datasource_type, version, datasource_conf)
values (1, 'test_mysql', '测试mysql', 'mysql', '5.7',
        '{
          "driverClassName": "com.mysql.jdbc.Driver",
          "address": "localhost:3306",
          "username": "xi_root",
          "password": "123456",
          "databaseName": "test_mysql_db"
        }'),
       (2, 'test_postgresql', '测试postgresql', 'postgresql', '14',
        '{
          "driverClassName": "org.postgresql.Driver",
          "address": "localhost:5432",
          "username": "xi_root",
          "password": "123456",
          "databaseName": "test_postgresql_db"
        }'),
       (3, 'test_oracle', '测试oracle', 'oracle', '12c',
        '{
          "driverClassName": "oracle.jdbc.driver.OracleDriver",
          "address": "localhost:1521",
          "username": "xi_root",
          "password": "123456",
          "serviceName": "orcl"
        }'),
       (4, 'test_sqlserver', '测试sqlserver', 'sqlserver', '2019',
        '{
          "driverClassName": "com.microsoft.sqlserver.jdbc.SQLServerDriver",
          "address": "localhost:1433",
          "params": "{\\"encrypt\\":\\"true\\",\\"trustServerCertificate\\":\\"true\\"}",
          "username": "xi_root",
          "password": "123456",
          "databaseName": "test_sqlserver_db"
        }');


insert into maple.maple_cluster (name, category, address, `desc`, configuration)
values ('hadoop-default', 'YARN', 'localhost:8080', '测试集群', '{}'),
       ('k8s-default', 'K8s', 'localhost:6443', '测试集群2', '{}');

insert into maple.maple_cluster_engine (id, cluster, name, version, engine_home, ext_info)
values (1, 'hadoop-default', 'spark', '3.3.2', '/opt/spark/current', '{
  "envs": {
    "HADOOP_HOME": "/opt/hadoop/current",
    "HADOOP_CONF_DIR": "/opt/hadoop/current/etc/hadoop/",
    "YARN_CONF_DIR": "/opt/hadoop/current/etc/hadoop/"
  },
  "forbiddenConfs": [
    {
      "name": "spark.yarn.queue",
      "replaceParameter": "--queue",
      "desc": "YARN 队列"
    },
    {
      "name": "spark.driver.cores",
      "replaceParameter": "--driver-cores",
      "desc": "Spark driver vcores"
    },
    {
      "name": "spark.driver.memory",
      "replaceParameter": "--driver-memory",
      "desc": "Spark driver 内存"
    },
    {
      "name": "spark.executor.instances",
      "replaceParameter": "--num-executors",
      "desc": "Spark executor 内存"
    },
    {
      "name": "spark.executor.cores",
      "replaceParameter": "--executor-cores",
      "desc": "Spark executor vcores"
    },
    {
      "name": "spark.executor.memory",
      "replaceParameter": "--executor-memory",
      "desc": "Spark executor 内存"
    },
    {
      "name": "spark.driver.extraJavaOptions",
      "replaceParameter": "--driver-java-options",
      "desc": "Spark driver java 启动参数"
    },
    {
      "name": "spark.driver.extraLibraryPath",
      "replaceParameter": "--driver-library-path",
      "desc": "Spark driver java 启动参数"
    },
    {
      "name": "spark.jars",
      "replaceParameter": "--jars",
      "desc": "以逗号分隔的 jars 列表，包含在 driver 和 executor 的类路径中"
    }
  ]
}');


insert into maple.maple_cluster_engine (id, cluster, name, version, engine_home, ext_info)
values (2, 'k8s-default', 'spark', '3.3.2', '/opt/spark/current', '{

}');


insert into maple.maple_cluster_engine (id, cluster, name, version, engine_home, ext_info)
values (3, 'hadoop-default', 'flink', '1.16', '/opt/flink/current', '{
  "envs": {
    "HADOOP_CLASSPATH": "/opt/hadoop/current/etc/hadoop:/opt/hadoop/current/share/hadoop/common/lib/*:/opt/hadoop/current/share/hadoop/common/*:/opt/hadoop/current/share/hadoop/hdfs:/opt/hadoop/current/share/hadoop/hdfs/lib/*:/opt/hadoop/current/share/hadoop/hdfs/*:/opt/hadoop/current/share/hadoop/mapreduce/*:/opt/hadoop/current/share/hadoop/yarn:/opt/hadoop/current/share/hadoop/yarn/lib/*:/opt/hadoop/current/share/hadoop/yarn/*"
  },
  "forbiddenConfs": [
  ]
}');


insert into maple.maple_cluster_engine (id, cluster, name, version, engine_home, ext_info)
values (4, 'k8s-default', 'flink', '1.16', '/opt/flink/current', '{
  "envs": {
  },
  "forbiddenConfs": [
  ]
}');
