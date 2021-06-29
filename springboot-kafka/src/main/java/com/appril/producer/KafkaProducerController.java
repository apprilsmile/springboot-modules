package com.appril.producer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.appril.dto.ScoreKafkaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author appril
 * @create 2021 06 29
 */
@RestController
@Slf4j
public class KafkaProducerController {

    private static  final String SIMPLE_TOPIC = "simple-topic";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * kafka发送消息
     */
    @GetMapping("/kafka/sendMsg")
    public String sendKafkaMessage() {
        String sqScoreStr = "{\"crtTm\":1539332728092,\"crtUsrId\":170920165000586,\"cstMgrBuOrgCd\":\"00000005\",\"cstMgrId\":170607165000406,\"cstMgrNm\":\"黄石\",\"cstMgrOrgCd\":\"00000005000300020005\",\"cstName\":\"武红军\",\"mp\":\"15690919444\",\"rcCstBscInfoId\":171012296000086,\"rskLvlCd\":\"2\",\"splId\":20553,\"splNm\":\"叶城县重汽配套维修服务有限公司\"}";
        ScoreKafkaDTO dto = JSONObject.parseObject(sqScoreStr,ScoreKafkaDTO.class);
        kafkaTemplate.send(SIMPLE_TOPIC, dto);
        log.info("kafka消息发送成功，topic="+SIMPLE_TOPIC+", content=\n"+ JSON.toJSONString(dto));
        return "消息发送成功";
    }


}

