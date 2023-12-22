package org.xi.maple.hadoop.api.service;


import org.xi.maple.hadoop.api.model.HdfsFileContent;
import org.xi.maple.hadoop.api.model.HdfsPathModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface HdfsService {

    /**
     * 获取HDFS文件夹路径下所有文件
     *
     * @param path HDFS文件夹路径
     * @return 文件列表
     * @throws InterruptedException
     * @throws IOException
     */
    List<HdfsPathModel> list(String path) throws InterruptedException, IOException;

    /**
     * 上传HDFS文件
     *
     * @param path        文件路径
     * @param inputStream 文件流
     * @throws InterruptedException
     * @throws IOException
     */
    void upload(String path, InputStream inputStream) throws InterruptedException, IOException;

    /**
     * 上传HDFS文件
     *
     * @param path    文件路径
     * @param content 文件字节内容
     * @throws InterruptedException
     * @throws IOException
     */
    void upload(String path, byte[] content) throws InterruptedException, IOException;

    /**
     * 上传HDFS文件
     *
     * @param path    文件路径
     * @param content 文件内容
     * @throws InterruptedException
     * @throws IOException
     */
    void upload(String path, String content) throws InterruptedException, IOException;

    /**
     * 获取HDFS文件内容
     *
     * @param path 文件路径
     * @return 文件内容
     * @throws InterruptedException
     * @throws IOException
     */
    HdfsFileContent getContent(String path) throws IOException, InterruptedException;
}
