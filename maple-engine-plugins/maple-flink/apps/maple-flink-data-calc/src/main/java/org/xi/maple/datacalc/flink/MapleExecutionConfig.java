package org.xi.maple.datacalc.flink;

import org.apache.flink.table.api.StatementSet;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.catalog.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.common.util.VariableUtils;
import org.xi.maple.datacalc.flink.api.ResultTableConfig;
import org.xi.maple.datacalc.flink.exception.ConfigRuntimeException;
import org.xi.maple.datacalc.flink.model.*;
import org.xi.maple.datacalc.flink.util.ConfigUtil;
import org.xi.maple.datacalc.flink.util.TableUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

public class MapleExecutionConfig<T extends MapleData> {

    private static final Logger logger = LoggerFactory.getLogger(MapleExecutionConfig.class);

    final TableEnvironment tableEnv;

    final T mapleData;

    final Map<String, String> gv;

    final Set<String> registerTableSet;

    public MapleExecutionConfig(TableEnvironment tableEnv, T mapleData) {
        this.tableEnv = tableEnv;
        this.mapleData = mapleData;
        this.gv = new HashMap<>();
        mapleData.getVariables().forEach((k, v) -> gv.put(k, VariableUtils.replaceVariables(v, mapleData.getVariables())));
        this.registerTableSet = new HashSet<>();
    }

    public void execute() {
        if (this.mapleData instanceof MapleGroupData) {
            executeGroup((MapleGroupData) this.mapleData);
        } else if (this.mapleData instanceof MapleArrayData) {
            executeArray((MapleArrayData) this.mapleData);
        } else {
            throw new ConfigRuntimeException("MapleData type [" + this.mapleData.getClass() + "] is not supported");
        }
    }

    private void executeGroup(MapleGroupData mapleData) {
        int initialCapacity = mapleData.getSources().length + mapleData.getTransformations().length + mapleData.getSinks().length;
        List<MaplePluginConfig> executions = new ArrayList<>(initialCapacity);
        for (MapleDataConfig dc : mapleData.getSources()) {
            executions.add(ConfigUtil.createConfig(ConfigUtil.ExecutionType.SOURCE, dc.getName(), dc.getConfig()));
        }
        for (MapleDataConfig dc : mapleData.getTransformations()) {
            executions.add(ConfigUtil.createConfig(ConfigUtil.ExecutionType.TRANSFORM, dc.getName(), dc.getConfig()));
        }
        for (MapleDataConfig dc : mapleData.getSinks()) {
            executions.add(ConfigUtil.createConfig(ConfigUtil.ExecutionType.SINK, dc.getName(), dc.getConfig()));
        }
        executePlugins(executions);
    }

    private void executeArray(MapleArrayData mapleData) {
        if (mapleData.getPlugins() == null || mapleData.getPlugins().length == 0) {
            throw new ConfigRuntimeException("plugins is empty");
        }
        List<MaplePluginConfig> executions = new ArrayList<>(mapleData.getPlugins().length);
        for (MapleDataConfig dc : mapleData.getPlugins()) {
            executions.add(ConfigUtil.createConfig(dc.getType(), dc.getName(), dc.getConfig()));
        }
        executePlugins(executions);
    }

    private void executePlugins(List<MaplePluginConfig> executions) {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            for (MaplePluginConfig executionConfig : executions) {
                if (!checkPluginConfig(validator, executionConfig)) {
                    throw new ConfigRuntimeException("Config data valid failed");
                }
            }
        }
        StatementSet statementSet = tableEnv.createStatementSet();
        for (MaplePluginConfig executionConfig : executions) {
            if (executionConfig instanceof CustomCreateTableConfig) {
                CustomCreateTableConfig tableDefinition = (CustomCreateTableConfig) executionConfig;
                tableEnv.executeSql(tableDefinition.getCreateSql());
            } else if (executionConfig instanceof CreateTableConfig) {
                CreateTableConfig createTableConfig = (CreateTableConfig) executionConfig;
                TableDescriptor tableDescriptor = TableUtils.getTableDescriptor(createTableConfig);
                tableEnv.createTemporaryTable(createTableConfig.getResultTable(), tableDescriptor);
                if (createTableConfig instanceof InsertTableConfig) {
                    InsertTableConfig insertTableConfig = (InsertTableConfig) createTableConfig;
                    String selectSourceTable = insertTableConfig.getSelectSourceTable();
                    Table sourceTable = tableEnv.sqlQuery(selectSourceTable);
                    List<Column> columns = sourceTable.getResolvedSchema().getColumns(); // todo check
                    if (columns.size() != insertTableConfig.getColumns().size()) {
                        throw new ConfigRuntimeException("The number of columns in the source table and the target table is inconsistent");
                    }
                    statementSet.addInsertSql(insertTableConfig.getInsertSql());
                }
            } else if (executionConfig instanceof CreateViewConfig) {
                CreateViewConfig createViewConfig = (CreateViewConfig) executionConfig;
                Table view = tableEnv.sqlQuery(createViewConfig.getSelectSql());
                tableEnv.createTemporaryView(createViewConfig.getViewName(), view);
            } else if (executionConfig instanceof CustomInsertTableConfig) {
                CustomInsertTableConfig insertTableConfig = (CustomInsertTableConfig) executionConfig;
                statementSet.addInsertSql(insertTableConfig.getInsertSql());
            }
            if (executionConfig.isTerminate()) {
                break;
            }
        }
        statementSet.execute(); // todo
    }

    private boolean checkPluginConfig(Validator validator, MaplePluginConfig config) {
        Set<ConstraintViolation<MaplePluginConfig>> validate = validator.validate(config);
        boolean success = true;
        if (validate.size() > 0) {
            logger.error("Configuration check error, {}", JsonUtils.toJsonString(config, ""));
            for (ConstraintViolation<MaplePluginConfig> violation : validate) {
                if (violation.getMessageTemplate().startsWith("{") && violation.getMessageTemplate().endsWith("}")) {
                    logger.error("[{}] {}", violation.getPropertyPath(), violation.getMessage());
                } else {
                    logger.error(violation.getMessage());
                }
            }
            return false;
        }
        if (config instanceof ResultTableConfig) {
            ResultTableConfig c = (ResultTableConfig) config;
            if (registerTableSet.contains(c.getResultTable())) {
                logger.error("Result table or view [{}] cannot be duplicate", c.getResultTable());
                success = false;
            } else {
                registerTableSet.add(c.getResultTable());
            }
        }
        return success;
    }
}
