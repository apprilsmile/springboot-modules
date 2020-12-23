package com.appril.listener;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

@Component
public class TransactionMessageListener implements TransactionListener {
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object o) {
        //执行一系列本地业务逻辑(同步),方法内部写spring事务管理，然后如果出错失败，就抛异常，外层捕获到直接回滚通知broker
        //a()
        //b()
        //c()
        System.out.println("setTransactionListener   txID:"+msg.getTransactionId()+",body "+new String(msg.getBody()));
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        System.out.println("checkLocalTransaction   txID:"+messageExt.getTransactionId()+",body "+new String(messageExt.getBody()));
        //broker 异步线程 回调 ，回调延迟策略：在控制台后台页面信息里，cluster->config中，messageDelayLevel设置了延时回调策略
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
