package com.appril.model.internal.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@TableName("trace_report_external")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuperBuilder
@Accessors(chain = true)
public class TraceReportExternal implements Serializable {

    /**
     * 外数调用来源：
     * 数据来源类型：生产策略 PROD、灰度策略PROD_GRAY、陪跑策略 PROD_RUNWITH，组件测试 TEST_COMPONENT、策略测试TEST_STRATEGY、服务测试 SERVICE_TEST、变量加工生产 VAR_PROD、变量加工测试VAR_TEST
     *
     */
    @TableField("source_type")
    private String     sourceType;

    /**
     * decision_serial_no VARCHAR comment '决策流水号-外部调用流水号',
     */
    @TableField("decision_serial_no")
    private String     decisionSerialNo;

    /**
     * decision_serial_no VARCHAR comment '变量加工流水号',
     */
    @TableField("var_decision_serial_no")
    private String     varDecisionSerialNo;

    /**
     * outside_service_serial_no String comment '外数内部流水号',
     */
    @TableField("outside_service_serial_no")
    private String     outsideServiceSerialNo;

    /**
     * 空间类型：1-领域；2-变量空间
     */
    @TableField("space_type")
    private Integer    spaceType;

    /**
     * 决策领域是 领域编号
     * 变量空间是 空间id
     */
    @TableField("domain_id")
    private Long       domainId;

    /**
     * 决策服务主键
     */
    @TableField("decision_id")
    private Long       decisionId;

    /**
     * 领域编码
     */
    @TableField(exist = false)
    private String     domainCode;

    /**
     * 领域名称
     */
    @TableField(exist = false)
    private String     domainName;

    /**
     * service_id UInt64 comment ''决策服务'',
     */
    @TableField("service_id")
    private Long       serviceId;

    /**
     * bucket_id UInt64 comment ''服务细分'',
     */
    @TableField("bucket_id")
    private Long       bucketId;

    /**
     * strategy_id UInt64 comment ''策略版本'',
     */
    @TableField("strategy_id")
    private Long       strategyId;

    /**
     * 外部服务ID
     */
    @TableField(exist = false)
    private Long       outsideServiceId;

    /**
     * service_code VARCHAR comment '外部服务编码',
     */
    @TableField("service_code")
    private String     serviceCode;

    /**
     * reuqest_start_date DateTime64(3) comment '请求开始处理时间',
     */
    @TableField("reuqest_start_date")
    private Date       reuqestStartDate;

    /**
     * call_success BOOLEAN comment '接口成功',
     */
    @TableField("call_success")
    private Integer    callSuccess;

    /**
     * business_success BOOLEAN comment '查得',
     */
    @TableField("business_success")
    private Integer    businessSuccess;

    /**
     * charge_required BOOLEAN comment '是否计费',
     */
    @TableField("charge_required")
    private Integer    chargeRequired;

    /**
     * hit_cache BOOLEAN comment '命中缓存',
     */
    @TableField("hit_cache")
    private Integer    hitCache;

    /**
     * 是否来自mock的结果,查询时默认排除掉
     */
    @TableField("mock_data")
    private Integer    mockData;

    /**
     * try_times UInt8 comment '重试次数',
     */
    @TableField("try_times")
    private Integer    tryTimes;

    /**
     * response_code UInt16 comment '响应码',
     */
    @TableField("response_code")
    private Integer    responseCode;

    /**
     * exception_type VARCHAR comment '异常类型',
     */
    @TableField("exception_type")
    private String     exceptionType;

    /**
     * cost_millisecond UInt16 comment '耗时',
     */
    @TableField("cost_millisecond")
    private Integer    costMillisecond;

    /**
     * net_state 网络请求状态：0：未请求，1：成功，2：失败
     */
    @TableField("net_state")
    private Integer    netState;

    /**
     * business_serial_no VARCHAR comment '业务流水号：指标加工取指标实时服务绑定的或者批量回溯任务绑定的主体唯一标识',
     */
    @TableField("business_serial_no")
    private String     businessSerialNo;

    /**
     * 调用场景
     */
    @TableField("call_scene")
    private String     callScene;

    /**
     * 调用方
     * 指标清单测试调用，具体指标清单名称；指标实时服务或批量回溯任务调用，服务名称或任务名称
     */
    @TableField("call_name")
    private String     callName;

    /**
     * 批次号
     */
    @TableField("batch_no")
    private String     batchNo;

    /**
     * 变量传入字段json对象
     */
    @TableField(exist = false)
    private JSONObject paramJson;

    /**
     * 变量传入json字符串hash值，用于命中索引
     */
    @TableField(exist = false)
    private String     paramHash;

    /**
     * 响应描述
     */
    @TableField(exist = false)
    private String     respDesc;

    /**
     * 请求报文
     */
    @TableField(exist = false)
    private String     reqMessage;

    /**
     * 响应报文
     */
    @TableField(exist = false)
    private String     respMessage;

    // 建表语句
    /*
    CREATE TABLE trace_report_external
    (
        decision_serial_no VARCHAR comment '流水号',
        outside_service_serial_no String comment '外数内部流水号',
        space_id Int16 default 1 comment '空间类型，1：领域；2：变量',
        domain_id UInt64 comment '决策领域',
        decision_id UInt64 comment '决策服务主键',
        service_id UInt64 comment '决策服务',
        bucket_id UInt64 comment '服务细分',
        strategy_id UInt64 comment '策略版本',
        service_code VARCHAR comment '外部服务编码',
        reuqest_start_date DateTime64(3) comment '请求开始处理时间',
        call_success UInt8 comment '接口成功',
        business_success UInt8 comment '查得',
        hit_cache UInt8 comment '命中缓存',
        mock_data UInt8 default 0 comment 'mock数据',
        try_times UInt8 comment '重试次数',
        response_code UInt16 comment '响应码',
        exception_type VARCHAR comment '异常类型',
        net_state UInt8 comment '网络请求状态：0：未请求，1：成功，2：失败',
        cost_millisecond UInt16 comment '耗时',
        source_type String comment '外数调用来源',
        create_time DateTime64(3) DEFAULT now() comment '落库时间'
    )
    ENGINE=MergeTree
    PARTITION BY toYYYYMM(reuqest_start_date)
    ORDER BY (decision_serial_no,outside_service_serial_no,reuqest_start_date)
    COMMENT '外数trace报表'
    * */

}
