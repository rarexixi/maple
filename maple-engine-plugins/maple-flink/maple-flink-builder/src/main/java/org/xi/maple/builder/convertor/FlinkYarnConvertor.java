package org.xi.maple.builder.convertor;

import org.xi.maple.builder.annotation.ClusterCategory;
import org.xi.maple.builder.annotation.EngineCategory;
import org.xi.maple.builder.annotation.EngineVersion;
import org.xi.maple.builder.model.*;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.constant.EngineCategoryConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@ClusterCategory(ClusterCategoryConstants.YARN)
@EngineCategory(EngineCategoryConstants.FLINK)
@EngineVersion(value = {"1.16.1", "1.17.2"})
public class FlinkYarnConvertor extends FlinkConvertor {

    private static final Pattern CLUSTER_APP_ID_PATTERN = Pattern.compile("application_\\d+_\\d+");
    private static final Pattern CLUSTER_APP_WEB_URL_PATTERN = Pattern.compile("(?<=Found Web Interface ).*(?= of)");

    @Override
    public List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<FlinkRunConf.Yarn, FlinkExecConf.ExecConf> execConf = convert(execution);
        String resultFileName = "flink-yarn-submit.sh";
        return Arrays.asList(new CommandGeneratorModel(resultFileName, resultFileName + ".ftl", execConf, true));
    }

    @Override
    public List<CommandGeneratorModel> getOperateCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<FlinkRunConf.Yarn, FlinkExecConf.ExecConf> execConf = convert(execution);
        String resultFileName = String.format("flink-yarn-%s.sh", execution.getAction());
        return Arrays.asList(new CommandGeneratorModel(resultFileName, resultFileName + ".ftl", execConf, true));
    }

    @Override
    public Pattern getClusterAppIdPatterns() {
        return CLUSTER_APP_ID_PATTERN;
    }

    @Override
    public Pattern getClusterAppWebUrl() {
        return CLUSTER_APP_WEB_URL_PATTERN;
    }

    @Override
    public List<ExecInfoPattern> getExecInfoPatterns() {
        List<ExecInfoPattern> patterns = new ArrayList<>(2);
        patterns.add(new ExecInfoPattern("APPLICATION_ID", CLUSTER_APP_ID_PATTERN));
        patterns.add(new ExecInfoPattern("WEB_INTERFACE", CLUSTER_APP_WEB_URL_PATTERN));
        return patterns;
    }

    private ExecFtlModel<FlinkRunConf.Yarn, FlinkExecConf.ExecConf> convert(EngineExecutionModel execution) {
        ExecFtlModel<FlinkRunConf.Yarn, FlinkExecConf.ExecConf> execModel = new ExecFtlModel<>(execution);
        execModel.setRunConf(getRunConf(execution, FlinkRunConf.Yarn.class));
        execModel.setExecConf(getExecConf(execution));
        return execModel;
    }
}
