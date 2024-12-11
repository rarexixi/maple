package org.xi.maple.hadoop.api.service.impl;

import org.xi.maple.hadoop.api.configuration.MapleHdfsProperties;
import org.xi.maple.hadoop.api.model.HdfsPathModel;
import org.xi.maple.hadoop.api.model.HdfsFileContent;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xi.maple.hadoop.api.service.HdfsService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

@Service("hdfsService")
public class HdfsServiceImpl implements HdfsService {

    private final Logger logger = LoggerFactory.getLogger(HdfsServiceImpl.class);

    final MapleHdfsProperties hdfsProperties;

    @Autowired
    public HdfsServiceImpl(MapleHdfsProperties hdfsProperties) {
        this.hdfsProperties = hdfsProperties;
    }

    /**
     * 获取HDFS文件夹路径下所有文件
     *
     * @param path HDFS文件夹路径
     * @return 文件列表
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public List<HdfsPathModel> list(String path) throws InterruptedException, IOException {
        List<HdfsPathModel> list = new LinkedList<>();
        try (FileSystem fileSystem = getFileSystem()) {
            Path folder = new Path(path);
            if (fileSystem.exists(folder)) {
                FileStatus[] statuses = fileSystem.listStatus(folder);
                for (FileStatus status : statuses) {
                    HdfsPathModel model = new HdfsPathModel();
                    setHdfsPathModel(model, status);
                    list.add(model);
                }
            }
        }
        return list;
    }

    /**
     * 上传HDFS文件
     *
     * @param path        文件路径
     * @param inputStream 文件流
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public void upload(String path, InputStream inputStream) throws InterruptedException, IOException {
        try (FileSystem fileSystem = getFileSystem();
             FSDataOutputStream outputStream = fileSystem.create(new Path(path))) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

    /**
     * 上传HDFS文件
     *
     * @param path    文件路径
     * @param content 文件字节内容
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public void upload(String path, byte[] content) throws InterruptedException, IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(content)) {
            upload(path, inputStream);
        }
    }

    /**
     * 上传HDFS文件
     *
     * @param path    文件路径
     * @param content 文件内容
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public void upload(String path, String content) throws InterruptedException, IOException {
        upload(path, content.getBytes());
    }

    /**
     * 获取HDFS文件内容
     *
     * @param path 文件路径
     * @return 文件内容
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public HdfsFileContent getContent(String path) throws IOException, InterruptedException {
        HdfsFileContent fileContent = new HdfsFileContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (FileSystem fileSystem = getFileSystem();
             FSDataInputStream inputStream = fileSystem.open(new Path(path))) {
            Path file = new Path(path);
            FileStatus status = fileSystem.getFileStatus(file);
            setHdfsPathModel(fileContent, status);
            IOUtils.copy(inputStream, outputStream);
        }
        String content = outputStream.toString();
        fileContent.setContent(content);
        return fileContent;
    }

    private void setHdfsPathModel(HdfsPathModel model, FileStatus status) {
        Path file = status.getPath();
        model.setName(file.getName());
        URI uri = status.getPath().toUri();
        model.setPath(uri.getPath());
        model.setDirectory(status.isDirectory());
        model.setLastModified(status.getModificationTime());
        model.setParent(uri.getPath().substring(0, uri.getPath().length() - file.getName().length()));
        if (status.isFile()) {
            model.setLength(status.getLen());
        }
    }

    private FileSystem getFileSystem() throws IOException, InterruptedException {
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        hdfsProperties.getConf().forEach(configuration::set);
        return FileSystem.get(FileSystem.getDefaultUri(configuration), configuration, hdfsProperties.getUsername());
    }
}