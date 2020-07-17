package com.rabbitmq.client.draft.core;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.draft.common.RabbitMsgDTO;
import com.rabbitmq.client.draft.common.RabbitUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @author XIAXINYU3
 * @date 2020.7.2
 */
@Slf4j
@Component
public class RabbitMsgSender {

    private boolean isEnable = true;

    public void sendMsg(RabbitTemplate rabbitTemplate, RabbitMsgDTO rabbitMsgDto, String exchange, String routingKey, Long timeOut) throws UnsupportedEncodingException {
        log.info("发送MQ消息开关是否开启:{}", isEnable);
        if (isEnable) {
            String correlationId = RabbitUtils.generateId();
            String msg = JSON.toJSONString(rabbitMsgDto.getData());
            log.info("发送MQ消息: exchange={}, routingKey={}, msgType={}, correlationId={}, content={}",
                    exchange, routingKey, rabbitMsgDto.getMsgType(), correlationId, msg);

            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(correlationId);

            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");
            messageProperties.setContentEncoding("UTF-8");
            messageProperties.setExpiration(String.valueOf(timeOut));
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            messageProperties.setContentLength(msg.length());

            Message rabbitMessage = new Message(msg.getBytes("utf-8"), messageProperties);

            rabbitTemplate.convertAndSend(exchange, routingKey, rabbitMessage, correlationData);
        }
    }

    public void sendMsg(RabbitTemplate rabbitTemplate, RabbitMsgDTO rabbitMsgDto) throws UnsupportedEncodingException {
        String msg = JSON.toJSONString(rabbitMsgDto.getData());
        log.info("发送MQ消息类型是:{}, 消息内容:{}", rabbitMsgDto.getMsgType(), msg);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        messageProperties.setContentEncoding("UTF-8");
        Message rabbitMessage = new Message(msg.getBytes("utf-8"), messageProperties);

        rabbitTemplate.send(rabbitMessage);
    }
}
