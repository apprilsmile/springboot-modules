package com.appril.biz;


import com.alibaba.fastjson.JSON;
import com.appril.adaptor.OutsideServiceAdaptor;
import com.appril.model.primary.entity.OutsideService;
import com.appril.model.primary.service.OutsideServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class OutsideServiceBiz {
    @Resource
    private OutsideServiceAdaptor outsideServiceAdaptor;

    @Resource
    private OutsideServiceService outsideServiceService;
    public String query(){
        OutsideService byId = outsideServiceService.getById(8000);
        System.out.println(JSON.toJSONString(byId));
        return outsideServiceAdaptor.query();
    }

}
