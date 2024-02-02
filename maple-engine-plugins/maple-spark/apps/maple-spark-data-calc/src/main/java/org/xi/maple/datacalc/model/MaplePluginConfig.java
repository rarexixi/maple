package org.xi.maple.datacalc.model;

import java.io.Serializable;

public abstract class MaplePluginConfig implements Serializable {

    protected boolean terminate = false;

    public boolean isTerminate() {
        return terminate;
    }

    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }
}
