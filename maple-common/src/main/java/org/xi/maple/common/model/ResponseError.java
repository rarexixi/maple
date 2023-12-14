package org.xi.maple.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xishihao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError implements Serializable {

    private int code;
    private String msg;
}
