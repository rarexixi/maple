create table `job`
(
    `id`           int                                    not null auto_increment,
    `job_name`     varchar(256) default ''                not null comment '',
    `job_type`     varchar(32)  default ''                not null comment '作业类型 (sync, async)',
    `content_type` varchar(32)  default 'text'            not null comment '执行内容类型 (text, path)',
    `result_type`  varchar(32)  default 'text'            not null comment '执行结果类型 (text, path)',
    `status`       varchar(16)                            not null comment '状态',

    `create_user`  int          default 0                 not null comment '创建人',
    `create_time`  datetime     default current_timestamp not null comment '创建时间',
    `update_time`  datetime     default current_timestamp not null on update current_timestamp comment '更新时间',

    primary key (`id`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '执行作业';

create table `job_content`
(
    `id`      int        not null,
    `content` mediumtext not null comment '执行内容',
    primary key (`id`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '执行作业内容';

create table `job_result`
(
    `id`     int        not null,
    `result` mediumtext not null comment '执行结果',
    primary key (`id`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '执行作业结果';


create table `instance`
(
    `address`        varchar(128) default ''                not null comment '地址',
    `type`           varchar(32)  default ''                not null comment '类型 (spark, flink)',
    `last_job_id`    int          default 0                 not null comment '最后执行的作业ID',
    `job_num`        int          default 0                 not null comment '执行的作业次数',
    `heartbeat_time` datetime     default current_timestamp not null comment '分类',

    `create_user`    int          default 0                 not null comment '创建人',
    `create_time`    datetime     default current_timestamp not null comment '创建时间',
    `update_time`    datetime     default current_timestamp not null on update current_timestamp comment '更新时间',
    primary key (`address`)
) engine = InnoDB
  default charset = utf8
  collate = utf8_unicode_ci
    comment = '执行器实例';