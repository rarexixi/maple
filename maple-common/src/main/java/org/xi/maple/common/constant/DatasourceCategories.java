package org.xi.maple.common.constant;

import lombok.Getter;

@Getter
public enum DatasourceCategories {

    RDBMS("rdbms", "RDBMS"),
    HIVE("hive", "Hive"),
    KAFKA("kafka", "Kafka"),
    ELASTICSEARCH("elasticsearch", "Elasticsearch"),
    HIVE3("hive3", "Hive3"),
    CLICKHOUSE("clickhouse", "ClickHouse"),
    MONGODB("mongodb", "MongoDB"),
    REDIS("redis", "Redis");

    final String value;
    final String name;

    DatasourceCategories(String value, String name) {
        this.value = value;
        this.name = name;
    }
}
