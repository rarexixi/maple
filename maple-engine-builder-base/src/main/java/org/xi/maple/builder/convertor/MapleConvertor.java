package org.xi.maple.builder.convertor;

import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.builder.model.EngineExecutionModel;

import java.io.IOException;
import java.util.List;

public interface MapleConvertor {

    List<CommandGeneratorModel> getCommandGenerator(EngineExecutionModel execution);
}
