package org.xi.maple.spark3.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author xishihao
 */
@XmlRootElement
public class SqlRequest implements Serializable {

    Integer jobId;

    String sql;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return "SqlRequest{" +
                "jobId=" + jobId +
                ", sql='" + sql + '\'' +
                '}';
    }
}