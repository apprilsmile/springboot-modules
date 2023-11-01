package com.appril.adaptor.impl;

import com.alibaba.fastjson.JSON;
import com.appril.adaptor.OutsideServiceAdaptor;
import com.appril.model.mongo.entity.OutsideServiceCallRecord;
import com.appril.model.mongo.service.SimpleMongoService;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyang
 * @date 2023/10/31 18:36
 */
public class MongoOutsideServiceAdaptor implements OutsideServiceAdaptor {

    private final SimpleMongoService simpleMongoService;

    public  MongoOutsideServiceAdaptor(SimpleMongoService mongoService) {
        this.simpleMongoService = mongoService;
    }

    @Override
    public String query() {
        Map<String, String> conditionMap = new HashMap<>(1);
        conditionMap.put("outsideServiceSerialNo", "1830938846708338688");
        List<OutsideServiceCallRecord> callRecord = simpleMongoService.selectListByCondition(
                "outside_service_call_record", OutsideServiceCallRecord.class, conditionMap);
        if (CollectionUtils.isEmpty(callRecord)) {
            return null;
        }
        return JSON.toJSONString(callRecord.get(0));
    }
}
