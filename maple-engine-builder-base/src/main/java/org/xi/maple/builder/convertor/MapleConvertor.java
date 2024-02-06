package org.xi.maple.builder.convertor;

import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.builder.model.EngineExecutionModel;

import java.util.List;

public interface MapleConvertor {

    List<CommandGeneratorModel> getSubmitCommandGenerator(EngineExecutionModel execution);

    default List<CommandGeneratorModel> getUpdateCommandGenerator(EngineExecutionModel execution) {
        return null;
    }

    default List<CommandGeneratorModel> getStopCommandGenerator(EngineExecutionModel execution) {
        return null;
    }
}
