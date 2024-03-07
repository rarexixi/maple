package org.xi.maple.datacalc.flink;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.table.api.StatementSet;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.common.util.VariableUtils;
import org.xi.maple.datacalc.flink.api.MaplePlugin;
import org.xi.maple.datacalc.flink.api.TableInsert;
import org.xi.maple.datacalc.flink.exception.ConfigRuntimeException;
import org.xi.maple.datacalc.flink.model.*;
import org.xi.maple.datacalc.flink.util.PluginUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

public class MapleExecution<T extends MapleData> {

    private static final Logger logger = LoggerFactory.getLogger(MapleExecution.class);

    final TableEnvironment tableEnv;

    final T mapleData;

    final Map<String, String> gv;

    final Set<String> registerTableSet;

    public MapleExecution(TableEnvironment tableEnv, T mapleData) {
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
        List<MaplePlugin> executions = new ArrayList<>();
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            final Validator validator = validatorFactory.getValidator();
            for (MapleDataConfig dc : mapleData.getSources()) {
                executions.add(getAndCheckExecution(validator, PluginUtil.ExecutionType.SOURCE, dc));
            }
            for (MapleDataConfig dc : mapleData.getTransformations()) {
                executions.add(getAndCheckExecution(validator, PluginUtil.ExecutionType.TRANSFORM, dc));
            }
            for (MapleDataConfig dc : mapleData.getSinks()) {
                executions.add(getAndCheckExecution(validator, PluginUtil.ExecutionType.SINK, dc));
            }
        }
        executePlugins(executions);
    }

    private MaplePlugin<MaplePluginConfig> getAndCheckExecution(final Validator validator, PluginUtil.ExecutionType dcType, MapleDataConfig dc) {
        MaplePlugin<MaplePluginConfig> execution = PluginUtil.createExecution(dcType, dc.getName(), dc.getConfig(), tableEnv, gv);
        checkPluginConfig(validator, execution.getConfig());
        return execution;
    }

    private void executeArray(MapleArrayData mapleData) {
        if (mapleData.getPlugins() == null || mapleData.getPlugins().length == 0) {
            throw new ConfigRuntimeException("plugins is empty");
        }

        List<MaplePlugin> executions = new ArrayList<>(mapleData.getPlugins().length);
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            for (MapleDataConfig dc : mapleData.getPlugins()) {
                MaplePlugin<MaplePluginConfig> execution = PluginUtil.createExecution(dc.getType(), dc.getName(), dc.getConfig(), tableEnv, gv);
                checkPluginConfig(validator, execution.getConfig());
                executions.add(execution);
            }
        }
        executePlugins(executions);
    }

    private void executePlugins(List<MaplePlugin> executions) {
        StatementSet statementSet = tableEnv.createStatementSet();
        for (MaplePlugin execution : executions) {
            execution.define();
            if (execution.getConfig() instanceof TableInsert) {
                TableInsert tableInsert = (TableInsert) execution.getConfig();
                statementSet.addInsertSql(tableInsert.getInsertSql());
            }
            if (execution.getConfig().isTerminate()) {
                break;
            }
        }
        TableResult tableResult = statementSet.execute();
    }

    private void checkPluginConfig(Validator validator, MaplePluginConfig config) {
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
            success = false;
        }
        if (config instanceof StructTableConfig) {
            StructTableConfig c = (StructTableConfig) config;
            StringBuilder tableName = new StringBuilder();
            if (StringUtils.isNotBlank(c.getCatalogName())) {
                tableName.append(c.getCatalogName()).append(".");
            }
            if (StringUtils.isNotBlank(c.getDatabaseName())) {
                tableName.append(c.getDatabaseName()).append(".");
            }
            tableName.append(c.getTableName());
            if (registerTableSet.contains(tableName.toString())) {
                logger.error("Result table [{}] cannot be duplicate", tableName);
                success = false;
            } else {
                registerTableSet.add(tableName.toString());
            }
        }
        if (!success) {
            throw new ConfigRuntimeException("Config data valid failed");
        }
    }
}
