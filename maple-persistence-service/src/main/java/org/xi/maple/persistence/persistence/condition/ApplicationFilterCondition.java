package org.xi.maple.persistence.persistence.condition;

import org.xi.maple.common.model.db.condition.FilterCondition;

import java.time.LocalDateTime;
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
public class ApplicationFilterCondition implements FilterCondition {

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
     * 是否禁用
     */
    private Integer disabled;

    /**
     * 创建人
     */
    private Integer createdBy;

    /**
     * 创建人列表
     */
    private Collection<Integer> createdByIn;

    /**
     * 排除的创建人列表
     */
    private Collection<Integer> createdByNotIn;

    /**
     * 最小创建人
     */
    private Integer createdByMin;

    /**
     * 最大创建人
     */
    private Integer createdByMax;

    /**
     * 修改人
     */
    private Integer updatedBy;

    /**
     * 修改人列表
     */
    private Collection<Integer> updatedByIn;

    /**
     * 排除的修改人列表
     */
    private Collection<Integer> updatedByNotIn;

    /**
     * 最小修改人
     */
    private Integer updatedByMin;

    /**
     * 最大修改人
     */
    private Integer updatedByMax;

    /**
     * 最小创建时间
     */
    private LocalDateTime createdAtMin;

    /**
     * 最大创建时间
     */
    private LocalDateTime createdAtMax;

    /**
     * 最小更新时间
     */
    private LocalDateTime updatedAtMin;

    /**
     * 最大更新时间
     */
    private LocalDateTime updatedAtMax;
}
