
const Databases: Array<any> = [{
    "databaseName": "ods",
}, {
    "databaseName": "dw",
}, {
    "databaseName": "dm",
}]

const SampleConfig: any = {
    "variables": {
        "dt": "2021-08-01"
    },
    "sources": [
        {
            "name": "managed_jdbc",
            "config": {
                "resultTable": "source_table_01",
                "persist": true,
                "storageLevel": 'MEMORY_AND_DISK',
                "options": {
                    "connectionCollation": "utf8mb4_unicode_ci"
                },
                "datasource": "test_mysql",
                "query": "select * from test_mysql_db.test_tb where id > 0"
            }
        },
        {
            "name": "jdbc",
            "config": {
                "resultTable": "source_table_02",
                "persist": false,
                "storageLevel": 'MEMORY_AND_DISK',
                "options": {
                    "connectionCollation": "utf8mb4_unicode_ci"
                },
                "url": "jdbc:mysql://localhost:3306/maple_datasource?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&rewriteBatchedStatements=true&useServerPrepStmts=true&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai",
                "driver": "com.mysql.cj.jdbc.Driver",
                "user": "xi_root",
                "password": "123456",
                "query": "select * from test_mysql_db.test_tb where id > 0"
            }
        },
        {
            "name": "file",
            "config": {
                "resultTable": "source_table_03",
                "persist": false,
                "storageLevel": 'MEMORY_AND_DISK',
                "options": {},
                "path": "hdfs:///data/tmp_file/${dt}",
                "serializer": "csv",
                "columnNames": ["id", "name", "address"]
            }
        }
    ],
    "transformations": [
        {
            "name": "sql",
            "config": {
                "sourceTable": "",
                "resultTable": "transform_table_01",
                "persist": false,
                "storageLevel": 'MEMORY_AND_DISK',
                "sql": "select st03.name, st03.address, dtt.test\nfrom source_table_03 st03 join dw.test_tb dtt on st03.id=dtt.tid",
            }
        }
    ],
    "sinks": [
        {
            "name": "hive",
            "config": {
                "sourceTable": "transform_table_01",
                "sourceQuery": "",
                "numPartitions": 10,
                "options": {},
                "targetDatabase": "dm",
                "targetTable": "dm_test_table",
                "saveMode": "overwrite",
                "strongCheck": false,
                "writeAsFile": false
            }
        },
        {
            "name": "jdbc",
            "config": {
                "sourceTable": "source_table_01",
                "sourceQuery": "",
                "numPartitions": 0,
                "options": {
                    "connectionCollation": "utf8mb4_unicode_ci",
                    "isolationLevel": "NONE",
                    "batchsize": "5000"
                },
                "url": "jdbc:mysql://localhost:3306/maple_datasource?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&rewriteBatchedStatements=true&useServerPrepStmts=true&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai",
                "driver": "com.mysql.cj.jdbc.Driver",
                "user": "xi_root",
                "password": "123456",
                "targetDatabase": "test_mysql_db",
                "targetTable": "test_mysql_db_table_0001",
                "saveMode": "overwrite",
                "preQueries": [
                    "delete from test_mysql_db.test_mysql_db_table_0001"
                ]
            }
        },
        {
            "name": "managed_jdbc",
            "config": {
                "sourceTable": "",
                "sourceQuery": "select * from source_table_01",
                "numPartitions": 0,
                "options": {
                },
                "targetDatasource": "test_postgresql",
                "targetDatabase": "postgresql_test_db",
                "targetTable": "postgresql_test_tb",
                "saveMode": "overwrite",
                "preQueries": [
                    "delete from postgresql_test_tb where id > 0"
                ]
            }
        },
        {
            "name": "file",
            "config": {
                "sourceTable": "source_table_03",
                "sourceQuery": "",
                "numPartitions": 0,
                "options": {},
                "path": "hdfs:///data/tmp_file/${dt}",
                "serializer": "parquet",
                "saveMode": "overwrite",
                "partitionBy": ["type"]
            }
        }
    ]
}
const SampleArrayConfig: any = {
    "variables": {
        "dt": "2021-08-01"
    },
    "plugins": [
        {
            "type": "source",
            "name": "managed_jdbc",
            "config": {
                "resultTable": "source_table_01",
                "persist": true,
                "storageLevel": 'MEMORY_AND_DISK',
                "options": {
                    "connectionCollation": "utf8mb4_unicode_ci"
                },
                "datasource": "test_mysql",
                "query": "select * from test_mysql_db.test_tb where id > 0"
            }
        },
        {
            "type": "source",
            "name": "jdbc",
            "config": {
                "resultTable": "source_table_02",
                "persist": false,
                "storageLevel": 'MEMORY_AND_DISK',
                "options": {
                    "connectionCollation": "utf8mb4_unicode_ci"
                },
                "url": "jdbc:mysql://localhost:3306/maple_datasource?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&rewriteBatchedStatements=true&useServerPrepStmts=true&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai",
                "driver": "com.mysql.cj.jdbc.Driver",
                "user": "xi_root",
                "password": "123456",
                "query": "select * from test_mysql_db.test_tb where id > 0"
            }
        },
        {
            "type": "source",
            "name": "file",
            "config": {
                "resultTable": "source_table_03",
                "persist": false,
                "storageLevel": 'MEMORY_AND_DISK',
                "options": {},
                "path": "hdfs:///data/tmp_file/${dt}",
                "serializer": "csv",
                "columnNames": ["id", "name", "address"]
            }
        },
        {
            "name": "sql",
            "type": "transformation",
            "config": {
                "sourceTable": "",
                "resultTable": "transform_table_01",
                "persist": false,
                "storageLevel": 'MEMORY_AND_DISK',
                "sql": "select st03.name, st03.address, dtt.test\nfrom source_table_03 st03 join dw.test_tb dtt on st03.id=dtt.tid",
            }
        },
        {
            "type": "sink",
            "name": "hive",
            "config": {
                "sourceTable": "transform_table_01",
                "sourceQuery": "",
                "numPartitions": 10,
                "options": {},
                "targetDatabase": "dm",
                "targetTable": "dm_test_table",
                "saveMode": "overwrite",
                "strongCheck": false,
                "writeAsFile": false
            }
        },
        {
            "type": "sink",
            "name": "jdbc",
            "config": {
                "sourceTable": "source_table_01",
                "sourceQuery": "",
                "numPartitions": 0,
                "options": {
                    "connectionCollation": "utf8mb4_unicode_ci",
                    "isolationLevel": "NONE",
                    "batchsize": "5000"
                },
                "url": "jdbc:mysql://localhost:3306/maple_datasource?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&rewriteBatchedStatements=true&useServerPrepStmts=true&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai",
                "driver": "com.mysql.cj.jdbc.Driver",
                "user": "xi_root",
                "password": "123456",
                "targetDatabase": "test_mysql_db",
                "targetTable": "test_mysql_db_table_0001",
                "saveMode": "overwrite",
                "preQueries": [
                    "delete from test_mysql_db.test_mysql_db_table_0001"
                ]
            }
        },
        {
            "type": "sink",
            "name": "managed_jdbc",
            "config": {
                "sourceTable": "",
                "sourceQuery": "select * from source_table_01",
                "numPartitions": 0,
                "options": {
                },
                "targetDatasource": "test_postgresql",
                "targetDatabase": "postgresql_test_db",
                "targetTable": "postgresql_test_tb",
                "saveMode": "overwrite",
                "preQueries": [
                    "delete from postgresql_test_tb where id > 0"
                ]
            }
        },
        {
            "type": "sink",
            "name": "file",
            "config": {
                "sourceTable": "source_table_03",
                "sourceQuery": "",
                "numPartitions": 0,
                "options": {},
                "path": "hdfs:///data/tmp_file/${dt}",
                "serializer": "parquet",
                "saveMode": "overwrite",
                "partitionBy": ["type"]
            }
        }
    ]
}

export default {
    Databases, SampleConfig, SampleArrayConfig
}