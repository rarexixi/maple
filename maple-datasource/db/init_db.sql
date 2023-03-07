create database maple_datasource;
use maple_datasource;

drop table if exists `maple_datasource_type`;
create table `maple_datasource_type`
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
    comment ='数据源类型';

drop table if exists `maple_datasource_config_key`;
create table `maple_datasource_config_key`
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
    comment ='数据源配置项';

drop table if exists `maple_datasource`;
create table `maple_datasource`
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
    comment ='数据源配置';

replace into maple_datasource.maple_datasource_type (type_code, type_name, icon, classifier, versions)
values ('clickhouse', 'ClickHouse', '', '数据库', '1.1,18,19,20,21,22'),
       ('db2', 'DB2', '', '数据库', '9.7,10,11,12,13'),
       ('doris', 'Doris', '', '数据库', '1.1,1.2'),
       ('mysql', 'MySQL', '', '数据库', '5.6,5.7,8.0'),
       ('oracle', 'Oracle', '', '数据库', '9i,10g,11g,12c,18c,19c,21c'),
       ('postgresql', 'PostgreSQL', '', '数据库', '10,11,12,13,14,15'),
       ('sqlserver', 'SqlServer', '', '数据库', '2016,2017,2019,2022'),
       ('tidb', 'TiDB', '', '数据库', '4,5,6.1,6.5');

INSERT INTO maple_datasource.maple_datasource_config_key (datasource_type, versions, key_code, key_name, key_order, default_value, value_type, required,
                                                          value_regex, description)
VALUES ('mysql', '*', 'driverClassName', '驱动类名', 1, 'com.mysql.jdbc.Driver', 'STRING', 0, '', ''),
       ('mysql', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('mysql', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('mysql', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('mysql', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('mysql', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', ''),
       ('tidb', '*', 'driverClassName', '驱动类名', 1, 'com.mysql.jdbc.Driver', 'STRING', 0, '', ''),
       ('tidb', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('tidb', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('tidb', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('tidb', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('tidb', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', ''),
       ('doris', '*', 'driverClassName', '驱动类名', 1, 'com.mysql.jdbc.Driver', 'STRING', 0, '', ''),
       ('doris', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('doris', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('doris', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('doris', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('doris', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', ''),
       ('clickhouse', '*', 'driverClassName', '驱动类名', 1, 'ru.yandex.clickhouse.ClickHouseDriver', 'STRING', 0, '', ''),
       ('clickhouse', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('clickhouse', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('clickhouse', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('clickhouse', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('clickhouse', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', ''),
       ('postgresql', '*', 'driverClassName', '驱动类名', 1, 'org.postgresql.Driver', 'STRING', 0, '', ''),
       ('postgresql', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('postgresql', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('postgresql', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('postgresql', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('postgresql', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', ''),
       ('db2', '*', 'driverClassName', '驱动类名', 1, 'com.ibm.db2.jcc.DB2Driver', 'STRING', 0, '', ''),
       ('db2', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('db2', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('db2', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('db2', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('db2', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', ''),
       ('oracle', '*', 'driverClassName', '驱动类名', 1, 'oracle.jdbc.driver.OracleDriver', 'STRING', 0, '', ''),
       ('oracle', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...]'),
       ('oracle', '*', 'params', '连接参数', 3, '', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('oracle', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('oracle', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('oracle', '9i,10g', 'sid', 'SID', 6, '', 'STRING', 0, '', ''),
       ('oracle', '11g,12c,18c,19c,21c', 'serviceName', 'service_name', 7, '', 'STRING', 0, '', ''),
       ('oracle', '11g,12c,18c,19c,21c', 'server', 'server', 8, '', 'STRING', 0, '', ''),
       ('oracle', '11g,12c,18c,19c,21c', 'instance', '实例名', 9, '', 'STRING', 0, '', ''),
       ('sqlserver', '*', 'driverClassName', '驱动类名', 1, 'com.microsoft.sqlserver.jdbc.SQLServerDriver', 'STRING', 0, '', ''),
       ('sqlserver', '*', 'address', '地址', 2, '', 'STRING', 0, '', 'host1:port1[,host2:port2...][\\instance]'),
       ('sqlserver', '*', 'params', '连接参数', 3, '{"encrypt":"true","trustServerCertificate":"true"}', 'JSON', 0, '', 'JSON格式: {"param":"value"}'),
       ('sqlserver', '*', 'username', '用户名', 4, '', 'STRING', 0, '^[0-9A-Za-z_-]+$', ''),
       ('sqlserver', '*', 'password', '密码', 5, '', 'PASSWORD', 0, '', ''),
       ('sqlserver', '*', 'databaseName', '数据库名', 6, '', 'STRING', 0, '', '');


