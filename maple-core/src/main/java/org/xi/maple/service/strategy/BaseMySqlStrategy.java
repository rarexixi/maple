package org.xi.maple.service.strategy;

public abstract class BaseMySqlStrategy extends NormalStrategy {

    @Override
    public String defaultDriver() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public String getDatabaseType() {
        return "mysql";
    }
}
