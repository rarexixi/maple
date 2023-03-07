package org.xi.maple.service.strategy;

public abstract class BasePostgreSqlStrategy extends NormalStrategy {

    @Override
    public String defaultDriver() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getDatabaseType() {
        return "postgresql";
    }
}
