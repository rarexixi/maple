package org.xi.maple.datacalc.flink;

import org.apache.commons.cli.CommandLine;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.PipelineOptionsInternal;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.config.TableConfigOptions;
import org.xi.maple.datacalc.flink.model.MapleArrayData;
import org.xi.maple.datacalc.flink.model.MapleData;
import org.xi.maple.datacalc.flink.model.MapleGroupData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MapleApp {

    public static void main(String[] args) throws Exception {
        CommandLine cmd = ArgsParser.getCommandLine(args);
        cmd.getOptionValue("file");
        String execType = cmd.getOptionValue("exec-type", "array");

        String jobId = "maple-flink-data-calc";
        Configuration configuration = new Configuration();
        configuration.setString(PipelineOptionsInternal.PIPELINE_FIXED_JOB_ID, jobId);

        try (StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration)) {
            TableEnvironment tableEnv = "stream".equals(cmd.getOptionValue("mode"))
                    ? StreamTableEnvironment.create(env, EnvironmentSettings.inStreamingMode())
                    : StreamTableEnvironment.create(env, EnvironmentSettings.inBatchMode());

            Configuration tableConfiguration = tableEnv.getConfig().getConfiguration();
            tableConfiguration.setBoolean(TableConfigOptions.TABLE_DYNAMIC_TABLE_OPTIONS_ENABLED.key(), true);

            String config = cmd.hasOption("data") ? cmd.getOptionValue("data") : getContent(cmd.getOptionValue("file"));
            MapleData data = "group".equals(execType) ? MapleGroupData.getData(config) : MapleArrayData.getData(config);
            MapleExecution<MapleData> execution = new MapleExecution<>(tableEnv, data);
            execution.execute();
        }

    }

    private static String getContent(String file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}
