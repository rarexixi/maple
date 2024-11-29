package org.xi.maple.common.model.db.condition;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ColumnsCondition implements Serializable {

    final List<String> columns = new LinkedList<>();

    public List<String> getColumns() {
        return columns.isEmpty() ? null : columns;
    }

    public void setSelectColumns(String... columns) {
        Collections.addAll(this.columns, columns);
    }
}
