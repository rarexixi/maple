package org.xi.maple.datacalc.flink;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.table.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.common.util.VariableUtils;
import org.xi.maple.datacalc.flink.api.TableDefine;
import org.xi.maple.datacalc.flink.api.TableInsert;
import org.xi.maple.datacalc.flink.api.MaplePlugin;
import org.xi.maple.datacalc.flink.exception.ConfigRuntimeException;
import org.xi.maple.datacalc.flink.model.*;
import org.xi.maple.datacalc.flink.model.definition.*;
import org.xi.maple.datacalc.flink.util.PluginUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.stream.Collectors;

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
        for (MapleDataConfig dc : mapleData.getSources()) {
            executions.add(getExecution("source", dc));
        }
        for (MapleDataConfig dc : mapleData.getTransformations()) {
            executions.add(getExecution("transform", dc));
        }
        for (MapleDataConfig dc : mapleData.getSinks()) {
            executions.add(getExecution("sink", dc));
        }
        executePlugins(executions);
    }

    private void executeArray(MapleArrayData mapleData) {
        if (mapleData.getPlugins() == null || mapleData.getPlugins().length == 0) {
            throw new ConfigRuntimeException("plugins is empty");
        }
        List<MaplePlugin> executions = Arrays.stream(mapleData.getPlugins()).map(dc -> getExecution(dc.getType(), dc)).collect(Collectors.toList());
        executePlugins(executions);
    }

    private MaplePlugin getExecution(String dcType, MapleDataConfig dc) {
        switch (dcType) {
            case "source":
                return PluginUtil.createSource(dc.getName(), dc.getConfig(), tableEnv, gv);
            case "transform":
                return PluginUtil.createTransform(dc.getName(), dc.getConfig(), tableEnv, gv);
            case "sink":
                return PluginUtil.createSink(dc.getName(), dc.getConfig(), tableEnv, gv);
            default:
                throw new ConfigRuntimeException("[" + dcType + "] is not a valid type");
        }
    }

    private void executePlugins(List<MaplePlugin> executions) {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            for (MaplePlugin execution : executions) {
                if (!checkPluginConfig(validator, execution.getConfig())) {
                    throw new ConfigRuntimeException("Config data valid failed");
                }
            }
        }
        StatementSet statementSet = tableEnv.createStatementSet();
        for (MaplePlugin execution : executions) {
            if (execution instanceof TableDefine) {
                TableDefine tableDefine = (TableDefine) execution;
                tableDefine.define();
            }
            if (execution instanceof TableInsert) {
                TableInsert tableInsert = (TableInsert) execution;
                statementSet.addInsertSql(tableInsert.getInsertSql());
            }
            if (execution.getConfig().isTerminate()) {
                break;
            }
        }
        TableResult tableResult = statementSet.execute();

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
        if (config instanceof TableDefinition) {
            TableDefinition c = (TableDefinition) config;
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

    public TableDescriptor getTableDescriptor(TableDefinition tableDefinition) {
        Schema.Builder schemaBuilder = Schema.newBuilder();
        List<Schema.UnresolvedColumn> unresolvedColumns = schemaBuilder.build().getColumns();
        for (BaseColumn column : tableDefinition.getColumns()) {
            if (column instanceof PhysicalColumn) {
                PhysicalColumn pc = (PhysicalColumn) column;
                // new Schema.UnresolvedPhysicalColumn(pc.getName(), DataTypes.of(pc.getDataType()), pc.getComment());
                schemaBuilder.column(pc.getName(), pc.getDataType()).withComment(pc.getComment());
            } else if (column instanceof MetadataColumn) {
                MetadataColumn mc = (MetadataColumn) column;
                // new Schema.UnresolvedMetadataColumn(mc.getName(), DataTypes.of(mc.getDataType()), mc.getMetadataKey(), mc.isVirtual(), mc.getComment());
                schemaBuilder.columnByMetadata(mc.getName(), mc.getDataType(), mc.getMetadataKey(), mc.isVirtual()).withComment(mc.getComment());
            } else if (column instanceof ComputedColumn) {
                ComputedColumn cc = (ComputedColumn) column;
                // new Schema.UnresolvedComputedColumn(cc.getName(), new SqlCallExpression(cc.getExpression()), cc.getComment());
                schemaBuilder.columnByExpression(cc.getName(), cc.getExpression()).withComment(cc.getComment());
            }
        }
        if (tableDefinition.getPrimaryKey() != null) {
            PrimaryKeyDefinition pk = tableDefinition.getPrimaryKey();
            if (StringUtils.isBlank(pk.getName())) {
                schemaBuilder.primaryKey(pk.getColumns());
                schemaBuilder.primaryKey(pk.getColumns());
            } else {
                schemaBuilder.primaryKeyNamed(pk.getName(), pk.getColumns());
            }
        }
        if (tableDefinition.getWatermark() != null) {
            WatermarkDefinition watermark = tableDefinition.getWatermark();
            schemaBuilder.watermark(watermark.getColumnName(), watermark.getExpression());
        }

        TableDescriptor.Builder tableBuilder = TableDescriptor.forConnector(tableDefinition.getConnector()).schema(schemaBuilder.build());
        Map<String, String> defineOptions = tableDefinition.getDefineOptions();
        if (defineOptions != null) {
            defineOptions.forEach((key, value) -> {
                tableDefinition.getOptions().remove(key);
                tableBuilder.option(key, value);
            });
        }
        if (tableDefinition.getOptions() != null) {
            tableDefinition.getOptions().forEach(tableBuilder::option);
        }
        if (tableDefinition.getPartitionColumns() != null) {
            tableBuilder.partitionedBy(tableDefinition.getPartitionColumns());
        }
        tableBuilder.comment(tableDefinition.getComment());
        return tableBuilder.build();
    }
}
