package com.appril.constant;

public class RabbitMqConstant {
    public  static  final String FIRST_EXCHANGE = "exchangeFirst";
    public  static  final String SECOND_EXCHANGE = "exchangeSecond";
    public  static  final String FIRST_QUEUE = "first-queue";
    public  static  final String SECOND_QUEUE = "second-queue";
    public  static  final String FIRST_BINDING_KEY = "first.*";
    public  static  final String SECOND_BINDING_KEY = "second.*";
    /**
     * Exchange
     */
    public enum MqExchange{
        FIRST_EXCHANGE("exchangeFirst","topic","第一交换机"),
        SECOND_EXCHANGE("exchangeSecond","direct","第二交换机");
        private final String value;
        private final String type;
        private final String name;
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public String getType() {
            return type;
        }
        MqExchange(String value,String type, String name){
            this.value=value;
            this.type=type;
            this.name=name;
        }
    }

    /**
     * queue
     */
    public enum MqQueue{
        FIRST_QUEUE("first-queue","第一个队列"),
        SECOND_QUEUE("second-queue","第二个队列");
        private final String value;
        private final String name;
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        MqQueue(String value, String name){
            this.value=value;
            this.name=name;
        }
    }
    /**
     * BindingKey
     */
    public enum MqBindingKey{
        FIRST_BINDING_KEY("first.*","绑定键1"),
        SECOND_BINDING_KEY("second.do","绑定键2");
        private final String value;
        private final String name;
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        MqBindingKey(String value, String name){
            this.value=value;
            this.name=name;
        }
    }
    /**
     * RoutingKey
     */
    public enum MqRoutingKey{
        FIRST_ROUTING_KEY("first.all","路由键1-all"),
        FIRST_ROUTING_KEY_DO("first.do","路由键1-do"),
        SECOND_ROUTING_KEY("second.do","路由键2");
        private final String value;
        private final String name;
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        MqRoutingKey(String value, String name){
            this.value=value;
            this.name=name;
        }
    }
}
