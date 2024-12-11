# create database maple default character set utf8;

# region datasource

DROP TABLE IF EXISTS `maple`.`maple_datasource_type`;
CREATE TABLE `maple`.`maple_datasource_type`
(
    `type_code`      varchar(32)                            NOT NULL COMMENT '类型编码',
    `type_name`      varchar(256) DEFAULT ''                NOT NULL COMMENT '类型名称',
    `icon`           varchar(256) DEFAULT ''                NOT NULL COMMENT '图标地址',
    `classifier`     varchar(32)                            NOT NULL COMMENT '分类',
    `versions`       varchar(256) DEFAULT ''                NOT NULL COMMENT '版本(多个版本用","隔开)',
    `configurations` json                                   NOT NULL COMMENT '数据源配置信息',

    `disabled`       tinyint      DEFAULT 0                 NOT NULL COMMENT '是否禁用',
    `created_by`     int          DEFAULT 0                 NOT NULL COMMENT '创建人',
    `updated_by`     int          DEFAULT 0                 NOT NULL COMMENT '修改人',
    `created_at`     datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`     datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`type_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '数据源类型';

DROP TABLE IF EXISTS `maple`.`maple_datasource`;
CREATE TABLE `maple`.`maple_datasource`
(
    `id`              int                                    NOT NULL AUTO_INCREMENT,
    `name`            varchar(32)                            NOT NULL COMMENT '数据源名称',
    `description`     varchar(256) DEFAULT ''                NOT NULL COMMENT '数据源描述',
    `datasource_type` varchar(32)                            NOT NULL COMMENT '数据源类型',
    `version`         varchar(32)                            NOT NULL COMMENT '数据源版本',
    `datasource_conf` json                                   NOT NULL COMMENT '数据源配置',

    `disabled`        tinyint      DEFAULT 0                 NOT NULL COMMENT '是否禁用',
    `created_by`      int          DEFAULT 0                 NOT NULL COMMENT '创建人',
    `updated_by`      int          DEFAULT 0                 NOT NULL COMMENT '修改人',
    `created_at`      datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`      datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE `uniq_datasource_name` (`name`),
    INDEX `idx_datasource_type` (`datasource_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '数据源';

# endregion

# region material

DROP TABLE IF EXISTS `maple`.`maple_material`;
CREATE TABLE `maple`.`maple_material`
(
    `id`             int                                   NOT NULL AUTO_INCREMENT,
    `name`           varchar(32)                           NOT NULL COMMENT '素材名称',
    `description`           varchar(256)                          NOT NULL COMMENT '素材描述',
    `material_type`  varchar(32)                           NOT NULL COMMENT '素材类型',
    `visibility`     varchar(32)                           NOT NULL COMMENT '可见范围 (system, group, private)',
    `latest_version` int         DEFAULT 1                 NOT NULL COMMENT '最新版本',

    `group`          varchar(32) DEFAULT ''                NOT NULL COMMENT '用户组',
    `user`           varchar(32) DEFAULT ''                NOT NULL COMMENT '用户',

    `disabled`       tinyint     DEFAULT 0                 NOT NULL COMMENT '是否禁用',
    `created_by`     int         DEFAULT 0                 NOT NULL COMMENT '创建人',
    `updated_by`     int         DEFAULT 0                 NOT NULL COMMENT '修改人',
    `created_at`     datetime    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`     datetime    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE `uniq_material_name` (`name`),
    INDEX `idx_material_type` (`material_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '素材配置';


DROP TABLE IF EXISTS `maple`.`maple_material_version`;
CREATE TABLE `maple`.`maple_material_version`
(
    `id`            int                                NOT NULL AUTO_INCREMENT,
    `material_id`   int                                NOT NULL COMMENT '素材ID',
    `store_path`    varchar(256)                       NOT NULL COMMENT '存储路径', -- 例如 HDFS 路径
    `file_sha256`   char(64)                           NOT NULL COMMENT '文件SHA256',
    `description`          varchar(256)                       NOT NULL COMMENT '版本描述',
    `version`       int                                NOT NULL COMMENT '版本',
    `material_conf` json                               NOT NULL COMMENT '素材配置',

    `disabled`      tinyint  DEFAULT 0                 NOT NULL COMMENT '是否禁用',
    `created_by`    int      DEFAULT 0                 NOT NULL COMMENT '创建人',
    `updated_by`    int      DEFAULT 0                 NOT NULL COMMENT '修改人',
    `created_at`    datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`    datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE `uniq_material_version` (`material_id`, `version`),
    INDEX `idx_material_id` (`material_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '素材版本配置';

# endregion

# region application

DROP TABLE IF EXISTS `maple`.`maple_application`;
CREATE TABLE `maple`.`maple_application`
(
    `app_name`    varchar(32)                             NOT NULL COMMENT '应用名称',
    `access_key`  varchar(256)  DEFAULT ''                NOT NULL COMMENT '应用访问密钥',
    `legal_hosts` varchar(1500) DEFAULT '*'               NOT NULL COMMENT '允许请求的IP',
    `webhooks`    varchar(2048) DEFAULT '{}'              NOT NULL COMMENT '回调接口',

    `disabled`    tinyint       DEFAULT 0                 NOT NULL COMMENT '是否禁用',
    `created_by`  int           DEFAULT 0                 NOT NULL COMMENT '创建人',
    `updated_by`  int           DEFAULT 0                 NOT NULL COMMENT '修改人',
    `created_at`  datetime      DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`  datetime      DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`app_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '访问程序';

# endregion

# region engine

DROP TABLE IF EXISTS `maple`.`maple_cluster`;
CREATE TABLE `maple`.`maple_cluster`
(
    `id`           int                                   NOT NULL AUTO_INCREMENT COMMENT '集群ID',
    `name`         varchar(32)                           NOT NULL COMMENT '集群名称',
    `category`     varchar(16)                           NOT NULL COMMENT '集群种类', -- YARN, K8s
    `address`      varchar(256)                          NOT NULL COMMENT '集群地址',
    `description`         varchar(16) DEFAULT ''                NOT NULL COMMENT '集群说明',
    `cluster_conf` json                                  NOT NULL COMMENT '集群配置',

    `disabled`     tinyint     DEFAULT 0                 NOT NULL COMMENT '是否禁用',
    `created_by`   int         DEFAULT 0                 NOT NULL COMMENT '创建人',
    `updated_by`   int         DEFAULT 0                 NOT NULL COMMENT '修改人',
    `created_at`   datetime    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`   datetime    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE uniq_cluster_address (`address`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '集群';

DROP TABLE IF EXISTS `maple`.`maple_cluster_engine`;
CREATE TABLE `maple`.`maple_cluster_engine`
(
    `id`          int                                    NOT NULL AUTO_INCREMENT COMMENT '引擎ID',
    `cluster_id`  varchar(32)  DEFAULT ''                NOT NULL COMMENT '所属集群',
    `name`        varchar(32)  DEFAULT ''                NOT NULL COMMENT '引擎名称', -- spark, flink, hive
    `version`     varchar(32)  DEFAULT ''                NOT NULL COMMENT '引擎版本',
    `engine_home` varchar(256) DEFAULT ''                NOT NULL COMMENT '引擎目录',
    `engine_conf` json                                   NOT NULL COMMENT '引擎配置', -- 包括禁止的 --conf 配置，envs, default_conf 等

    `disabled`    tinyint      DEFAULT 0                 NOT NULL COMMENT '是否禁用',
    `created_by`  int          DEFAULT 0                 NOT NULL COMMENT '创建人',
    `updated_by`  int          DEFAULT 0                 NOT NULL COMMENT '修改人',
    `created_at`  datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`  datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE uniq_cluster_engine_version (`cluster_id`, `name`, `version`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '计算引擎';

DROP TABLE IF EXISTS `maple`.`maple_udf`;
CREATE TABLE `maple`.`maple_udf`
(
    `id`               int                                    NOT NULL AUTO_INCREMENT COMMENT '引擎ID',
    `material_id`      int                                    NOT NULL COMMENT '物料ID',
    `material_version` int                                    NOT NULL COMMENT '物料版本',
    `function_name`    varchar(32)  DEFAULT ''                NOT NULL COMMENT '方法名称',
    `main_class`       varchar(256) DEFAULT ''                NOT NULL COMMENT '方法主类',
    `engine_home`      varchar(256) DEFAULT ''                NOT NULL COMMENT '引擎目录',
    `udf_conf`         json                                   NOT NULL COMMENT '扩展信息',

    `created_at`       datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`       datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = 'UDF';

DROP TABLE IF EXISTS `maple`.`maple_cluster_engine_default_conf`;
CREATE TABLE `maple`.`maple_cluster_engine_default_conf`
(
    `id`           int                    NOT NULL AUTO_INCREMENT COMMENT '引擎ID',
    `obj_type`     varchar(32) DEFAULT '' NOT NULL COMMENT '主体类型', -- group、user
    `obj_name`     varchar(32) DEFAULT '' NOT NULL COMMENT '所属主体', -- group_name、user_name
    `engine_id`    int                    NOT NULL COMMENT '集群引擎ID',
    `default_conf` json                   NOT NULL COMMENT '默认配置', -- json，包括 envs，conf，args

    PRIMARY KEY (`id`),
    UNIQUE uniq_cluster_engine_obj_name (`obj_type`, `obj_name`, `engine_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '集群引擎默认配置';

DROP TABLE IF EXISTS `maple`.`maple_engine_execution_queue`;
CREATE TABLE `maple`.`maple_engine_execution_queue`
(
    `queue_name`    varchar(128) DEFAULT ''                NOT NULL COMMENT '执行队列名',
    `cluster`       varchar(16)  DEFAULT ''                NOT NULL COMMENT '提交集群',
    `cluster_queue` varchar(128) DEFAULT ''                NOT NULL COMMENT '集群队列',
    `from_app`      varchar(16)  DEFAULT ''                NOT NULL COMMENT '来源应用',
    `group`         varchar(16)  DEFAULT ''                NOT NULL COMMENT '用户组',
    `priority`      tinyint                                NOT NULL COMMENT '队列优先级',

    `created_at`    datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`    datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`queue_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '执行队列';

DROP TABLE IF EXISTS `maple`.`maple_engine_execution`;
CREATE TABLE `maple`.`maple_engine_execution`
(
    `id`              int                                   NOT NULL AUTO_INCREMENT COMMENT '执行ID',
    `exec_file`       varchar(256)                          NOT NULL COMMENT '执行文件',

    `from_app`        varchar(16)                           NOT NULL COMMENT '来源应用',           -- 用于区分哪个应用提交，例如调度系统，实时平台等
    `job_id`          varchar(32)                           NOT NULL COMMENT '作业ID',             -- 用于关联到某一个具体的作业配置
    `biz_id`          varchar(32)                           NOT NULL COMMENT '执行批次ID',         -- 某个作业同一个业务时间运行的实例的集合ID (可能包含多次执行)
    `exec_uniq_id`    varchar(32)                           NOT NULL COMMENT '应用作业执行唯一ID', -- 一个作业运行实例的ID，用于防止重复提交执行
    `exec_name`       varchar(32) DEFAULT ''                NOT NULL COMMENT '执行名称',           -- 作业的code，用于生成集群上的名称

    `cluster`         varchar(32)                           NOT NULL COMMENT '提交集群',
    `resource_group`  json        DEFAULT '{}'              NOT NULL COMMENT '集群资源组',         -- 如 YARN、Volcano 的 Queue，K8s 的 Namespace 等
    `engine_category` varchar(16) DEFAULT ''                NOT NULL COMMENT '引擎种类',           -- 如 Spark、Flink、Hive 等
    `engine_version`  varchar(16) DEFAULT ''                NOT NULL COMMENT '引擎版本',
    `priority`        tinyint                               NOT NULL COMMENT '初始优先级',         -- 用于区分优先级，优先级高的先执行
    `run_pri`         tinyint                               NOT NULL COMMENT '运行优先级',
    `pri_upgradable`  bit         DEFAULT 0                 NOT NULL COMMENT '优先级可提升',       -- 执行时，优先级是否按照一定规则提升优先级 (当资源不足重试时，将优先级提升)

    `group`           varchar(32) DEFAULT ''                NOT NULL COMMENT '用户组',
    `user`            varchar(32) DEFAULT ''                NOT NULL COMMENT '用户',

    `cluster_app_id`  varchar(64) DEFAULT ''                NOT NULL COMMENT '集群应用ID',         -- K8s 按一定规则生成，直接写入数据库，YARN 的 ApplicationID 由 YARN 生成，后续回写到数据库
    `status`          varchar(16) DEFAULT 'CREATED'         NOT NULL COMMENT '状态',               -- 任务状态，CREATED、ACCEPTED、STARTING、START_FAILED、RUNNING、SUCCEED、FAILED、KILLED、CANCELED、UNKNOWN
    `submitted_at`    datetime                              NULL COMMENT '任务提交时间',           -- 对应 STARTING 的时间，提交执行的时候设置
    `started_at`      datetime                              NULL COMMENT '任务执行开始时间',       -- 对应首次 RUNNING 的时间，任务真正开始执行的时候设置
    `finished_at`     datetime                              NULL COMMENT '任务执行结束时间',       -- 对应结束状态的时间，任务结束的时候设置，不管是否成功

    `created_at`      datetime    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`      datetime    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE uniq_exec_from_app_uniq_id (`from_app`, `exec_uniq_id`),
    INDEX idx_exec_name (`exec_name`),
    INDEX idx_exec_cluster (`cluster`),
    INDEX idx_exec_engine (`engine_category`, `engine_version`),
    INDEX idx_exec_status (`status`),
    INDEX idx_exec_group (`group`),
    INDEX idx_exec_user (`user`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '引擎执行记录';

DROP TABLE IF EXISTS `maple`.maple_engine_execution_ext_info;
CREATE TABLE `maple`.`maple_engine_execution_ext_info`
(
    `id`        int  NOT NULL COMMENT '执行ID',
    `exec_conf` json NULL COMMENT '作业配置', -- 作业的配置信息
    `ext_info`  json NULL COMMENT '扩展信息', -- 作业的扩展信息，todo
    `exec_info` json NULL COMMENT '执行信息', -- 包括状态信息，状态变更时间等
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '引擎执行扩展信息';


-- region 暂时不用

DROP TABLE IF EXISTS `maple_engine_instance`;
CREATE TABLE `maple_engine_instance`
(
    `id`              int                                    NOT NULL AUTO_INCREMENT COMMENT '引擎ID',
    `application_id`  varchar(128) DEFAULT ''                NOT NULL COMMENT '程序ID',
    `cluster`         varchar(16)  DEFAULT ''                NOT NULL COMMENT '请求集群',
    `cluster_queue`   varchar(16)  DEFAULT ''                NOT NULL COMMENT '集群队列',
    `address`         varchar(256) DEFAULT ''                NOT NULL COMMENT '地址',
    `engine_category` varchar(16)  DEFAULT ''                NOT NULL COMMENT '引擎种类', # spark, flink
    `engine_version`  varchar(16)  DEFAULT ''                NOT NULL COMMENT '版本',
    `engine_type`     varchar(16)  DEFAULT ''                NOT NULL COMMENT '引擎类型 (once，resident)',
    `job_count`       int UNSIGNED DEFAULT 0                 NOT NULL COMMENT '执行的作业次数',
    `running_count`   int UNSIGNED DEFAULT 0                 NOT NULL COMMENT '执行中的作业数量',
    `heartbeat_time`  datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '心跳时间',
    `status`          varchar(16)  DEFAULT ''                NOT NULL COMMENT '状态 (SUBMITTED, ACCEPTED, RUNNING, FINISHED, FAILED, KILLED)',
    `job_cleaned`     tinyint      DEFAULT 0                 NOT NULL COMMENT '是否已清理作业',
    `group`           varchar(32)  DEFAULT ''                NOT NULL COMMENT '用户组',
    `user`            varchar(32)  DEFAULT ''                NOT NULL COMMENT '用户',

    `created_at`      datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`      datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    INDEX idx_engine_status (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '执行器实例';

DROP TABLE IF EXISTS `maple`.`maple_job_queue`;
CREATE TABLE `maple`.`maple_job_queue`
(
    `queue_name`      varchar(128) DEFAULT ''                NOT NULL COMMENT '作业队列名',
    `lock_name`       varchar(128) DEFAULT ''                NOT NULL COMMENT '作业队列锁名',
    `cluster`         varchar(16)  DEFAULT ''                NOT NULL COMMENT '提交集群',
    `cluster_queue`   varchar(128) DEFAULT ''                NOT NULL COMMENT '集群队列',
    `engine_category` varchar(16)  DEFAULT ''                NOT NULL COMMENT '引擎种类',
    `engine_version`  varchar(16)  DEFAULT ''                NOT NULL COMMENT '引擎版本',
    `from_app`        varchar(16)  DEFAULT ''                NOT NULL COMMENT '来源应用',
    `group`           varchar(16)  DEFAULT ''                NOT NULL COMMENT '用户组',
    `priority`        tinyint                                NOT NULL COMMENT '队列优先级',

    `created_at`      datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`      datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`queue_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '作业队列';

/**
`id`              int                                    NOT NULL AUTO_INCREMENT COMMENT '执行ID',
`exec_file`       varchar(256)                           NOT NULL COMMENT '执行文件',

`from_app`        varchar(16)                            NOT NULL COMMENT '来源应用',           -- 用于区分哪个应用提交，例如调度系统，实时平台等
`job_id`          varchar(32)                            NOT NULL COMMENT '作业ID',             -- 用于关联到某一个具体的作业配置
`biz_id`          varchar(32)                            NOT NULL COMMENT '执行批次ID',         -- 某个作业同一个业务时间运行的实例的集合ID (可能包含多次执行)
`exec_uniq_id`    varchar(32)                            NOT NULL COMMENT '应用作业执行唯一ID', -- 一个作业运行实例的ID，用于防止重复提交执行
`exec_name`       varchar(32)  DEFAULT ''                NOT NULL COMMENT '执行名称',           -- 作业的code，用于生成集群上的名称

`cluster`         varchar(32)                            NOT NULL COMMENT '提交集群',
`resource_group`  varchar(256) DEFAULT ''                NOT NULL COMMENT '集群资源组',         -- 如 YARN、Volcano 的 Queue，K8s 的 Namespace 等
`engine_category` varchar(16)  DEFAULT ''                NOT NULL COMMENT '引擎种类',           -- 如 Spark、Flink、Hive 等
`engine_version`  varchar(16)  DEFAULT ''                NOT NULL COMMENT '引擎版本',
`priority`        tinyint                                NOT NULL COMMENT '初始优先级',         -- 用于区分优先级，优先级高的先执行
`run_pri`         tinyint                                NOT NULL COMMENT '运行优先级',
`pri_upgradable`  bit          DEFAULT 0                 NOT NULL COMMENT '优先级可提升',       -- 执行时，优先级是否按照一定规则提升优先级 (当资源不足重试时，将优先级提升)

`group`           varchar(32)  DEFAULT ''                NOT NULL COMMENT '用户组',
`user`            varchar(32)  DEFAULT ''                NOT NULL COMMENT '用户',
 */

DROP TABLE IF EXISTS `maple`.`maple_sys_conf`;
CREATE TABLE `maple`.`maple_sys_conf`
(
    `conf_key`   varchar(64)                            NOT NULL COMMENT '配置键',
    `conf_value` json                                   NOT NULL COMMENT '配置值',
    `description`       varchar(512) DEFAULT ''                NOT NULL COMMENT '配置说明',

    `disabled`   tinyint      DEFAULT 0                 NOT NULL COMMENT '是否禁用',
    `created_by` int          DEFAULT 0                 NOT NULL COMMENT '创建人',
    `updated_by` int          DEFAULT 0                 NOT NULL COMMENT '修改人',
    `created_at` datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at` datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`conf_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT ='系统配置';


INSERT INTO maple_sys_conf(`conf_key`, `conf_value`, `desc`)
VALUES ('job_run_types', '[
  {
    "type_code": "spark-data-calc",
    "type_name": "Spark数据计算",
    "icon": "spark-data-calc",
    "color": "#1890ff",
    "engine_versions": [
      "3.*"
    ]
  },
  {
    "type_code": "flink-data-calc",
    "type_name": "Flink数据计算",
    "icon": "flink-data-calc",
    "color": "#1890ff",
    "engine_versions": [
      "1.17.*"
    ]
  }
]', '作业运行类型(例如：spark-sql, spark-data-calc等)');

INSERT INTO maple_sys_conf(`conf_key`, `conf_value`, `desc`)
VALUES ('engine_categories', '[
  {
    "value": "Spark",
    "label": "spark"
  },
  {
    "value": "Spark",
    "label": "spark"
  }
]', '引擎种类类型(例如：spark, flink, hive等)');

INSERT INTO maple_sys_conf(`conf_key`, `conf_value`, `desc`)
VALUES ('cluster_categories', '[
  {
    "value": "K8s",
    "label": "K8s"
  },
  {
    "value": "YARN",
    "label": "YARN"
  }
]', '集群种类类型(例如：K8s, YARN等)');

DROP TABLE IF EXISTS `maple`.`maple_job`;
CREATE TABLE `maple`.`maple_job`
(
    `id`          int                                    NOT NULL AUTO_INCREMENT COMMENT '作业ID',
    `job_name`    varchar(64)                            NOT NULL COMMENT '作业名',
    `desc`        varchar(256) DEFAULT ''                NOT NULL COMMENT '作业说明',
    `job_type`    varchar(8)                             NOT NULL COMMENT '作业类型', -- spark-sql, spark-data-calc, flink-data-calc
    `engine_id`   int                                    NOT NULL COMMENT '引擎ID',
    `owner`       varchar(32)  DEFAULT ''                NOT NULL COMMENT '作业负责人',
    `run_conf` json                                   NOT NULL COMMENT '执行配置',
    `job_conf`    json                                   NOT NULL COMMENT '作业配置', -- 启动参数，运行参数等，根据集群种类不同，配置不同

    `disabled`    tinyint      DEFAULT 0                 NOT NULL COMMENT '是否禁用',
    `created_by`  int          DEFAULT 0                 NOT NULL COMMENT '创建人',
    `updated_by`  int          DEFAULT 0                 NOT NULL COMMENT '修改人',
    `created_at`  datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`  datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    INDEX idx_job_name (`job_name`),
    INDEX idx_job_type (`job_type`),
    INDEX idx_job_cluster_id (`engine_id`),
    INDEX idx_job_owner (`owner`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '执行作业';

DROP TABLE IF EXISTS `maple`.`maple_job_ext_info`;
CREATE TABLE `maple`.`maple_job_ext_info`
(
    `id`      int        NOT NULL,
    `content` mediumtext NOT NULL COMMENT '执行配置',
    `result`  mediumtext NOT NULL COMMENT '执行结果',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '执行作业结果';

-- endregion