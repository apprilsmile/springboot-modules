package com.appril.model.internal.service.impl;

import com.appril.dto.OutsideServiceRespTimeReportDto;
import com.appril.model.internal.entity.TraceReportExternal;
import com.appril.model.internal.mapper.TraceReportExternalMapper;
import com.appril.model.internal.service.TraceReportExternalMysqlService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Lazy
public class TraceReportExternalMysqlServiceImpl extends ServiceImpl<TraceReportExternalMapper, TraceReportExternal> implements TraceReportExternalMysqlService {
    @Override
    public List<OutsideServiceRespTimeReportDto> findRespTimeByCurrentHour(String startTime) {
        return getBaseMapper().findRespTimeByCurrentHour(startTime);
    }
}
