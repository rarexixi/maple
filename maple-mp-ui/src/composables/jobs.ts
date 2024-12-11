const getBaseSource = () => ({
  resultTable: "",
  persist: false,
  storageLevel: undefined,
  options: {},
})

const getBaseTransformation = () => ({
  sourceTable: "",
  resultTable: "",
  persist: false,
  storageLevel: undefined,
})

const getBaseSink = () => ({
  sourceTable: "",
  sourceQuery: "",
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
        url: "",
        driver: "",
        user: "",
        password: "",
        query: "",
      }
    }),
    "doris": () => ({
      type: "source",
      name: "doris",
      config: {
        ...getBaseSource(),
        fenodes: "",
        user: "",
        password: "",
        database: "",
        table: "",
      }
    }),
    "star_rocks": () => ({
      type: "source",
      name: "starrocks",
      config: {
        ...getBaseSource(),
        feHttpUrl: "",
        feJdbcUrl: "",
        user: "",
        password: "",
        database: "",
        table: "",
      }
    }),
    "managed_jdbc": () => ({
      type: "source",
      name: "managed_jdbc",
      config: {
        ...getBaseSource(),
        datasource: "",
        query: "",
      }
    }),
    "file": () => ({
      type: "source",
      name: "file",
      config: {
        ...getBaseSource(),
        path: "",
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
        sql: "",
      }
    }),
  },
  "sink": {
    "hive": () => ({
      type: "sink",
      name: "hive",
      config: {
        ...getBaseSink(),
        targetDatabase: "",
        targetTable: "",
        saveMode: "overwrite",
        strongCheck: false,
        writeAsFile: false,
      }
    }),
    "jdbc": () => ({
      type: "sink",
      name: "jdbc",
      config: {
        ...getBaseSink(),
        url: "",
        driver: "",
        user: "",
        password: "",
        targetDatabase: "",
        targetTable: "",
        saveMode: "overwrite",
        preQueries: [],
      }
    }),
    "doris": () => ({
      type: "sink",
      name: "doris",
      config: {
        ...getBaseSink(),
        fenodes: "",
        user: "",
        password: "",
        targetDatabase: "",
        targetTable: "",
        saveMode: "overwrite",
        preQueries: [],
      }
    }),
    "star_rocks": () => ({
      type: "sink",
      name: "starrocks",
      config: {
        ...getBaseSink(),
        feHttpUrl: "",
        feJdbcUrl: "",
        user: "",
        password: "",
        targetDatabase: "",
        targetTable: "",
        saveMode: "overwrite",
        preQueries: [],
      }
    }),
    "managed_jdbc": () => ({
      type: "sink",
      name: "managed_jdbc",
      config: {
        ...getBaseSink(),
        targetDatasource: "",
        targetDatabase: "",
        targetTable: "",
        saveMode: "overwrite",
        preQueries: [],
      }
    }),
    "file": () => ({
      type: "sink",
      name: "file",
      config: {
        ...getBaseSink(),
        path: "",
        serializer: "parquet",
        saveMode: "overwrite",
        partitionBy: [],
      }
    }),
  },
}

const FlinkDataCalcModels = {}

const JobRunTypes: any = {
  "spark": () => ({
    driverCores: undefined,
    driverMemory: undefined,
    executorCores: undefined,
    executorMemory: undefined,
    numExecutors: undefined,
  }),
  "flink": () => ({
    driverCores: undefined,
    driverMemory: undefined,
    executorCores: undefined,
    executorMemory: undefined,
    numExecutors: undefined,
  }),
}

const JobConf: any = {
  "spark-data-calc-group": () => ({
    "variables": {},
    "sources": [],
    "transformations": [],
    "sinks": []
  }),
  "spark-data-calc-array": () => ({
    "variables": {},
    "plugins": [],
  }),
  "flink-data-calc-group": () => ({
    "variables": {},
    "sources": [],
    "transformations": [],
    "sinks": []
  }),
  "flink-data-calc-array": () => ({
    "variables": {},
    "plugins": [],
  }),
}

export default { SparkDataCalcModels, FlinkDataCalcModels, JobRunTypes, JobConf }