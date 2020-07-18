package com.rabbitmq.client.spring;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Slf4j
@Configuration
public class ContainerConfig {
    @Bean
    public SimpleMessageListenerContainer messageContainer1(ConnectionFactory connectionFactory, Queue queue001) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //同时监听多个队列
        container.setQueues(queue001);
        //设置当前的消费者数量
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        //设置是否重回队列
        container.setDefaultRequeueRejected(false);
        //设置自动签收
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置监听外露
        container.setExposeListenerChannel(true);
        //设置消费端标签策略
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });
        //设置消息监听
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                String msg = new String(message.getBody(), "utf-8");
                log.info("-----------消费者From：{}, 消费消息：{}", queue001.getName(), msg);
            }
        });
        return container;
    }


    @Bean
    public SimpleMessageListenerContainer messageContainer2(ConnectionFactory connectionFactory, Queue queue002) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //同时监听多个队列
        container.setQueues(queue002);
        //设置当前的消费者数量
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        //设置是否重回队列
        container.setDefaultRequeueRejected(false);
        //设置自动签收
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置监听外露
        container.setExposeListenerChannel(true);
        //设置消费端标签策略
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });
        //设置消息监听
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                String msg = new String(message.getBody(), "utf-8");
                log.info("-----------消费者From：{}, 消费消息：{}", queue002.getName(), msg);
            }
        });
        return container;
    }
}
