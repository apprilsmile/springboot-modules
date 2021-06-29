package com.appril.util;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * @author wm
 */
public class JsonSerializer implements Serializer<Object> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String s, Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public void close() {

    }
}

