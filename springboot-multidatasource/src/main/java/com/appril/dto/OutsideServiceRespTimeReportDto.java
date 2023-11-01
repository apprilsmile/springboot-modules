package com.appril.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 外部服务响应时间报表 出参DTO
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutsideServiceRespTimeReportDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String callTime;

    private Integer            maxTime;

    private Integer            avgTime;

    private Integer            minTime;

    /**
     * OutsideServiceCallInvocationDto
     *
     * @param callTime
     * @return
     */
    public OutsideServiceRespTimeReportDto(String callTime) {
        this.callTime = callTime;
        this.maxTime = 0;
        this.avgTime = 0;
        this.minTime = 0;
    }

}
