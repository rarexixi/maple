package org.xi.maple.builder.convertor;

import org.xi.maple.builder.annotation.ClusterCategory;
import org.xi.maple.builder.annotation.EngineCategory;
import org.xi.maple.builder.annotation.EngineVersion;
import org.xi.maple.builder.model.*;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.constant.EngineCategoryConstants;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.common.util.MapUtils;

import java.util.ArrayList;
import java.util.List;

@ClusterCategory(ClusterCategoryConstants.YARN)
@EngineCategory(EngineCategoryConstants.SPARK)
@EngineVersion(value = {"3.3.2"})
public class Spark3YarnConvertor extends Spark3Convertor {

    @Override
    public List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution) {
        ExecFtlModel<Spark3RunConf.Yarn, ?> execConf = convert(execution);

        List<CommandGeneratorModel> generatorModels = new ArrayList<>();
        generatorModels.add(new CommandGeneratorModel("spark3-yarn-submit.sh", "spark3-yarn-submit.sh.ftl", execConf, true));
        return generatorModels;
    }

    private ExecFtlModel<Spark3RunConf.Yarn, Spark3ExecConf.ExecConf> convert(EngineExecutionModel execution) {
        ExecFtlModel<Spark3RunConf.Yarn, Spark3ExecConf.ExecConf> execModel = new ExecFtlModel<>(execution);
        execModel.setRunConf(getRunConf(execution));
        execModel.setExecConf(getExecConf(execution));
        return execModel;
    }

    Spark3RunConf.Yarn getRunConf(EngineExecutionModel execution) {
        Spark3RunConf.Yarn runConf = JsonUtils.parseObject(execution.getRunConf(), Spark3RunConf.Yarn.class, null);
        if (runConf == null) {
            throw new MapleException("RunConf has errors.");
        }
        if (execution.getEngine().getConfs() != null) {
            runConf.setConf(MapUtils.mergeMap(execution.getEngine().getConfs(), runConf.getConf()));
        } else {
            runConf.setConf(runConf.getConf());
        }
        return runConf;
    }
}
