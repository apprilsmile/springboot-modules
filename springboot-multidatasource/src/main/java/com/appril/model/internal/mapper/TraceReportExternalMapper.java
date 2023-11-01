package com.appril.model.internal.mapper;

import com.appril.dto.OutsideServiceRespTimeReportDto;
import com.appril.model.internal.entity.TraceReportExternal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface TraceReportExternalMapper extends BaseMapper<TraceReportExternal> {
    /**
     * findRespTimeByCurrentHour
     *
     * @param startTime
     * @return OutsideServiceRespTimeReportDto
     */
    List<OutsideServiceRespTimeReportDto> findRespTimeByCurrentHour(String startTime);

}
