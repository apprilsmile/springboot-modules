package com.appril.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author appril
 * @desc
 * @date 20210624
 */
@Data
public class ScoreKafkaDTO implements Serializable {

    /**
     * 征信ID
     */
    private Long rcCstBscInfoId;

    /**
     * 客户姓名
     */
    private String cstName;

    /**
     * 客户电话
     */
    private String mp;

    /**
     * 供应商名称
     */
    private String splNm;

    /**
     * 所属供应商
     */
    private Long splId;

    /**
     * 客户经理ID
     */
    private Long cstMgrId;

    /**
     * 客户经理名称
     */
    private String cstMgrNm;

    /**
     * 客户经理事业部代码(如果有)
     */
    private String cstMgrBuOrgCd;

    /**
     * 客户经理机构代码(如果有)
     */
    private String cstMgrOrgCd;

    /**
     * 查询人ID
     */
    private Long crtUsrId;

    /**
     * 查询人姓名
     */
    private String crtUsrName;

    /**
     * 征信开始查询时间
     */
    private Date crtTm;

    /**
     * 狮桥分等级(非C级别)
     */
    private String rskLvlCd;

    /**
     * 用户等级(123)
     */
    private String rskLvlCd2;

}
