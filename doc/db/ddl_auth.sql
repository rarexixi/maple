DROP TABLE IF EXISTS `maple`.`maple_user`;
CREATE TABLE `maple`.`maple_user`
(
    `id`           int                                    NOT NULL AUTO_INCREMENT,
    `username`     varchar(32)                            NOT NULL COMMENT '用户名',
    `name`         varchar(32)  DEFAULT ''                NOT NULL COMMENT '姓名',
    `password`     varchar(256)                           NOT NULL COMMENT '密码',
    `email`        varchar(256) DEFAULT ''                NOT NULL COMMENT '电子邮箱',
    `phone_number` varchar(32)  DEFAULT ''                NOT NULL COMMENT '手机号',
    `picture`      varchar(256) DEFAULT ''                NOT NULL COMMENT '头像',
    `gender`       tinyint      DEFAULT 0                 NOT NULL COMMENT '性别(0:未知,1:男,2:女)',

    `disabled`     tinyint      DEFAULT 0                 NOT NULL COMMENT '是否禁用',
    `created_by`   int          DEFAULT 0                 NOT NULL COMMENT '创建人',
    `updated_by`   int          DEFAULT 0                 NOT NULL COMMENT '修改人',
    `created_at`   datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at`   datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE `uniq_user_username` (`name`),
    UNIQUE `uniq_user_email` (`email`),
    UNIQUE `uniq_user_phone` (`phone_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '用户';

INSERT INTO maple.maple_user (id, username, name, password, email)
VALUES (1, 'admin', '管理员', 'admin', '');

DROP TABLE IF EXISTS `maple`.`maple_permission`;
CREATE TABLE `maple`.`maple_permission`
(
    `id`         int                                   NOT NULL AUTO_INCREMENT,
    `code`       varchar(32)                           NOT NULL COMMENT '权限名称',
    `description`       varchar(32) DEFAULT ''                NOT NULL COMMENT '权限描述',

    `disabled`   tinyint     DEFAULT 0                 NOT NULL COMMENT '是否禁用',
    `created_by` int         DEFAULT 0                 NOT NULL COMMENT '创建人',
    `updated_by` int         DEFAULT 0                 NOT NULL COMMENT '修改人',
    `created_at` datetime    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updated_at` datetime    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE `uniq_user_username` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '权限';

DROP TABLE IF EXISTS `maple`.`maple_user_permission`;
CREATE TABLE `maple`.`maple_user_permission`
(
    `user_id` int NOT NULL COMMENT '用户ID',
    `perm_id` int NOT NULL COMMENT '权限ID',

    PRIMARY KEY (`user_id`, `perm_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci COMMENT = '用户权限';


INSERT INTO maple.maple_user_permission (user_id, perm_id)
VALUES (1, 1);

INSERT INTO maple.maple_permission (id, code, `description`)
VALUES (1, 'user:permissions', '用户权限查询');

SELECT MP.`code`
FROM `maple_permission` MP
         JOIN `maple_user_permission` MUP ON MP.`id` = MUP.`perm_id`
    where
    MUP.`user_id` = 1





