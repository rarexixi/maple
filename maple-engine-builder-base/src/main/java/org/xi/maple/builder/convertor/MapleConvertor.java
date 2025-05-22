package org.xi.maple.builder.convertor;

import org.xi.maple.builder.exception.CommandsEmptyException;
import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.builder.model.ExecInfoPattern;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public interface MapleConvertor {

    List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution);

    default List<CommandGeneratorModel> getOperateCommandGenerator(EngineExecutionModel execution) {
        throw new CommandsEmptyException("Commands are empty!");
    }

    default Pattern getClusterAppIdPatterns() {
        return null;
    }

    default Pattern getClusterAppWebUrl() {
        return null;
    }

    /**
     * 获取执行信息的正则用于查找对应执行信息，如 ApplicationID，任务的 webURL 等
     *
     * @return 列表
     */
    default List<ExecInfoPattern> getExecInfoPatterns() {
        return Collections.emptyList();
    }
}
