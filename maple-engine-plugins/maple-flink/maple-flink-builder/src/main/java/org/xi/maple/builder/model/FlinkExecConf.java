package org.xi.maple.builder.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class FlinkExecConf {

    @Data
    public static abstract class ExecConf implements Serializable {
    }

    @Data
    public static class DataCalc extends ExecConf {
        String execFile;
    }

    @Data
    public static class Sql extends ExecConf {
        String execFile;
    }

    @Data
    public static class Jar extends ExecConf {
        String execFile;
        String mainClass;
        String args;
    }

    @Data
    public static class Py extends ExecConf {
        String execFile;
        String filePath;
        String args;
    }
}
