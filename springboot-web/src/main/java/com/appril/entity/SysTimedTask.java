package com.appril.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author appril
 * @since 2020-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_TIMED_TASK")
public class SysTimedTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 任务状态：1：启用，2：终止
     */
    private Integer taskStatus;

    private String jobName;

    private String jobGroupName;

    private String triggerName;

    private String triggerGroupName;

    /**
     * cron表达式
     */
    private String dateCron;

    /**
     * 是否删除,0未删除，1已删除
     */
    private Integer isDel;

    /**
     * 创建时间
     */
    private Date crtTm;

    /**
     * 创建人ID
     */
    private Integer crtUsrId;

    /**
     * 修改时间
     */
    private Date mdfTm;

    /**
     * 修改人ID
     */
    private Integer mdfUsrId;


}
