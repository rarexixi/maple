package org.xi.maple.hadoop.api.model;

import lombok.Data;

/**
 * HDFS文件响应实体
 */
@Data
public class HdfsPathModel {

    /**
     * 是否是文件夹
     */
    private boolean directory;
    /**
     * 文件夹路径
     */
    private String parent;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件名
     */
    private String name;
    /**
     * 最后修改时间
     */
    private long lastModified;
    /**
     * 文件大小
     */
    private long length;
}
