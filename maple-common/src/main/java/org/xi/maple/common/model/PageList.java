package org.xi.maple.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author xishihao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageList<T> implements Serializable {

    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Collection<T> list;
}
