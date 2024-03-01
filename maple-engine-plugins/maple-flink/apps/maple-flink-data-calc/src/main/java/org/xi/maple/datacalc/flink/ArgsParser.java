package org.xi.maple.datacalc.flink;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgsParser {
    private static final Logger logger = LoggerFactory.getLogger(ArgsParser.class);

    private static final Options options = getOptions();

    public static CommandLine getCommandLine(String[] args) {

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("h")) {
                printUsage();
                System.exit(0);
            }
            if (cmd.hasOption("f") && cmd.hasOption("d")) {
                logger.error("Config file or config data string cannot be set up at the same time");
                System.exit(1);
            }
            if (!cmd.hasOption("f") && !cmd.hasOption("d")) {
                logger.error("Config file or config data string should be set up");
                System.exit(1);
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return cmd;
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "显示帮助信息");
        options.addOption("f", "file", true, "配置地址");
        options.addOption("d", "data", true, "配置内容");
        options.addOption("m", "mode", true, "执行模式");
        return options;
    }

    private static void printUsage() {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("Usage", options);
    }
}
