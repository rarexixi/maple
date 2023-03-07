package org.xi.maple.service.strategy;

public class ClickHouseStrategy extends NormalStrategy {

    @Override
    public String defaultDriver() {
        return "ru.yandex.clickhouse.ClickHouseDriver";
        // return "com.clickhouse.jdbc.ClickHouseDriver";  // >= 0.3.2
    }

    @Override
    public String getDatabaseType() {
        return "clickhouse";
    }
}
