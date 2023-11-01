package com.appril.model.internal.service.impl;

import com.appril.dto.OutsideServiceRespTimeReportDto;
import com.appril.model.internal.entity.TraceReportExternal;
import com.appril.model.internal.mapper.TraceReportExternalMapper;
import com.appril.model.internal.service.TraceReportExternalMysqlService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@ConditionalOnProperty(name="spring.datasource.dynamic.type",havingValue = "internal")
public class TraceReportExternalMysqlServiceImpl extends ServiceImpl<TraceReportExternalMapper, TraceReportExternal> implements TraceReportExternalMysqlService {
    @Resource
    private TraceReportExternalMapper traceReportExternalMapper;
    @Override
    public List<OutsideServiceRespTimeReportDto> findRespTimeByCurrentHour(String startTime) {
        return traceReportExternalMapper.findRespTimeByCurrentHour(startTime);
    }
}
