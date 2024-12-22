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
  "spark-data-calc": () => ({
    "variables": {},
    "plugins": [],
  }),
  "flink-data-calc": () => ({
    "variables": {},
    "plugins": [],
  }),
}

export default { JobRunTypes, JobConf }