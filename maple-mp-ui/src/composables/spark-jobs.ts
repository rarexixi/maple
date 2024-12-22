import type { RdbmsTable } from "@/composables/models"

export interface TableResult {
  resultTable: string
  persist: false
  storageLevel?: string
}

export interface BaseSource extends TableResult {
  options: any
}

export interface BaseTransformation extends TableResult {
}

export interface BaseSink {
  sourceTable: string
  sourceQuery: string
  numPartitions?: number
  options: any
}

export interface JdbcSourceConfig extends BaseSource {
  datasource: number
  sourceTable: RdbmsTable
}

export interface DorisSourceConfig extends BaseSource {
  datasource: number
  sourceTable: RdbmsTable
}

export interface StarRocksSourceConfig extends BaseSource {
  datasource: number
  sourceTable: RdbmsTable
}

export interface FileSourceConfig extends BaseSource {
  path: string
  serializer: string
  columnNames: string[]
}

export interface SqlTransformConfig extends BaseTransformation {
  sql: string
}

export interface JdbcSinkConfig extends BaseSink {
  targetDatasource: number
  targetTable: RdbmsTable
  saveMode: string
  preQueries: string[]
}

export interface DorisSinkConfig extends BaseSink {
  targetDatasource: number
  targetTable: RdbmsTable
  saveMode: string
}

export interface StarRocksSinkConfig extends BaseSink {
  targetDatasource: number
  targetTable: RdbmsTable
  saveMode: string
}

export interface HiveSinkConfig extends BaseSink {
  targetTable: RdbmsTable
  saveMode: string
  strongCheck: boolean
  writeAsFile: boolean
}

export interface FileSinkConfig extends BaseSink {
  path: string
  serializer: string
  saveMode: string
  partitionBy: string[]
}

const getRdbmsTable = () => ({
  databaseName: undefined,
  schemaName: undefined,
  tableName: undefined,
})

const getBaseSource = () => ({
  resultTable: undefined,
  persist: false,
  storageLevel: undefined,
  options: {},
})

const getBaseTransformation = () => ({
  resultTable: undefined,
  persist: false,
  storageLevel: undefined,
})

const getBaseSink = () => ({
  sourceTable: undefined,
  sourceQuery: undefined,
  numPartitions: undefined,
  options: {},
})

const SparkDataCalcModels: any = {
  "source": {
    "jdbc": () => ({
      type: "source",
      name: "jdbc",
      config: {
        ...getBaseSource(),
        datasource: undefined,
        sourceTable: getRdbmsTable(),
      }
    }),
    "doris": () => ({
      type: "source",
      name: "doris",
      config: {
        ...getBaseSource(),
        datasource: undefined,
        sourceTable: getRdbmsTable(),
      }
    }),
    "starrocks": () => ({
      type: "source",
      name: "starrocks",
      config: {
        ...getBaseSource(),
        datasource: undefined,
        sourceTable: getRdbmsTable(),
      }
    }),
    "file": () => ({
      type: "source",
      name: "file",
      config: {
        ...getBaseSource(),
        path: undefined,
        serializer: "parquet",
        columnNames: [],
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
        targetTable: getRdbmsTable(),
        saveMode: "overwrite",
        preQueries: [],
      }
    }),
    "doris": () => ({
      type: "sink",
      name: "doris",
      config: {
        ...getBaseSink(),
        targetDatasource: undefined,
        targetTable: getRdbmsTable(),
        saveMode: "overwrite",
        preQueries: [],
      }
    }),
    "starrocks": () => ({
      type: "sink",
      name: "starrocks",
      config: {
        ...getBaseSink(),
        targetDatasource: undefined,
        targetTable: getRdbmsTable(),
        saveMode: "overwrite",
        preQueries: [],
      }
    }),
    "hive": () => ({
      type: "sink",
      name: "hive",
      config: {
        ...getBaseSink(),
        targetTable: getRdbmsTable(),
        saveMode: "overwrite",
        strongCheck: false,
        writeAsFile: false,
      }
    }),
    "file": () => ({
      type: "sink",
      name: "file",
      config: {
        ...getBaseSink(),
        path: undefined,
        serializer: "parquet",
        saveMode: "overwrite",
        partitionBy: [],
      }
    }),
  },
}


export default { SparkDataCalcModels }