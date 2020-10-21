package com.appril.excel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author appril
 * @since 2020-09-25
 */
@Data
public class SysUserEasyDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID",index= 0)
    private Long id;

    /**
     * 工号
     */
    @ExcelProperty(value = "工号",index = 1)
    private String usrNm;

    /**
     * 真实姓名
     */
    @ExcelProperty(value = "真实姓名",index = 2)
    private String rlNm;

    /**
     * 密码
     */
    @ExcelProperty(value = "密码",index = 3)
    private String pwd;

    /**
     * 电话
     */
    @ExcelProperty(value = "电话",index = 4)
    private String mp;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱",index = 5)
    private String email;




}
