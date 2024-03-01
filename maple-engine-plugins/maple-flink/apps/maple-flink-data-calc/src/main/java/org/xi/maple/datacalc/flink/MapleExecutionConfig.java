package org.xi.maple.datacalc.flink;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.table.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.common.util.VariableUtils;
import org.xi.maple.datacalc.flink.exception.ConfigRuntimeException;
import org.xi.maple.datacalc.flink.model.*;
import org.xi.maple.datacalc.flink.model.definition.*;
import org.xi.maple.datacalc.flink.util.ConfigUtil;
import org.xi.maple.datacalc.flink.util.TableUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.stream.Collectors;

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
        List<MaplePluginConfig> executions = new ArrayList<>();
        for (MapleDataConfig dc : mapleData.getSources()) {
            executions.add(getExecutionConfig("source", dc));
        }
        for (MapleDataConfig dc : mapleData.getTransformations()) {
            executions.add(getExecutionConfig("transform", dc));
        }
        for (MapleDataConfig dc : mapleData.getSinks()) {
            executions.add(getExecutionConfig("sink", dc));
        }
        executePlugins(executions);
    }

    private void executeArray(MapleArrayData mapleData) {
        if (mapleData.getPlugins() == null || mapleData.getPlugins().length == 0) {
            throw new ConfigRuntimeException("plugins is empty");
        }
        List<MaplePluginConfig> executions = Arrays.stream(mapleData.getPlugins()).map(this::getExecutionConfig).collect(Collectors.toList());
        executePlugins(executions);
    }

    private MaplePluginConfig getExecutionConfig(MapleDataConfig dc) {
        return ConfigUtil.createConfig(dc.getType(), dc.getName(), dc.getConfig());
    }

    private MaplePluginConfig getExecutionConfig(String dcType, MapleDataConfig dc) {
        return ConfigUtil.createConfig(dcType, dc.getName(), dc.getConfig());
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
                    // statementSet.addInsertSql(insertTableConfig.getInsertSql());
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
        if (config instanceof CreateTableConfig) {
            CreateTableConfig c = (CreateTableConfig) config;
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
        return success;
    }
}
