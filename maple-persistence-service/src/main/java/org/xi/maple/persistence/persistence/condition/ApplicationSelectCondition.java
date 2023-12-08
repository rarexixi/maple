package org.xi.maple.persistence.persistence.condition;

import org.xi.maple.common.model.SelectCondition;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 访问程序查询条件
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ApplicationSelectCondition extends SelectCondition {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用名称列表
     */
    private Collection<String> appNameIn;

    /**
     * 排除的应用名称列表
     */
    private Collection<String> appNameNotIn;

    /**
     * 应用名称不为空
     */
    private Boolean appNameIsNotEmpty;

    /**
     * 应用名称为空
     */
    private Boolean appNameIsEmpty;

    /**
     * 应用名称开始
     */
    private String appNameStartWith;

    /**
     * 应用名称结束
     */
    private String appNameEndWith;

    /**
     * 应用名称包含
     */
    private String appNameContains;

    /**
     * 应用访问密钥
     */
    private String accessKey;

    /**
     * 应用访问密钥列表
     */
    private Collection<String> accessKeyIn;

    /**
     * 排除的应用访问密钥列表
     */
    private Collection<String> accessKeyNotIn;

    /**
     * 应用访问密钥不为空
     */
    private Boolean accessKeyIsNotEmpty;

    /**
     * 应用访问密钥为空
     */
    private Boolean accessKeyIsEmpty;

    /**
     * 应用访问密钥开始
     */
    private String accessKeyStartWith;

    /**
     * 应用访问密钥结束
     */
    private String accessKeyEndWith;

    /**
     * 应用访问密钥包含
     */
    private String accessKeyContains;

    /**
     * 允许请求的IP
     */
    private String legalHosts;

    /**
     * 允许请求的IP列表
     */
    private Collection<String> legalHostsIn;

    /**
     * 排除的允许请求的IP列表
     */
    private Collection<String> legalHostsNotIn;

    /**
     * 允许请求的IP不为空
     */
    private Boolean legalHostsIsNotEmpty;

    /**
     * 允许请求的IP为空
     */
    private Boolean legalHostsIsEmpty;

    /**
     * 允许请求的IP开始
     */
    private String legalHostsStartWith;

    /**
     * 允许请求的IP结束
     */
    private String legalHostsEndWith;

    /**
     * 允许请求的IP包含
     */
    private String legalHostsContains;

    /**
     * 回调接口
     */
    private String callbackUrls;

    /**
     * 回调接口列表
     */
    private Collection<String> callbackUrlsIn;

    /**
     * 排除的回调接口列表
     */
    private Collection<String> callbackUrlsNotIn;

    /**
     * 回调接口不为空
     */
    private Boolean callbackUrlsIsNotEmpty;

    /**
     * 回调接口为空
     */
    private Boolean callbackUrlsIsEmpty;

    /**
     * 回调接口开始
     */
    private String callbackUrlsStartWith;

    /**
     * 回调接口结束
     */
    private String callbackUrlsEndWith;

    /**
     * 回调接口包含
     */
    private String callbackUrlsContains;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建人列表
     */
    private Collection<Integer> createUserIn;

    /**
     * 排除的创建人列表
     */
    private Collection<Integer> createUserNotIn;

    /**
     * 最小创建人
     */
    private Integer createUserMin;

    /**
     * 最大创建人
     */
    private Integer createUserMax;

    /**
     * 修改人
     */
    private Integer updateUser;

    /**
     * 修改人列表
     */
    private Collection<Integer> updateUserIn;

    /**
     * 排除的修改人列表
     */
    private Collection<Integer> updateUserNotIn;

    /**
     * 最小修改人
     */
    private Integer updateUserMin;

    /**
     * 最大修改人
     */
    private Integer updateUserMax;

    /**
     * 最小创建时间
     */
    private LocalDateTime createTimeMin;

    /**
     * 最大创建时间
     */
    private LocalDateTime createTimeMax;

    /**
     * 最小更新时间
     */
    private LocalDateTime updateTimeMin;

    /**
     * 最大更新时间
     */
    private LocalDateTime updateTimeMax;
}
