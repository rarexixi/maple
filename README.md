# 样例页面启动方式

1. 新建数据库

   初始化表 sql 路径 `maple-datasource/db/init_db.sql`

2. 运行 `maple-datasource` 模块

   1. application.properties 修改数据库配置和 FreeMarker 模版路径 `freemarker.template.path`
   2. 启动服务

3. 启动 maple-ui，访问 [http://localhost:5173](http://localhost:5173)

   ```
   npm i
   npm run dev
   ```

# 项目说明

Maple 是一个分布式计算中间件。上层应用可以方便的启动 Spark、FLink 等任务，无需关心底层提交的细节。同时支持 K8s 和 YARN 集群。
并且支持任务向多个集群提交任务。

![](./doc/assets/img/engine_execution.svg)

