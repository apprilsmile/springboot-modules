package com.appril.consumer;

import com.alibaba.fastjson.JSON;
import com.appril.dto.ScoreKafkaDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author appril
 */
@Component
public class SimpleKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(SimpleKafkaConsumer.class);

    private static  final String SIMPLE_TOPIC = "simple-topic";

    /**
     *
     * @param record
     */
    @KafkaListener(id = "score_consumer", topics = {SIMPLE_TOPIC})
    public void sqScoreListener(ConsumerRecord<String, byte[]> record) {
        try {
            ScoreKafkaDTO dto = JSON.parseObject(record.value(), ScoreKafkaDTO.class);
            Long rcCstBscInfoId = dto.getRcCstBscInfoId();
            logger.info("kafka消息接收成功，topic=" + SIMPLE_TOPIC + " ，id：" + rcCstBscInfoId + ", content=\n" + JSON.toJSONString(dto));
        } catch (Exception e) {
            logger.error("消费kafka消息异常", e);
        }
    }
}
