const JobRunTypes: any = {
  "spark": () => ({
    driverCores: undefined,
    driverMemory: undefined,
    executorCores: undefined,
    executorMemory: undefined,
    numExecutors: undefined,
    driverJavaOptions: undefined,
    driverClassPath: undefined,
    jars: undefined,
    files: undefined,
    archives: undefined,
    confs: {}
  }),
  "flink": () => ({
    jobManagerHaEnable: true,
    jobManagerReplicas: 2,
    jobManagerCores: 1,
    jobManagerMemory: undefined,
    numTaskManager: undefined,
    taskManagerCores: undefined,
    taskManagerMemory: undefined,
    numberOfTaskSlots: undefined,
    confs: {}
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