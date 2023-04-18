package org.xi.maple.datasource.persistence.condition;

import org.xi.maple.common.model.UpdateCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据源配置项更新条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class DatasourceConfigKeyUpdateCondition extends UpdateCondition {

    /**
     * Id
     */
    private Integer id;

    /**
     * Id列表
     */
    private Collection<Integer> ids;

    private String datasourceType;
}
