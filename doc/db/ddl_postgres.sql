CREATE DATABASE maple WITH ENCODING = 'UTF8' LC_COLLATE = 'zh_CN.UTF-8' LC_CTYPE = 'zh_CN.UTF-8' TEMPLATE = template0;

https://jable.tv/SEARCH/%E7%B7%8A%E7%B8%9B/



COMMENT ON DATABASE maple IS 'maple数据库';

DROP TABLE IF EXISTS "maple_user";

CREATE TABLE maple_user
(
    id         SERIAL       NOT NULL,
    username   VARCHAR(32)  NOT NULL,
    password   VARCHAR(256) NOT NULL DEFAULT '',
    email      VARCHAR(256) NOT NULL DEFAULT '',
    mobile     VARCHAR(32)  NOT NULL,
    name       VARCHAR(32)  NOT NULL DEFAULT '',

    disabled   SMALLINT     NOT NULL DEFAULT 0,
    created_by INT          NOT NULL DEFAULT 0,
    updated_by INT          NOT NULL DEFAULT 0,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT maple_user_pkey PRIMARY KEY (id)
);

-- 添加字段备注
COMMENT ON COLUMN maple_user.id IS '主键ID';
COMMENT ON COLUMN maple_user.username IS '用户名';
COMMENT ON COLUMN maple_user.password IS '密码';
COMMENT ON COLUMN maple_user.email IS '电子邮箱';
COMMENT ON COLUMN maple_user.mobile IS '手机号';
COMMENT ON COLUMN maple_user.name IS '姓名';
COMMENT ON COLUMN maple_user.disabled IS '是否禁用';
COMMENT ON COLUMN maple_user.created_by IS '创建人';
COMMENT ON COLUMN maple_user.updated_by IS '修改人';
COMMENT ON COLUMN maple_user.created_at IS '创建时间';
COMMENT ON COLUMN maple_user.updated_at IS '更新时间';

-- 添加表备注
COMMENT ON TABLE maple_user IS '用户';

SELECT a.attname AS column_name, d.description AS comment
FROM pg_class c
         JOIN pg_attribute a ON c.oid = a.attrelid
         LEFT JOIN pg_description d ON c.oid = d.objoid AND a.attnum = d.objsubid;

SELECT *
FROM pg_database c
WHERE datistemplate = FALSE;

COMMENT ON DATABASE maple IS 'maple数据库';

SELECT d.datname AS database_name, pg_catalog.obj_description(d.oid, 'pg_database') AS database_comment
FROM pg_database d
WHERE datistemplate = FALSE
ORDER BY d.datname;


SELECT n.nspname AS schema_name, d.description AS schema_comment
FROM pg_namespace n
         LEFT JOIN pg_description d ON d.objoid = n.oid AND d.objsubid = 0;

SELECT t.table_name AS table_name, d.description AS table_comment
FROM information_schema.tables t
         LEFT JOIN pg_description d ON d.objsubid = 0 AND d.objoid = t.table_name::regclass
WHERE t.table_catalog = 'maple'
  AND t.table_schema = 'public'
  AND t.table_type = 'BASE TABLE';

SELECT c.column_name      AS columnName,
       c.column_default   AS columnDefault,
       c.is_nullable      AS isNullable,
       c.udt_name         AS dataType,
       c.column_default   AS column_default,
       d.description      AS columnComment,
       c.ordinal_position AS columnPosition
FROM information_schema.columns c
         LEFT JOIN pg_description d ON d.objoid = c.table_name::regclass AND d.objsubid = c.ordinal_position
WHERE c.table_catalog = 'maple'
  AND c.table_schema = 'public'
  AND c.table_name = 'maple_user'
ORDER BY c.ordinal_position;

SELECT i.schemaname AS schema_name, i.tablename AS table_name, i.indexname AS index_name, d.description AS index_comment
FROM pg_indexes i
         LEFT JOIN pg_description d ON d.objoid = i.indexname::regclass AND d.objsubid = 0
WHERE i.schemaname = 'public' -- 可以根据需要更改 schema 名称
ORDER BY i.schemaname, i.tablename, i.indexname;

SELECT *
FROM pg_indexes i
WHERE i.schemaname = 'public';

SELECT i.schemaname           AS schema_name,
       i.tablename            AS table_name,
       i.indexname            AS index_name,
       i.indexdef             AS index_definition,
       d.description          AS index_comment,
       CASE WHEN i.indexdef LIKE '%UNIQUE%' THEN 'unique'
            WHEN i.indexdef LIKE '%PRIMARY KEY%' THEN 'primary key'
            ELSE 'normal' END AS index_type
FROM pg_indexes i
         LEFT JOIN pg_description d ON d.objoid = i.indexname::regclass AND d.objsubid = 0
WHERE i.schemaname = 'public'
ORDER BY i.schemaname, i.tablename, i.indexname;

SELECT *
FROM information_schema.table_constraints
WHERE table_name = 'maple_user';

SELECT tc.table_schema    AS schema_name,
       tc.table_name      AS table_name,
       kcu.column_name    AS column_name,
       tc.constraint_name AS constraint_name
FROM information_schema.table_constraints tc
         JOIN information_schema.key_column_usage kcu ON tc.constraint_name = kcu.constraint_name
WHERE tc.constraint_type = 'PRIMARY KEY'
  AND tc.table_schema = 'public'
  AND tc.table_name = 'maple_user'
ORDER BY tc.table_schema, tc.table_name, kcu.ordinal_position;


SELECT index_name,
       STRING_AGG(DISTINCT column_name, ' ', sort_order, ', ') AS column_names,
       MIN(index_type)                                         AS index_type
FROM (
         SELECT i.relname                                                                                            AS index_name,
                a.attname                                                                                            AS column_name,
                idx.idx                                                                                              AS column_position,
                CASE WHEN ix.indoption[idx - 1] & 1 = 1 THEN 'DESC' ELSE 'ASC' END                                   AS sort_order,
                CASE WHEN ix.indisprimary THEN 'PRIMARY KEY'
                     WHEN ix.indisunique THEN 'UNIQUE'
                     ELSE 'NON-UNIQUE' END                                                                           AS index_type
         FROM pg_class t
                  JOIN pg_index ix ON t.oid = ix.indrelid
                  JOIN pg_class i ON i.oid = ix.indexrelid
                  JOIN pg_attribute a ON a.attrelid = t.oid AND a.attnum = ANY (ix.indkey)
                  JOIN UNNEST(ix.indkey) WITH ORDINALITY AS idx(key, idx) ON a.attnum = key
         WHERE t.relkind = 'r'
           AND t.relname = 'maple_user'
     ) AS incnit
GROUP BY index_name;





















































CREATE TABLE test_table1
(
    serial_c        serial        NOT NULL,
    bigint_c        bigint        NOT NULL,
    bit_c           bit           NOT NULL,
    boolean_c       boolean       NOT NULL,
    box_c           box           NOT NULL,
    bytea_c         bytea         NOT NULL,
    character_c     character(10) NOT NULL,
    cidr_c          cidr          NOT NULL,
    circle_c        circle        NOT NULL,
    date_c          date          NOT NULL,
    double_c        float8        NOT NULL,
    inet_c          inet          NOT NULL,
    integer_c       integer       NOT NULL,
    interval_c      interval      NOT NULL,
    json_c          json          NOT NULL,
    jsonb_c         jsonb         NOT NULL,
    line_c          line          NOT NULL,
    lseg_c          lseg          NOT NULL,
    macaddr_c       macaddr       NOT NULL,
    macaddr8_c      macaddr8      NOT NULL,
    money_c         money         NOT NULL,
    numeric_c       numeric       NOT NULL,
    path_c          path          NOT NULL,
    pg_lsn_c        pg_lsn        NOT NULL,
    pg_snapshot_c   pg_snapshot   NOT NULL,
    point_c         point         NOT NULL,
    polygon_c       polygon       NOT NULL,
    real_c          real          NOT NULL,
    smallint_c      smallint      NOT NULL,
    smallserial_c   smallserial   NOT NULL,
    text_c          text          NOT NULL,
    time_c          time          NOT NULL,
    timestamp_c     timestamp     NOT NULL,
    tsquery_c       tsquery       NOT NULL,
    tsvector_c      tsvector      NOT NULL,
    txid_snapshot_c txid_snapshot NOT NULL,
    uuid_c          uuid          NOT NULL,
    xml_c           xml           NOT NULL,
    PRIMARY KEY (serial_c)
);

CREATE TABLE test_table2
(
bigserial_c bigserial,
bit_c bit varying(3),
character_c character varying(10),
time_c time,
timestamp_c timestamp,
PRIMARY KEY (bigserial_c)
);