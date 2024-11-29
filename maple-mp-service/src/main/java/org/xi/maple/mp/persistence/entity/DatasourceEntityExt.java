package org.xi.maple.mp.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据源扩展实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class DatasourceEntityExt extends DatasourceEntity {

    /**
     * 数据源类型
     */
    private String datasourceTypeText;

    private void setDatasourceTypeText (String datasourceTypeText) {
        this.datasourceTypeText = datasourceTypeText;
    }

    private String getDatasourceTypeText() {
        return datasourceTypeText;
    }
}
