package org.xi.maple.service.strategy;

public class DB2Strategy extends NormalStrategy {

    @Override
    public String defaultDriver() {
        return "com.ibm.db2.jcc.DB2Driver";
    }

    @Override
    public String getDatabaseType() {
        return "db2";
    }

    @Override
    protected String getParamsStartCharacter() {
        return ":";
    }

    @Override
    protected String getParamsSplitCharacter() {
        return ";";
    }
}
