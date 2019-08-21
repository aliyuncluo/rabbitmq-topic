package com.cluo.rabbitmq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author luolei
 * @Date 2019/8/20 9:27
 * @Description
 */
@RestController
public class ProducerController {

    private Logger logger = LoggerFactory.getLogger(ProducerController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private AmqpTemplate amqpTemplate;

    /**
    *@Description 简单消息模式和work模式
    *@Author  luolei
    *@Date 2019/8/20 10:26
    *@Param
    *@Return
    *@Exception
    *
    **/
    @GetMapping("/test/send1")
    public Map<String,Object> sendSimpleMsg(String message){
        Map<String,Object> result = new HashMap<>();
        logger.info("发送的消息内容为："+message);
        String routingKey = "simple_queue";
        rabbitTemplate.convertAndSend(routingKey,message);
        result.put("exec","S");
        result.put("code","10000");
        result.put("message","消息发送成功");
        return result;
    }

    /**
    *@Description 消息的发布与订阅模式（Publish/Subscribe）
    *@Author  luolei
    *@Date 2019/8/20 13:26
    *@Param
    *@Return
    *@Exception
    *
    **/
    @GetMapping("/test/send2")
    public Map<String,Object> sendPublicMessage(String message){
        Map<String,Object> result = new HashMap<>();
        final String FANOUT_EXCHANGE = "fanout_exchange";
        //String routingKey = "first_queue";
        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE,"",message);
        result.put("exec","S");
        result.put("code","10000");
        result.put("message","消息发送成功");
        return result;
    }

    /**
    *@Description 路由模式（Routing） P发送消息到交换机并且要指定Routngkey，消费者将队列绑定到交换机时需要指定Bindingkey。
    *@Author  luolei
    *@Date 2019/8/20 17:10
    *@Param
    *@Return
    *@Exception
    *
    **/
    @GetMapping("/test/send3")
    public Map<String,Object> sendRoutingMessage(@RequestParam("routingKey") String routingKey,
                                                 @RequestParam("message") String message){
        Map<String,Object> result = new HashMap<>();
        final String DIRECT_EXCHANGE="direct_exchange";
        rabbitTemplate.convertAndSend(DIRECT_EXCHANGE,routingKey,message);
        result.put("exec","S");
        result.put("code","10000");
        result.put("message","消息发送成功");
        return result;
    }


    /**
    *@Description  主题模式（Topic）
    *@Author  luolei
    *@Date 2019/8/20 17:39
    *@Param  
    *@Return  
    *@Exception 
    *
    **/
    @GetMapping("/test/send4")
    public Map<String,Object> sendTopicMessage(){
        //logger.info("发送的消息内容为："+message);
        Map<String,Object> result = new HashMap<>();
        final String TOPIC_EXCHANGE="topic_exchange";
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE,"user.info","这是一条用户的信息");
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE,"role.info.detail","这是一条角色的详细信息");
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE,"order.info","这是一条订单的信息");
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE,"sale.info.detail","这是一条销售的详细信息");
        result.put("exec","S");
        result.put("code","10000");
        result.put("message","消息发送成功");
        return result;
    }
}
