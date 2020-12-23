package com.appril.controller;

import com.appril.dto.Order;
import com.appril.listener.TransactionMessageListener;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mqProducer")
public class MQController {
    public static final Logger LOGGER = LoggerFactory.getLogger(MQController.class);

    @Autowired
    private DefaultMQProducer defaultMQProducer;


    /**
     * 发送同步MQ消息
     *
     * @param msg
     * @return
     */
    @GetMapping("/syncSend")
    public void syncSend(String msg) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        LOGGER.info("发送MQ消息内容：" + msg);
        //Broker禁止自动创建Topic可通过控制台新增Topic
        Message sendMsg = new Message("TestTopic", "TestTag", msg.getBytes());
        // 默认3秒超时
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        LOGGER.info("消息发送响应：" + sendResult.toString());
    }

    /**
     * 发送异步MQ消息
     *
     * @param msg
     * @return
     */
    @GetMapping("/asyncSend")
    public void asyncSend(String msg) throws InterruptedException, RemotingException, MQClientException {
        LOGGER.info("发送MQ消息内容：" + msg);
        //Broker禁止自动创建Topic可通过控制台新增Topic
        Message sendMsg = new Message("TestTopic", "TestTag", msg.getBytes());
        // 默认3秒超时
        defaultMQProducer.send(sendMsg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.printf("success:"+ sendResult.getMsgId());
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.printf("exception:"+ throwable);
            }
        });
    }

    /**
     * 发送单向MQ消息
     *
     * @param msg
     * @return
     */
    @GetMapping("/oneWay")
    public void oneWay(String msg) throws InterruptedException, RemotingException, MQClientException {
        LOGGER.info("发送MQ消息内容：" + msg);
        //Broker禁止自动创建Topic可通过控制台新增Topic
        Message sendMsg = new Message("TestTopic", "TestTag", msg.getBytes());
        // 默认3秒超时
        defaultMQProducer.sendOneway(sendMsg);
    }

    /**
     * 发送顺序MQ消息
     *
     * @param msg
     * @return
     */
    @GetMapping("/orderSend")
    public void orderSend(String msg) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        LOGGER.info("发送MQ消息内容：" + msg);
        //Broker禁止自动创建Topic可通过控制台新增Topic
        Message sendMsg = new Message("TestTopic", "TestTag", msg.getBytes());
        List<Order> orderList = new ArrayList<>();
        Order order1 = new Order();
        Order order2 = new Order();
        Order order3 = new Order();
        order1.setId(10086);
        order1.setId(1);
        orderList.add(order1);
        order3.setId(10089);
        order3.setId(1);
        orderList.add(order3);
        order1.setId(10086);
        order1.setId(2);
        orderList.add(order1);
        order2.setId(10087);
        order2.setId(1);
        orderList.add(order2);
        order1.setId(10086);
        order1.setId(3);
        orderList.add(order1);
        order1.setId(10086);
        order1.setId(4);
        orderList.add(order1);
        order2.setId(10087);
        order2.setId(2);
        orderList.add(order2);
        order1.setId(10086);
        order1.setId(5);
        orderList.add(order1);
        order3.setId(10089);
        order3.setId(2);
        orderList.add(order3);
        // 默认3秒超时
        for (int i = 0; i < orderList.size(); i++) {
            defaultMQProducer.send(sendMsg, (list, message, o) -> {
                Long id = (Long) o;
                long index = id % list.size();
                return list.get((int)index);
            },orderList.get(i).getId());
        }
    }

    @Autowired
    private TransactionMessageListener transactionListener;
    /**
     * 发送事务MQ消息
     *
     * @param msg
     * @return
     */
    @GetMapping("/sendMessageInTransaction")
    public void sendMessageInTransaction(String msg) throws MQClientException {
        LOGGER.info("发送事务MQ消息内容：" + msg);
        //Broker禁止自动创建Topic可通过控制台新增Topic
        Message sendMsg = new Message("TestTopic", "TestTag", msg.getBytes());
        // 延迟消息 messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
//        sendMsg.setDelayTimeLevel(1);
        TransactionMQProducer producer = new TransactionMQProducer("transact-rocket");
        producer.setNamesrvAddr("192.168.48.128:9876");
        producer.setTransactionListener(transactionListener);
        producer.start();
        producer.sendMessageInTransaction(sendMsg,null);
//        producer.shutdown();
    }
}