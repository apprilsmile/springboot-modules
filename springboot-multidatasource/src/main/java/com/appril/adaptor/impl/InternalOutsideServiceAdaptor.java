package com.appril.adaptor.impl;

import com.alibaba.fastjson.JSON;
import com.appril.adaptor.OutsideServiceAdaptor;
import com.appril.model.internal.service.TraceReportExternalMysqlService;

/**
 * @author zhangyang
 * @date 2023/10/31 18:36
 */
public class InternalOutsideServiceAdaptor implements OutsideServiceAdaptor {

    private TraceReportExternalMysqlService externalMysqlService;

    public InternalOutsideServiceAdaptor(TraceReportExternalMysqlService externalService){
        this.externalMysqlService = externalService;
    }
    @Override
    public String query() {
        return JSON.toJSONString(externalMysqlService.findRespTimeByCurrentHour("2023-10-10"));
    }
}
