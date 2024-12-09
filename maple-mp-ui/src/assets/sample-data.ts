import { listSearch } from "@/composables/requests";

const getBaseSource = () => ({
    resultTable: '',
    persist: false,
    storageLevel: 'MEMORY_AND_DISK',
    options: {},
})

const getBaseTransformation = () => ({
    sourceTable: '',
    resultTable: '',
    persist: false,
    storageLevel: 'MEMORY_AND_DISK',
})

const getBaseSink = () => ({
    sourceTable: '',
    sourceQuery: '',
    numPartitions: 0,
    options: {},
})

const PluginModels: any = {
    'source': {
        'jdbc': () => ({
            type: 'source',
            name: 'jdbc',
            config: {
                ...getBaseSource(),
                url: '',
                driver: '',
                user: '',
                password: '',
                query: '',
            }
        }),
        'doris': () => ({
            type: 'source',
            name: 'doris',
            config: {
                ...getBaseSource(),
                fenodes: '',
                user: '',
                password: '',
                database: '',
                table: '',
            }
        }),
        'star_rocks': () => ({
            type: 'source',
            name: 'starrocks',
            config: {
                ...getBaseSource(),
                feHttpUrl: '',
                feJdbcUrl: '',
                user: '',
                password: '',
                database: '',
                table: '',
            }
        }),
        'managed_jdbc': () => ({
            type: 'source',
            name: 'managed_jdbc',
            config: {
                ...getBaseSource(),
                datasource: '',
                query: '',
            }
        }),
        'file': () => ({
            type: 'source',
            name: 'file',
            config: {
                ...getBaseSource(),
                path: '',
                serializer: 'parquet',
                columnNames: [],
            }
        }),
    },
    'transformation': {
        'sql': () => ({
            type: 'transformation',
            name: 'sql',
            config: {
                ...getBaseTransformation(),
                sql: '',
            }
        }),
    },
    'sink': {
        'hive': () => ({
            type: 'sink',
            name: 'hive',
            config: {
                ...getBaseSink(),
                targetDatabase: '',
                targetTable: '',
                saveMode: 'overwrite',
                strongCheck: false,
                writeAsFile: false,
            }
        }),
        'jdbc': () => ({
            type: 'sink',
            name: 'jdbc',
            config: {
                ...getBaseSink(),
                url: '',
                driver: '',
                user: '',
                password: '',
                targetDatabase: '',
                targetTable: '',
                saveMode: 'overwrite',
                preQueries: [],
            }
        }),
        'doris': () => ({
            type: 'sink',
            name: 'doris',
            config: {
                ...getBaseSink(),
                fenodes: '',
                user: '',
                password: '',
                targetDatabase: '',
                targetTable: '',
                saveMode: 'overwrite',
                preQueries: [],
            }
        }),
        'star_rocks': () => ({
            type: 'sink',
            name: 'starrocks',
            config: {
                ...getBaseSink(),
                feHttpUrl: '',
                feJdbcUrl: '',
                user: '',
                password: '',
                targetDatabase: '',
                targetTable: '',
                saveMode: 'overwrite',
                preQueries: [],
            }
        }),
        'managed_jdbc': () => ({
            type: 'sink',
            name: 'managed_jdbc',
            config: {
                ...getBaseSink(),
                targetDatasource: '',
                targetDatabase: '',
                targetTable: '',
                saveMode: 'overwrite',
                preQueries: [],
            }
        }),
        'file': () => ({
            type: 'sink',
            name: 'file',
            config: {
                ...getBaseSink(),
                path: '',
                serializer: 'parquet',
                saveMode: 'overwrite',
                partitionBy: [],
            }
        }),
    },
}

const FileSerializers: Array<string> = ["parquet", "orc", "csv", "text", "json"]

const Layout = {
    labelCols: {
        w160: { span: 16 },
        w320: { span: 8 },
        w640: { span: 4 },
        w1280: { span: 2 },
    },
    wrapCols: {
        w160: { offset: 16 },
        w320: { offset: 8 },
        w640: { offset: 4 },
        w1280: { offset: 2 },
    },
}

const StorageLevels = [
    { value: 'NONE', label: 'NONE', },
    { value: 'DISK_ONLY', label: 'DISK_ONLY', },
    { value: 'DISK_ONLY_2', label: 'DISK_ONLY_2', },
    { value: 'MEMORY_ONLY', label: 'MEMORY_ONLY', },
    { value: 'MEMORY_ONLY_2', label: 'MEMORY_ONLY_2', },
    { value: 'MEMORY_ONLY_SER', label: 'MEMORY_ONLY_SER', },
    { value: 'MEMORY_ONLY_SER_2', label: 'MEMORY_ONLY_SER_2', },
    { value: 'MEMORY_AND_DISK', label: 'MEMORY_AND_DISK', },
    { value: 'MEMORY_AND_DISK_2', label: 'MEMORY_AND_DISK_2', },
    { value: 'MEMORY_AND_DISK_SER', label: 'MEMORY_AND_DISK_SER', },
    { value: 'MEMORY_AND_DISK_SER_2', label: 'MEMORY_AND_DISK_SER_2', },
    { value: 'OFF_HEAP', label: 'OFF_HEAP', },
]

const getDatasourceSelectList = listSearch({ url: '/api/datasources/all', method: 'GET' }, {})

getDatasourceSelectList.search()

const DatasourceList = getDatasourceSelectList.dataList


const Databases: Array<any> = [{
    "databaseName": "ods",
}, {
    "databaseName": "dw",
}, {
    "databaseName": "dm",
}]

const CacheLevels: Array<any> = []

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
    PluginModels, FileSerializers, Layout, Databases, DatasourceList, StorageLevels, SampleConfig, SampleArrayConfig
}