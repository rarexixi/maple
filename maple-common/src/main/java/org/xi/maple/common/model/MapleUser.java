package org.xi.maple.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xishihao
 */
@Data
public class MapleUser implements Serializable {

    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 姓名
     */
    private String name;
}
