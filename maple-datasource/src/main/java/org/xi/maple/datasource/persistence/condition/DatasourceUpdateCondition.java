package org.xi.maple.datasource.persistence.condition;

import org.xi.maple.common.model.ManipulateCondition;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据源配置更新条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class DatasourceUpdateCondition extends ManipulateCondition {

    /**
     * Id
     */
    private Integer id;

    /**
     * Id列表
     */
    private Collection<Integer> ids;
}
