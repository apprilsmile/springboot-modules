package com.appril.model.primary.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * <p>
 * 外部服务
 * </p>
 *
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("outside_service")
@NoArgsConstructor
@AllArgsConstructor
public class OutsideService extends BaseEntity {

    private static final long serialVersionUID = -5081125394981558019L;
    /**
     * 服务名称
     */
    @TableField("name")
    private String            name;

    /**
     * 服务编码
     */
    @TableField("code")
    private String            code;

    /**
     * 合作方Id
     */
    @TableField(value = "partner_id")
    private Long              partnerId;

    /**
     * 合作方名称
     */
    @TableField("partner_name")
    private String            partnerName;

    /**
     * 部门ID
     */
    @TableField(value = "dept_id")
    private Integer           deptId;

    /**
     * 服务状态:0待发布 1已发布 2已停用
     */
    @TableField("state")
    private Integer           state;

    /**
     * 单次调用超时时间:ms,0表示无超时设置
     */
    @TableField("single_timeout")
    private Integer           singleTimeout;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Integer           retryCount;

    /**
     * 服务总超时时间:ms,0表示无超时设置
     */
    @TableField("total_timeout")
    private Integer           totalTimeout;

    /**
     * 数据缓存期:0表示不缓存
     */
    @TableField("data_cache")
    private Integer           dataCache;

    /**
     * 缓存类型:1天,2小时,3秒
     */
    @TableField("data_cache_type")
    private Integer           dataCacheType;

    /**
     * 处理中重试次数
     */
    @TableField("dealing_retry_times")
    private Integer           dealingRetryTimes;

    /**
     * 处理中重试间隔毫秒数
     */
    @TableField("dealing_retry_interval")
    private Long              dealingRetryInterval;

    /**
     * 计费单价（元）
     */
    @TableField("unit_price")
    private Double            unitPrice;

    /**
     * 计费方式：1 查询计费 2 查得计费 3 计费规则
     */
    @TableField("charge_mode")
    private Integer           chargeMode;

    /**
     * 删除标识 0:已删除 1:可用
     */
    @TableField("delete_flag")
    private Integer           deleteFlag;

    /**
     * 描述
     */
    @TableField("description")
    private String            description;

    /**
     * 引入状态:0未引入 1已引入
     */
    @TableField("ref_type")
    private Integer           refType;

    /**
     * 引入状态:0未引入 1已引入
     */
    @TableField("ref_time")
    private Date              refTime;

    /**
     * 创建用户
     */
    @TableField("created_user")
    private String            createdUser;

    /**
     * 更新用户
     */
    @TableField("updated_user")
    private String            updatedUser;

}
