import type { RdbmsTable } from "@/composables/models"

export interface WatermarkDefinition {
  columnName: string
  delaySeconds: number
}

export interface TableDefinition {
  datasourceId: number
  resultTable: String
  comment: String
  watermark: WatermarkDefinition
  partitionColumns: string[]
  options: any
}

export interface BaseSource extends TableDefinition {
}

export interface BaseTransformation {
  resultTable: String
}

export interface BaseSink extends TableDefinition {
}

export interface Db2CdcSourceConfig extends BaseSource {
  rdbmsTable: RdbmsTable
  scanStartupMode: string
}

export interface DorisSourceConfig extends BaseSource {
  rdbmsTable: RdbmsTable
}

export interface JdbcSourceConfig extends BaseSource {
  rdbmsTable: RdbmsTable
}

export interface KafkaSourceConfig extends BaseSource {
  topic: string
  groupId: string
  format: string
  scanStartupMode: string
}

export interface MysqlCdcSourceConfig extends BaseSource {
  rdbmsTable: RdbmsTable
  scanStartupMode: string
}

export interface OceanBaseCdcSourceConfig extends BaseSource {
  rdbmsTable: RdbmsTable
  scanStartupMode: string
}

export interface OracleCdcSourceConfig extends BaseSource {
  rdbmsTable: RdbmsTable
  scanStartupMode: string
}

export interface PostgresCdcSourceConfig extends BaseSource {
  rdbmsTable: RdbmsTable
  slotName: string
  scanStartupMode: string
}

export interface SqlServerCdcSourceConfig extends BaseSource {
  rdbmsTable: RdbmsTable
}

export interface StarRocksSourceConfig extends BaseSource {
  rdbmsTable: RdbmsTable
}

export interface TidbCdcSourceConfig extends BaseSource {
  rdbmsTable: RdbmsTable
  scanStartupMode: string
}

export interface UpsertKafkaSourceConfig extends BaseSource {
  topic: string
  groupId: string
  format: string
  scanStartupMode: string
  keyFormat: string
  valueFormat: string
  valueFieldsInclude: string
}

export interface SqlTransformConfig extends BaseTransformation {
  sql: string
}

export interface DorisSinkConfig extends BaseSink {
  rdbmsTable: RdbmsTable
  labelPrefix: string
}

export interface JdbcSinkConfig extends BaseSink {
  rdbmsTable: RdbmsTable
}

export interface KafkaSinkConfig extends BaseSink {
  topic: string
  format: string
}

export interface StarRocksSinkConfig extends BaseSink {
  rdbmsTable: RdbmsTable
}

export interface UpsertKafkaSinkConfig extends BaseSink {
  topic: string
  format: string
  keyFormat: string
  valueFormat: string
  valueFieldsInclude: string
}

const getBaseSource = () => ({
  datasourceId: undefined,
  resultTable: undefined,
  comment: undefined,
  watermark: {
    columnName: undefined,
    delaySeconds: 5,
  },
  partitionColumns: [],
  options: {},
})

const getBaseTransformation = () => ({
  resultTable: undefined,
})

const getBaseSink = () => ({
  datasourceId: undefined,
  resultTable: undefined,
  comment: undefined,
  watermark: {
    columnName: undefined,
    delaySeconds: 5,
  },
  partitionColumns: [],
  options: {},
})

const getRdbmsTable = () => ({
  databaseName: undefined,
  schemaName: undefined,
  tableName: undefined,
})

const FlinkDataCalcModels: any = {
  "source": {
    "jdbc": () => ({
      type: "source",
      name: "jdbc",
      config: {
        ...getBaseSource(),
        rdbmsTable: getRdbmsTable(),
      }
    }),
    "doris": () => ({
      type: "source",
      name: "doris",
      config: {
        ...getBaseSource(),
        rdbmsTable: getRdbmsTable(),
      }
    }),
    "starrocks": () => ({
      type: "source",
      name: "starrocks",
      config: {
        ...getBaseSource(),
        rdbmsTable: getRdbmsTable(),
      }
    }),
    "mysql-cdc": () => ({
      type: "source",
      name: "mysql-cdc",
      config: {
        ...getBaseSource(),
        rdbmsTable: getRdbmsTable(),
        scanStartupMode: undefined,
      }
    }),
    "postgres-cdc": () => ({
      type: "source",
      name: "postgres-cdc",
      config: {
        ...getBaseSource(),
        rdbmsTable: getRdbmsTable(),
        slotName: undefined,
        scanStartupMode: undefined,
      }
    }),
    "oracle-cdc": () => ({
      type: "source",
      name: "oracle-cdc",
      config: {
        ...getBaseSource(),
        rdbmsTable: getRdbmsTable(),
        scanStartupMode: undefined,
      }
    }),
    "sqlserver-cdc": () => ({
      type: "source",
      name: "sqlserver-cdc",
      config: {
        ...getBaseSource(),
        rdbmsTable: getRdbmsTable(),
      }
    }),
    "db2-cdc": () => ({
      type: "source",
      name: "db2-cdc",
      config: {
        ...getBaseSource(),
        rdbmsTable: getRdbmsTable(),
      }
    }),
    "tidb-cdc": () => ({
      type: "source",
      name: "tidb-cdc",
      config: {
        ...getBaseSource(),
        rdbmsTable: getRdbmsTable(),
        scanStartupMode: undefined,
      }
    }),
    "kafka": () => ({
      type: "source",
      name: "kafka",
      config: {
        ...getBaseSource(),
        topic: undefined,
        groupId: undefined,
        format: undefined,
        scanStartupMode: undefined,
      }
    }),
    "upsert-kafka": () => ({
      type: "source",
      name: "upsert-kafka",
      config: {
        ...getBaseSource(),
        topic: undefined,
        format: undefined,
        scanStartupMode: undefined,
        keyFormat: undefined,
        valueFormat: undefined,
        valueFieldsInclude: undefined,
      }
    }),
  },
  "transformation": {
    "sql": () => ({
      type: "transformation",
      name: "sql",
      config: {
        ...getBaseTransformation(),
        sql: undefined,
      }
    }),
  },
  "sink": {
    "jdbc": () => ({
      type: "sink",
      name: "jdbc",
      config: {
        ...getBaseSink(),
        rdbmsTable: getRdbmsTable(),
      }
    }),
    "doris": () => ({
      type: "sink",
      name: "doris",
      config: {
        ...getBaseSink(),
        rdbmsTable: getRdbmsTable(),
        labelPrefix: undefined,
      }
    }),
    "starrocks": () => ({
      type: "sink",
      name: "starrocks",
      config: {
        ...getBaseSink(),
        rdbmsTable: getRdbmsTable(),
      }
    }),
    "kafka": () => ({
      type: "sink",
      name: "kafka",
      config: {
        ...getBaseSink(),
        rdbmsTable: getRdbmsTable(),
        saveMode: "overwrite",
        preQueries: [],
      }
    }),
    "upsert-kafka": () => ({
      type: "sink",
      name: "upsert-kafka",
      config: {
        ...getBaseSink(),
        topic: undefined,
        format: undefined,
        keyFormat: undefined,
        valueFormat: undefined,
        valueFieldsInclude: undefined,
      }
    }),
  },
}

export default { FlinkDataCalcModels }