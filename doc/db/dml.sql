replace into maple.maple_datasource_type (type_code, type_name, icon, classifier, versions)
values ('clickhouse', 'ClickHouse', '', '数据库', '1.1,18,19,20,21,22'),
       ('db2', 'DB2', '', '数据库', '9.7,10,11,12,13'),
       ('doris', 'Doris', '', '数据库', '1.1,1.2'),
       ('mysql', 'MySQL', '', '数据库', '5.6,5.7,8.0'),
       ('oracle', 'Oracle', '', '数据库', '9i,10g,11g,12c,18c,19c,21c'),
       ('postgresql', 'PostgreSQL', '', '数据库', '10,11,12,13,14,15'),
       ('sqlserver', 'SqlServer', '', '数据库', '2016,2017,2019,2022'),
       ('tidb', 'TiDB', '', '数据库', '4,5,6.1,6.5');

insert into maple.maple_datasource_config_key (datasource_type, versions, key_code, key_name, key_order, default_value,
                                               value_type, required, value_regex, description)
values ('mysql', '*', 'driverClassName', '驱动类名', 1, 'com.mysql.jdbc.Driver', 'STRING', 0, '', ''),
       ('mysql', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('mysql', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('mysql', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('mysql', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('mysql', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', ''),
       ('tidb', '*', 'driverClassName', '驱动类名', 1, 'com.mysql.jdbc.Driver', 'STRING', 0, '', ''),
       ('tidb', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('tidb', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('tidb', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('tidb', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('tidb', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', ''),
       ('doris', '*', 'driverClassName', '驱动类名', 1, 'com.mysql.jdbc.Driver', 'STRING', 0, '', ''),
       ('doris', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('doris', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('doris', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('doris', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('doris', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', ''),
       ('clickhouse', '*', 'driverClassName', '驱动类名', 1, 'ru.yandex.clickhouse.ClickHouseDriver', 'STRING', 0, '',
        ''),
       ('clickhouse', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('clickhouse', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('clickhouse', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('clickhouse', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('clickhouse', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', ''),
       ('postgresql', '*', 'driverClassName', '驱动类名', 1, 'org.postgresql.Driver', 'STRING', 0, '', ''),
       ('postgresql', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('postgresql', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('postgresql', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('postgresql', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('postgresql', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', ''),
       ('db2', '*', 'driverClassName', '驱动类名', 1, 'com.ibm.db2.jcc.DB2Driver', 'STRING', 0, '', ''),
       ('db2', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('db2', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('db2', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('db2', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('db2', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', ''),
       ('oracle', '*', 'driverClassName', '驱动类名', 1, 'oracle.jdbc.driver.OracleDriver', 'STRING', 0, '', ''),
       ('oracle', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('oracle', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('oracle', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('oracle', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('oracle', '9i,10g', 'sid', 'SID', 6, '', 'STRING', 0, '', ''),
       ('oracle', '11g,12c,18c,19c,21c', 'serviceName', 'service_name', 7, '', 'STRING', 0, '', ''),
       ('oracle', '11g,12c,18c,19c,21c', 'server', 'server', 8, '', 'STRING', 0, '', ''),
       ('oracle', '11g,12c,18c,19c,21c', 'instance', '实例名', 9, '', 'STRING', 0, '', ''),
       ('sqlserver', '*', 'driverClassName', '驱动类名', 1, 'com.microsoft.sqlserver.jdbc.SQLServerDriver', 'STRING', 0,
        '', ''),
       ('sqlserver', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...][\\instance]'),
       ('sqlserver', '*', 'params', '连接参数', 3, '{"encrypt":"true","trustServerCertificate":"true"}', 'JSON', 0, '',
        'JSON格式: {"param":"value"}'),
       ('sqlserver', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('sqlserver', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('sqlserver', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', '');

insert into maple.maple_datasource (id, name, description, datasource_type, version, datasource_config)
values (1, 'test_mysql', '测试mysql', 'mysql', '5.7',
        '{"driverClassName":"com.mysql.jdbc.Driver","address":"localhost:3306","username":"xi_root","password":"123456","databaseName":"test_mysql_db"}'),
       (2, 'test_postgresql', '测试postgresql', 'postgresql', '14',
        '{"driverClassName":"org.postgresql.Driver","address":"localhost:5432","username":"xi_root","password":"123456","databaseName":"test_postgresql_db"}'),
       (3, 'test_oracle', '测试oracle', 'oracle', '12c',
        '{"driverClassName":"oracle.jdbc.driver.OracleDriver","address":"localhost:1521","username":"xi_root","password":"123456","serviceName":"orcl"}'),
       (4, 'test_sqlserver', '测试sqlserver', 'sqlserver', '2019',
        '{"driverClassName":"com.microsoft.sqlserver.jdbc.SQLServerDriver","address":"localhost:1433","params":"{\\"encrypt\\":\\"true\\",\\"trustServerCertificate\\":\\"true\\"}","username":"xi_root","password":"123456","databaseName":"test_sqlserver_db"}');


insert into maple.maple_cluster (name, category, address, `desc`, configuration, deleted, create_user, update_user)
values ('hadoop-default', 'YARN', 'localhost:8080', '测试集群', '{}', 0, 'xi', 'xi'),
       ('k8s-default', 'K8s', 'localhost:6443', '测试集群2', '{}', 0, 'xi', 'xi');

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
    "HADOOP_CLASSPATH": "/opt/hadoop/current/etc/hadoop:/opt/hadoop/current/share/hadoop/common/lib/*:/opt/hadoop/current/share/hadoop/common/*:/opt/hadoop/current/share/hadoop/hdfs:/opt/hadoop/current/share/hadoop/hdfs/lib/*:/opt/hadoop/current/share/hadoop/hdfs/*:/opt/hadoop/current/share/hadoop/mapreduce/*:/opt/hadoop/current/share/hadoop/yarn:/opt/hadoop/current/share/hadoop/yarn/lib/*:/opt/hadoop/current/share/hadoop/yarn/*",
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
