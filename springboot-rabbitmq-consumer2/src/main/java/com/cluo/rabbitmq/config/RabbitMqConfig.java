package com.cluo.rabbitmq.config;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
*@Author luolei
*@Date  2019/8/20 9:43
*@Description  RabbitMq配置 用于将消息队列与消息交换机进行绑定
*/
@Configuration
public class RabbitMqConfig {
       private Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);
        /**
        * routingKey
        */
        public static final String ROUTINGKEY1 = "order.*";
        public static final String ROUTINGKEY2 = "sale.#";

        @Autowired
        private QueueConfig queueConfig;
        @Autowired
        private ExchangeConfig exchangeConfig;

        /**
         * 连接工厂
         */
        @Autowired
        private ConnectionFactory connectionFactory;


        /**
         * 将消息队列和交换机进行绑定并关联routingKey
         */
        @Bean
        public Binding bindingExchange1() {
            return BindingBuilder.bind(queueConfig.secondQueue()).to(exchangeConfig.topicExchange()).with(RabbitMqConfig.ROUTINGKEY1);
        }

        @Bean
        public Binding bindingExchange2() {
            return BindingBuilder.bind(queueConfig.secondQueue()).to(exchangeConfig.topicExchange()).with(RabbitMqConfig.ROUTINGKEY2);
        }



        /**
         * 消息确认机制
         * Confirms给客户端一种轻量级的方式，能够跟踪哪些消息被broker处理，
         * 哪些可能因为broker宕掉或者网络失败的情况而重新发布。
         * 确认并且保证消息被送达，提供了两种方式：发布确认和事务。(两者不可同时使用)
         * 在channel为事务时，不可引入确认模式；同样channel为确认模式下，不可使用事务。
         * @return
         */
        @Bean
        public MsgSendConfirmCallBack msgSendConfirmCallBack(){
            return new MsgSendConfirmCallBack();
        }


        @Bean
        public RabbitTemplate rabbitTemplate() {
            RabbitTemplate template = new RabbitTemplate(connectionFactory);
            template.setMessageConverter(new Jackson2JsonMessageConverter());
            return template;
        }

        /**
        *@Description   消息转换器使用了RabbitMQ自带的Jackson2JsonMessageConverter转换器，但是没有指定消息的contentType类型
         *            所以需要手动设置content-type,这里用到了ContentTypeDelegatingMessageConverter
        *@Author  luolei
        *@Date 2019/8/21 15:17
        *@Param  
        *@Return  
        *@Exception 
        *
        **/
        @Bean
        public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
            SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
            factory.setConnectionFactory(connectionFactory);
            ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();
            converter.addDelegate(MessageProperties.CONTENT_TYPE_JSON, new ContentTypeDelegatingMessageConverter());
            //factory.setMessageConverter(new Jackson2JsonMessageConverter());
            factory.setMessageConverter(converter);
            return factory;
        }


}



