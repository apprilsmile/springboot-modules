package com.appril.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;


/**
 * @author wm
 * @create 2020-11-23
 */
@Slf4j
public class JsonDeserializer implements Deserializer<Object> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] deserialize(String s, byte[] bytes) {
        JSON json = null;
        try {
//            json = JSON.parseObject(bytes, JSON.class);
            boolean isJson = JSONValidator.fromUtf8(bytes).validate();
            if (!isJson){
                log.error("kafka消息格式错误，非json格式");
            }
        }catch (Exception e){
            log.error("kafka消息格式错误，非json格式");
        }
        return bytes;
    }

    @Override
    public void close() {

    }
}

