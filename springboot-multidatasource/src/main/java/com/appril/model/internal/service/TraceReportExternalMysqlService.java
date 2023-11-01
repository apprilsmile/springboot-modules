package com.appril.model.internal.service;


import com.appril.dto.OutsideServiceRespTimeReportDto;
import com.appril.model.internal.entity.TraceReportExternal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * TraceReportExternalMysqlService
 */
public interface TraceReportExternalMysqlService extends IService<TraceReportExternal> {
    /**
     * findRespTimeByCurrentHour
     *
     * @param startTime
     * @return OutsideServiceRespTimeReportDto
     */
    List<OutsideServiceRespTimeReportDto> findRespTimeByCurrentHour(String startTime);
}
