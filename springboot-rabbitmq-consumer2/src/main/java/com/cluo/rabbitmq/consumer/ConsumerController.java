package com.cluo.rabbitmq.consumer;

import com.cluo.rabbitmq.config.QueueConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @Author luolei
 * @Date 2019/8/20 10:52
 * @Description
 */
@Controller
public class ConsumerController {

    private Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    @RabbitListener(queues = {QueueConfig.QUEUE_NAME},containerFactory = "rabbitListenerContainerFactory")
    @RabbitHandler
    public void receiveMessage(String message){
        //处理消息
        logger.info("接收到的消息为"+message);
    }

//    @RabbitListener(queues = {QueueConfig.QUEUE_NAME},containerFactory = "rabbitListenerContainerFactory")
//    public void receiveMessage(@Payload String message, Message msg, Channel channel){
//        try {
//            channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
//            logger.info("接收消息的ID为："+msg.getMessageProperties().getDeliveryTag());
//            //处理消息
//            logger.info("接收到的消息为"+message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
