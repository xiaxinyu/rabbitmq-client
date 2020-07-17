package com.rabbitmq.client.draft.core;

import com.rabbitmq.client.draft.core.domain.ConnFactoryCfg;
import com.rabbitmq.client.draft.core.domain.QueueCfg;
import com.rabbitmq.client.draft.core.domain.ExchangeCfg;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author XIAXINYU3
 * @date 2020.7.2
 */
@Component
@Slf4j
public class RabbitMqUtils {

    public ConnectionFactory createConnectionFactory(ConnFactoryCfg factoryCfg) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(factoryCfg.getHostName());
        connectionFactory.setUsername(factoryCfg.getUserName());
        connectionFactory.setPassword(factoryCfg.getPassword());
        connectionFactory.setPassword(factoryCfg.getPassword());
        connectionFactory.setPort(factoryCfg.getPort());
        connectionFactory.setVirtualHost(factoryCfg.getVirtualHost());
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setBeanName("customizationConnectionFactory");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    public Connection createConnection(ConnectionFactory connectionFactory) {
        Connection connection = connectionFactory.createConnection();
        return connection;
    }

    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        //设置发送消息失败重试
        rabbitTemplate.setMandatory(true);

        //使用单独的发送连接，避免生产者由于各种原因阻塞而导致消费者同样阻塞
        rabbitTemplate.setUsePublisherConnection(true);


        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            //true表示消息发送成功
            if (ack) {
                log.info("ConfirmCallback success: correlationDataId={}", correlationData.getId());
            } else {
                log.error("ConfirmCallback fail: correlationDataId={}", correlationData.getId(), cause);
            }
        });

        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info(MessageFormat.format("ReturnCallback : {0}, {1}, {2}, {3}, {4}, {5}",
                    message, replyCode, replyText, exchange, routingKey));
        });

        return rabbitTemplate;
    }

    public Channel createChannel(Connection connection, boolean transactional) {
        return connection.createChannel(transactional);
    }

    public AMQP.Exchange.DeclareOk createExchange(Channel channel, ExchangeCfg exchangeCfg) throws IOException {
        return channel.exchangeDeclare(exchangeCfg.getExchange(), exchangeCfg.getExchangeType(), exchangeCfg.isDurable(), exchangeCfg.isAutoDelete(), exchangeCfg.getArguments());
    }

    public AMQP.Exchange.DeleteOk removeExchange(Channel channel, String exchange) throws IOException {
        return channel.exchangeDelete(exchange);
    }

    public AMQP.Queue.DeclareOk createQueue(Channel channel, QueueCfg queueCfg) throws IOException {
        return channel.queueDeclare(queueCfg.getQueue(), queueCfg.isDurable(), queueCfg.isExclusive(), queueCfg.isAutoDelete(), queueCfg.getArgs());
    }

    public AMQP.Queue.DeleteOk removeQueue(Channel channel, String queue) throws IOException {
        return channel.queueDelete(queue);
    }

    public AMQP.Queue.PurgeOk queuePurge(Channel channel, String queue) throws IOException {
        return channel.queuePurge(queue);
    }

    public AMQP.Queue.BindOk bindQueue(Channel channel, ExchangeCfg exchangeCfg, QueueCfg queueCfg, String routingKey) throws IOException {
        if (BuiltinExchangeType.TOPIC == exchangeCfg.getExchangeType()) {
            return channel.queueBind(queueCfg.getQueue(), exchangeCfg.getExchange(), routingKey);
        } else {
            return channel.queueBind(queueCfg.getQueue(), exchangeCfg.getExchange(), routingKey);
        }
    }
}
