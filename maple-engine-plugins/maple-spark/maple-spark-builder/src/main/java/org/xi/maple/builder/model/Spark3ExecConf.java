package org.xi.maple.builder.model;

import lombok.Data;

import java.io.Serializable;

public interface Spark3ExecConf {

    @Data
    abstract class ExecConf implements Serializable {
    }

    @Data
    class DataCalc extends ExecConf {
        String execFile;
    }

    @Data
    class Sql extends ExecConf {
        String execFile;
    }

    @Data
    class Jar extends ExecConf {
        String execFile;
        String mainClass;
        String args;
    }

    @Data
    class Py extends ExecConf {
        String execFile;
        String filePath;
        String args;
    }
}