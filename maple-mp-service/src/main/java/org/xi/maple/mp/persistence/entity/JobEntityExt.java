package org.xi.maple.mp.persistence.entity;

import java.math.BigDecimal;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 执行作业扩展实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class JobEntityExt extends JobEntity {

    /**
     * 引擎ID
     */
    private String engineText;

    private void setEngineText (String engineText) {
        this.engineText = engineText;
    }

    private String getEngineText() {
        return engineText;
    }
}
