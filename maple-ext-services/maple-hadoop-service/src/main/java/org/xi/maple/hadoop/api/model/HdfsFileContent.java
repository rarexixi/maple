package org.xi.maple.hadoop.api.model;

import lombok.Data;

@Data
public class HdfsFileContent extends HdfsPathModel {
    /**
     * 文件内容
     */
    private String content;
}
