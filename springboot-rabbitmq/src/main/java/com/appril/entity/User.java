package com.appril.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author appril
 * @since 2020-09-25
 */
@Data
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 工号
     */
    private String usrNm;

    /**
     * 真实姓名
     */
    private String rlNm;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 电话
     */
    private String mp;

    /**
     * 邮箱
     */
    private String eMail;

    /**
     * 是否管理员创建0否1是
     */
    private Integer isSysDef;

    /**
     * 推荐人ID
     */
    private Integer refUsrId;

    /**
     * 推荐人电话
     */
    private String refMp;

    /**
     * 最近一次登录时间
     */
    private Date lastLoginTm;

    /**
     * 是否有效，0 未禁用，1禁用
     */
    private Integer isValid;

    /**
     * 是否删除,0在职，1离职，2停薪留职，3锁定
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
