# 总体架构

## 服务说明

### maple-restful-api 入口服务

用于接收任务请求

### maple-scheduler 调度器

用于消费和管理任务

### maple-execution-manager 执行管理器

负责引擎的创建，任务执行

### maple-persistence-service 持久化服务

用于数据库访问

## 引擎执行

### 执行状态

- CREATED:       新建任务，刚存储到数据库
- ACCEPTED:      添加系统队列
- STARTING:      提交集群/开始启动
- START_FAILED:  提交集群失败/启动失败
- RUNNING:       运行中
- SUCCEED:       运行成功
- FAILED:        运行失败
- KILLED:        强制杀死
- CANCELED:      作业停止/取消
- UNKNOWN:       未知

**状态流转**

![](./assets/img/engine_execution_status.svg) 

## 用户请求任务

请求信息包括：请求唯一ID、group、user、对接应用、作业类型、请求描述，请求类型，执行类型，webhook(回调地址)

### 执行流程

![](./assets/img/engine_execution.svg)

1. 用户发起引擎执行作业请求

2. 添加作业状态为 SUBMITTED

   1. 将作业信息存储到数据库，获取到作业ID

   2. 将作业ID添加到Redis队列，队列标识：cluster + queue + 来源应用 + group + 优先级，例：hadoop-root.default-schedule-maple-1，修改作业状态为 ACCEPTED

3. scheduler 持续消费 redis 队列，根据作业ID拿到执行详细信息

4. scheduler 将执行请求发送到 execution-manager，修改作业状态为 STARTING

5. execution-manager 根据引擎的类型，加载对应插件，获取到执行命令生成对象（包括模板地址，输出文件地址，模板数据对象）

6. execution-manager 根据执行命令生成对象，

   1. YARN 生成对应的脚本，并启动

   2. K8s 生成对应的 yaml 文件，并调用 Scheduler 服务提交

如果启动失败，由 execution-manager 将作业状态更新为 START_FAILED（发送请求到 persistence-service，persistence-service 判断作业状态为 STARTING 时才更新）

引擎启动后自己回写状态，同时由 scheduler 监控状态

   1. YARN 类型的任务由 scheduler 定时获取结束的任务，修改作业状态为对应的结束状态

   2. K8s 类型的任务由 scheduler 通过 informer list-watch 机制，实时修改作业状态

启动完成后修改状态为 RUNNING

运行成功后，修改状态为 SUCCEED

运行失败后，修改状态为 FAILED

调用 scheduler kill 引擎，修改状态为 KILLED

# 数据库设计

## `maple_cluster_engine`

### ext_info 字段说明

### spark

```json
{
  "envs": {
    "HADOOP_HOME": "",
    "HADOOP_CONF_DIR": "",
    "YARN_CONF_DIR": ""
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
}
```

### flink

```json
{
  "envs": {
    "HADOOP_CLASSPATH": "/opt/hadoop/current/etc/hadoop:/opt/hadoop/current/share/hadoop/common/lib/*:/opt/hadoop/current/share/hadoop/common/*:/opt/hadoop/current/share/hadoop/hdfs:/opt/hadoop/current/share/hadoop/hdfs/lib/*:/opt/hadoop/current/share/hadoop/hdfs/*:/opt/hadoop/current/share/hadoop/mapreduce/*:/opt/hadoop/current/share/hadoop/yarn:/opt/hadoop/current/share/hadoop/yarn/lib/*:/opt/hadoop/current/share/hadoop/yarn/*",
  },
  "forbiddenConfs": [
  ]
}
```

## `maple_cluster_engine_default_conf`




## `maple_engine_execution`



## `maple_engine_execution_ext_info`

- configuration: 作业的配置信息，json 字符串

  
- ext_info: 作业的扩展信息，json 字符串，todo

  
- exec_info: 作业的执行信息，json 字符串

### YARN

```json
{
  "lastStatus": "",
  "startInfo": {
    "statuses": [
      {
        "code": "",
        "status": "",
        "message": "",
        "time": ""
      }
    ]
  },
  "lastClusterStatus": "",
  "clusterInfo": {
    "statuses": [
      {
        "code": "",
        "status": "",
        "time": ""
      }
    ],
    "resources": {
      "memory": "",
      "cores": ""
    },
    "detail": {
      // map
    }
  },
  "rawStatus": "",
  "rawInfo": {
    "statuses": [
      {
        "code": "",
        "status": "",
        "time": ""
      }
    ]
  }
}
```

### K8s

```json
{
  "lastStatus": "",
  "startInfo": {
    "statuses": [
      {
        "code": "",
        "status": "",
        "message": "",
        "time": ""
      }
    ]
  },
  "lastClusterStatus": "",
  "clusterInfo": {
    "statuses": [
      {
        "code": "",
        "status": "",
        "time": ""
      }
    ],
    "resources": {
      "memory": "",
      "cores": ""
    },
    "detail": {
      // map
    }
  }
}
```


# 作业配置

## spark

```json
{
   "driverCores": "",
   "driverMemory": "",
   "numExecutors": "",
   "executorCores": "",
   "executorMemory": "",
   "driverJavaOptions": "",
   "driverClassPath": "",
   "jars": "",
   "files": "",
   "archives": "",
   "conf": {
   },
   "runType": "data_calc",
   "jobConf": {
      "mainFile": ""
   },
   "runType": "sql",
   "jobConf": {
      "mainFile": ""
   },
   "runType": "scala",
   "jobConf": {
      "mainFile": ""
   },
   "runType": "py",
   "jobConf": {
      "pyFiles": "",    // 第三方库，你可以将它们打包成 .zip、.egg 或 .whl 文件
      "mainFile": "",
      "args": ""
   },
   "runType": "jar",
   "jobConf": {
      "mainFile": "",
      "mainClass": "",
      "args": ""
   }
}
```

## flink

```json
{
   "runType": "data_calc",
   "jobConf": {
      "mainFile": ""
   },
   "runType": "sql",
   "jobConf": {
      "mainFile": ""
   },
   "runType": "scala",
   "jobConf": {
      "mainFile": ""
   },
   "runType": "py",
   "jobConf": {
      "pyFiles": "",    // 第三方库，你可以将它们打包成 .zip、.egg 或 .whl 文件
      "mainFile": "",
      "args": ""
   },
   "runType": "jar",
   "jobConf": {
      "mainFile": "",
      "mainClass": "",
      "args": ""
   }
}
```

生成对象：

```json
{
   "mapleId": "1",
   "execName": "xxx_sync_task",
   "fromApp": "schedule",
   "queue": "default",
   "group": "",
   "user": "",
   "job": {} // 上面的作业配置
}
```


# YARN

# 作业返回详情
