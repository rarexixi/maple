create database maple default character set utf8;

# region datasource

drop table if exists `maple`.`maple_datasource_type`;
create table `maple`.`maple_datasource_type`
(
    `type_code`   varchar(32)                            not null comment '类型编码',
    `type_name`   varchar(256) default ''                not null comment '类型名称',
    `icon`        varchar(256) default ''                not null comment '图标地址',
    `classifier`  varchar(32)                            not null comment '分类',
    `versions`    varchar(256) default ''                not null comment '版本(多个版本用","隔开)',

    `deleted`     tinyint      default 0                 not null comment '是否删除',
    `create_user` int          default 0                 not null comment '创建人',
    `update_user` int          default 0                 not null comment '修改人',
    `create_time` datetime     default current_timestamp not null comment '创建时间',
    `update_time` datetime     default current_timestamp not null on update current_timestamp comment '更新时间',

    primary key (`type_code`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '数据源类型';

drop table if exists `maple`.`maple_datasource_config_key`;
create table `maple`.`maple_datasource_config_key`
(
    `id`              int                                    not null auto_increment,
    `datasource_type` varchar(32)  default ''                not null comment '类型编码',
    `versions`        varchar(128) default '*'               not null comment '对应版本(多个版本用","隔开)',
    `key_code`        varchar(32)  default ''                not null comment '配置编码',
    `key_name`        varchar(32)  default ''                not null comment '配置名',
    `key_order`       int          default 0                 not null comment '配置顺序',
    `default_value`   varchar(128) default ''                not null comment '默认值',
    `value_type`      varchar(32)  default 'STRING'          not null comment '类型',
    `required`        tinyint      default 0                 not null comment '是否必填',
    `value_regex`     varchar(256) default ''                not null comment '校验正则',
    `description`     varchar(256) default ''                not null comment '配置说明',

    `deleted`         tinyint      default 0                 not null comment '是否删除',
    `create_user`     int          default 0                 not null comment '创建人',
    `update_user`     int          default 0                 not null comment '修改人',
    `create_time`     datetime     default current_timestamp not null comment '创建时间',
    `update_time`     datetime     default current_timestamp not null on update current_timestamp comment '更新时间',

    primary key (`id`),
    unique `uniq_datasource_type_version_key` (`datasource_type`, `key_code`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '数据源配置项';

drop table if exists `maple`.`maple_datasource`;
create table `maple`.`maple_datasource`
(
    `id`                int                                not null auto_increment,
    `name`              varchar(32)                        not null comment '数据源名称',
    `description`       varchar(256)                       not null comment '数据源描述',
    `datasource_type`   varchar(32)                        not null comment '数据源类型',
    `version`           varchar(32)                        not null comment '数据源版本',
    `datasource_config` text                               not null comment '配置JSON',

    `deleted`           tinyint  default 0                 not null comment '是否删除',
    `create_user`       int      default 0                 not null comment '创建人',
    `update_user`       int      default 0                 not null comment '修改人',
    `create_time`       datetime default current_timestamp not null comment '创建时间',
    `update_time`       datetime default current_timestamp not null on update current_timestamp comment '更新时间',

    primary key (`id`),
    unique `uniq_datasource_name` (`name`),
    index `idx_datasource_type` (`datasource_type`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '数据源配置';

# endregion

# region application

drop table if exists `maple`.`maple_application`;
create table `maple`.`maple_application`
(
    `app_name`    varchar(32)                             not null comment '应用名称',
    `access_key`  varchar(256)  default ''                not null comment '应用访问密钥',
    `legal_hosts` varchar(1500) default '*'               not null comment '允许请求的IP',
    `webhooks`    varchar(2048) default '{}'              not null comment '回调接口',

    `deleted`     tinyint       default 0                 not null comment '是否删除',
    `create_user` int           default 0                 not null comment '创建人',
    `update_user` int           default 0                 not null comment '修改人',
    `create_time` datetime      default current_timestamp not null comment '创建时间',
    `update_time` datetime      default current_timestamp not null on update current_timestamp comment '更新时间',

    primary key (`app_name`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '访问程序';

# endregion

# region engine

drop table if exists `maple`.`maple_engine_category`;
create table `maple`.`maple_engine_category`
(
    `id`          int                                    not null auto_increment comment '引擎ID',
    `name`        varchar(32)  default ''                not null comment '类型名称',
    `version`     varchar(32)  default ''                not null comment '类型版本',
    `home`        varchar(256) default ''                not null comment '引擎目录',
    `group`       varchar(32)  default ''                not null comment '用户组',
    `user`        varchar(32)  default ''                not null comment '用户',

    `create_time` datetime     default current_timestamp not null comment '创建时间',
    `update_time` datetime     default current_timestamp not null on update current_timestamp comment '更新时间',

    primary key (`id`),
    index idx_engine_version (`name`, `version`),
    unique uniq_engine_version (`name`, `version`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '执行器实例';


drop table if exists `maple`.`maple_cluster`;
create table `maple`.`maple_cluster`
(
    `name`          varchar(32)                           not null comment '集群名称',
    `category`      varchar(16)                           not null comment '集群类型',
    `address`       varchar(256)                          not null comment '集群地址',
    `desc`          varchar(16) default ''                not null comment '集群说明',
    `configuration` text                                  not null comment '集群配置',

    `deleted`       tinyint     default 0                 not null comment '是否删除',
    `create_user`   int         default 0                 not null comment '创建人',
    `update_user`   int         default 0                 not null comment '修改人',
    `create_time`   datetime    default current_timestamp not null comment '创建时间',
    `update_time`   datetime    default current_timestamp not null on update current_timestamp comment '更新时间',

    primary key (`name`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '集群';


drop table if exists `maple`.`maple_engine_execution_queue`;
create table `maple`.`maple_engine_execution_queue`
(
    `queue_name`    varchar(128) default ''                not null comment '执行队列名',
    `cluster`       varchar(16)  default ''                not null comment '提交集群',
    `cluster_queue` varchar(128) default ''                not null comment '集群队列',
    `from_app`      varchar(16)  default ''                not null comment '来源应用',
    `group`         varchar(16)  default ''                not null comment '用户组',
    `priority`      tinyint                                not null comment '队列优先级',

    `create_time`   datetime     default current_timestamp not null comment '创建时间',
    `update_time`   datetime     default current_timestamp not null on update current_timestamp comment '更新时间',
    primary key (`queue_name`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '执行队列';

drop table if exists `maple`.`maple_engine_execution`;
create table `maple`.`maple_engine_execution`

(
    `id`              int                                    not null auto_increment comment '执行ID',
    `exec_file`       varchar(256)                           not null comment '执行文件',

    `from_app`        varchar(16)                            not null comment '来源应用',           -- 用于区分哪个应用提交，例如调度系统，实时平台等
    `job_id`          varchar(32)                            not null comment '作业ID',             -- 用于关联到某一个具体的作业配置
    `biz_id`          varchar(32)                            not null comment '执行批次ID',         -- 某个作业同一个业务时间运行的实例的集合ID (可能包含多次执行)
    `exec_uniq_id`    varchar(32)                            not null comment '应用作业执行唯一ID', -- 一个作业运行实例的ID，用于防止重复提交执行
    `exec_name`       varchar(32)  default ''                not null comment '执行名称',           -- 作业的code，用于生成集群上的名称

    `cluster`         varchar(32)                            not null comment '提交集群',
    `resource_group`  varchar(256) default ''                not null comment '集群资源组',         -- 如 YARN、Volcano 的 Queue，K8s 的 Namespace 等
    `engine_category` varchar(16)  default ''                not null comment '引擎种类',           -- 如 Spark、Flink、Hive 等
    `engine_version`  varchar(16)  default ''                not null comment '引擎版本',
    `priority`        tinyint                                not null comment '初始优先级',         -- 用于区分优先级，优先级高的先执行
    `run_pri`         tinyint                                not null comment '运行优先级',
    `pri_upgradable`  bit          default 0                 not null comment '优先级可提升',       -- 执行时，优先级是否按照一定规则提升优先级 (当资源不足重试时，将优先级提升)

    `group`           varchar(32)  default ''                not null comment '用户组',
    `user`            varchar(32)  default ''                not null comment '用户',

    `status`          varchar(16)  default 'CREATED'         not null comment '状态',               -- 任务状态，CREATED、ACCEPTED、STARTING、START_FAILED、RUNNING、SUCCEED、FAILED、KILLED、CANCELED、UNKNOWN
    `cluster_app_id`  varchar(64)  default ''                not null comment '集群应用ID',         -- K8s 按一定规则生成，直接写入数据库，YARN 的 ApplicationID 由 YARN 生成，后续回写到数据库

    `starting_time`   datetime                               null comment '任务提交时间',           -- 对应 STARTING 的时间，提交执行的时候设置
    `running_time`    datetime                               null comment '任务执行开始时间',       -- 对应首次 RUNNING 的时间，任务真正开始执行的时候设置
    `finish_time`     datetime                               null comment '任务执行结束时间',       -- 对应结束状态的时间，任务结束的时候设置，不管是否成功
    `create_time`     datetime     default current_timestamp not null comment '创建时间',
    `update_time`     datetime     default current_timestamp not null on update current_timestamp comment '更新时间',

    primary key (`id`),
    unique uniq_exec_from_app_uniq_id (`from_app`, `exec_uniq_id`),
    index idx_exec_name (`exec_name`),
    index idx_exec_cluster (`cluster`),
    index idx_exec_engine (`engine_category`, `engine_version`),
    index idx_exec_status (`status`),
    index idx_exec_group (`group`),
    index idx_exec_user (`user`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '引擎执行记录';

drop table if exists `maple`.maple_engine_execution_ext_info;
create table `maple`.`maple_engine_execution_ext_info`
(
    `id`            int  not null comment '执行ID',
    `configuration` text null comment '作业配置', -- 作业的配置信息
    `ext_info`      text null comment '扩展信息', -- 作业的扩展信息，todo
    `exec_info`     text null comment '执行信息', -- 包括状态信息，状态变更时间等
    primary key (`id`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '引擎执行扩展信息';


-- region 暂时不用

drop table if exists `maple_engine_instance`;
create table `maple_engine_instance`
(
    `id`              int                                    not null auto_increment comment '引擎ID',
    `application_id`  varchar(128) default ''                not null comment '程序ID',
    `cluster`         varchar(16)  default ''                not null comment '请求集群',
    `cluster_queue`   varchar(16)  default ''                not null comment '集群队列',
    `address`         varchar(256) default ''                not null comment '地址',
    `engine_category` varchar(16)  default ''                not null comment '引擎种类', # spark, flink
    `engine_version`  varchar(16)  default ''                not null comment '版本',
    `engine_type`     varchar(16)  default ''                not null comment '引擎类型 (once，resident)',
    `job_count`       int unsigned default 0                 not null comment '执行的作业次数',
    `running_count`   int unsigned default 0                 not null comment '执行中的作业数量',
    `heartbeat_time`  datetime     default current_timestamp not null comment '心跳时间',
    `status`          varchar(16)  default ''                not null comment '状态 (SUBMITTED, ACCEPTED, RUNNING, FINISHED, FAILED, KILLED)',
    `job_cleaned`     tinyint      default 0                 not null comment '是否已清理作业',
    `group`           varchar(32)  default ''                not null comment '用户组',
    `user`            varchar(32)  default ''                not null comment '用户',

    `create_time`     datetime     default current_timestamp not null comment '创建时间',
    `update_time`     datetime     default current_timestamp not null on update current_timestamp comment '更新时间',

    primary key (`id`),
    index idx_engine_status (`status`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '执行器实例';

drop table if exists `maple`.`maple_job_queue`;
create table `maple`.`maple_job_queue`
(
    `queue_name`      varchar(128) default ''                not null comment '作业队列名',
    `lock_name`       varchar(128) default ''                not null comment '作业队列锁名',
    `cluster`         varchar(16)  default ''                not null comment '提交集群',
    `cluster_queue`   varchar(128) default ''                not null comment '集群队列',
    `engine_category` varchar(16)  default ''                not null comment '引擎种类',
    `engine_version`  varchar(16)  default ''                not null comment '引擎版本',
    `from_app`        varchar(16)  default ''                not null comment '来源应用',
    `group`           varchar(16)  default ''                not null comment '用户组',
    `priority`        tinyint                                not null comment '队列优先级',

    `create_time`     datetime     default current_timestamp not null comment '创建时间',
    `update_time`     datetime     default current_timestamp not null on update current_timestamp comment '更新时间',
    primary key (`queue_name`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '作业队列';

drop table if exists `maple`.`maple_job`;
create table `maple`.`maple_job`
(
    `id`              int                                    not null auto_increment comment '作业ID',
    `job_name`        varchar(256) default ''                not null comment '作业名',
    `job_type`        varchar(8)   default ''                not null comment '作业类型 (sync, async)',
    `unique_id`       varchar(32)                            not null comment '作业唯一标识',
    `job_comment`     varchar(64)  default ''                not null comment '作业说明',
    `from_app`        varchar(16)                            not null comment '来源应用',
    `cluster`         varchar(16)                            not null comment '提交集群',
    `cluster_queue`   varchar(128) default ''                not null comment '集群队列',
    `engine_id`       int          default 0                 not null comment '引擎ID',
    `engine_category` varchar(16)  default ''                not null comment '引擎种类',
    `engine_version`  varchar(16)  default ''                not null comment '引擎版本',
    `priority`        tinyint                                not null comment '初始优先级',
    `run_priority`    tinyint                                not null comment '运行优先级',
    `content_type`    varchar(8)   default 'text'            not null comment '执行内容类型 (text, path)',
    `result_type`     varchar(8)   default 'text'            not null comment '执行结果类型 (text, path)',
    `status`          varchar(16)  default 'SUBMITTED'       not null comment '状态 (SUBMITTED, ACCEPTED, RUNNING, SUCCEED, FAILED, KILLED)',
    `group`           varchar(32)  default ''                not null comment '用户组',
    `user`            varchar(32)  default ''                not null comment '用户',
    `configuration`   text                                   not null comment '作业配置',
    `ext_info`        text                                   not null comment '扩展信息',

    `create_time`     datetime     default current_timestamp not null comment '创建时间',
    `update_time`     datetime     default current_timestamp not null on update current_timestamp comment '更新时间',

    primary key (`id`),
    index idx_job_name (`job_name`),
    index idx_job_from_app (`from_app`),
    index idx_job_unique_id (`unique_id`),
    index idx_job_engine (`engine_id`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '执行作业';

create table `maple_job_ext_info`
(
    `id`      int        not null,
    `content` mediumtext not null comment '执行内容',
    `result`  mediumtext not null comment '执行结果',
    primary key (`id`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '执行作业结果';

-- endregion