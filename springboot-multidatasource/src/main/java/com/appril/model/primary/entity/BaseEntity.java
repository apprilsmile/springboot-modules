package com.appril.model.primary.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 基础类
 * </p>
 *
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 2705687128014990509L;
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long              id;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date              createdTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date              updatedTime;

}
