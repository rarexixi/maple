package org.xi.maple.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author xishihao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseError implements Serializable {

    private int code;
    private String msg;
}
