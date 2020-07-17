package com.rabbitmq.client.spring;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class RabbitMQConfig {
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("127.0.0.1:5672");
        connectionFactory.setVirtualHost("/vhost_dwz");
        connectionFactory.setUsername("root_dwz");
        connectionFactory.setPassword("123456");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        System.err.println("RabbitAdmin启动了。。。");
        //设置启动spring容器时自动加载这个类(这个参数现在默认已经是true，可以不用设置)
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    /**
     *     针对消费者的配置
     * 1.设置交换机的类型
     * 2.将队列绑定到交换机
     * FanoutExchange:将消息分发到所有绑定的队列，无routingkey的概念
     * TopicExchange:多关键字匹配
     * HeadersExchange：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     */
    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("topic001", true, false);
    }

    @Bean
    public Queue queue001() {
        return new Queue("queue001", true);//队列持久化
    }

    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("topic002", true, false);
    }

    @Bean
    public Queue queue002() {
        return new Queue("queue002", true);//队列持久化
    }

    @Bean
    public Binding binding002() {
        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }

    @Bean
    public TopicExchange exchange003() {
        return new TopicExchange("topic003", true, false);
    }

    @Bean
    public Queue queue003() {
        return new Queue("queue003", true);//队列持久化
    }

    @Bean
    public Binding binding003() {
        return BindingBuilder.bind(queue003()).to(exchange003()).with("mq.*");
    }

    @Bean
    public Queue queue_image() {
        return new Queue("image_queue", true);//队列持久化
    }

    @Bean
    public Queue queue_pdf() {
        return new Queue("pdf_queue", true);//队列持久化
    }

    /*
     *     简单消息监听容器
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //同时监听多个队列
        container.setQueues(queue001(), queue002(), queue003(), queue_image(), queue_pdf());
        //设置当前的消费者数量
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        //设置是否重回队列
        container.setDefaultRequeueRejected(false);
        //设置自动签收
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
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
                System.out.println("-----------消费者：" + msg);
            }
        });
        return container;
    }
}
