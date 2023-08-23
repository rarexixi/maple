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

- SUBMITTED：新建任务，刚存储到数据库

- ACCEPTED：进入队列

- STARTING: 正在启动，从队列消费后为该状态

- STARTED_FAILED：启动失败

- RUNNING：运行中

- SUCCEED：成功

- FAILED：失败

- KILLED：运行中被杀

- LOST：连接丢失（心跳超时不上报）

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

6. execution-manager 根据执行命令生成对象，生成对应的脚本，并启动

如果启动失败，由 execution-manager 将作业状态更新为 STARTED_FAILED（发送请求到 persistence-service，persistence-service 判断作业状态为 STARTING 时才更新）

由引擎自己回写状态，并在任务执行完成前定时上报心跳

启动完成后修改状态为 RUNNING

运行成功后，修改状态为 SUCCEED

运行失败后，修改状态为 FAILED

调用 execution-manager kill 引擎，修改状态为 KILLED

scheduler 定时监控 RUNNING 中的任务，心跳超时修改状态为 LOST

## 单次任务

需要给定资源配置

调度器持续消费 redis 队列，

**消费任务**

1. 加锁
2. 判断是否有资源
   1. 将任务提交到新引擎
3. 释放锁

**引擎执行**

执行完成后，通过 webhook 通知到调用方
