package com.learning.rabbitmq.core;

import com.learning.rabbitmq.LearningRabbitMqApplication;
import com.learning.rabbitmq.biz.TestConsumer;
import com.learning.rabbitmq.common.RabbitMsgDTO;
import com.learning.rabbitmq.common.RabbitMsgType;
import com.learning.rabbitmq.core.domain.ConnFactoryCfg;
import com.learning.rabbitmq.core.domain.ExchangeCfg;
import com.learning.rabbitmq.core.domain.QueueCfg;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LearningRabbitMqApplication.class})
public class FanoutTest {
    @Autowired
    RabbitMqUtils rabbitMqUtils;

    @Autowired
    RabbitMsgSender rabbitMsgSender;

//    @Autowired
//    SimpleMessageListenerContainer container;

    private Channel channel;

    private RabbitTemplate rabbitTemplate;

    private String exchange = "fanout-exchange";

    private String queue1 = "fanout-queue1";

    private String queue2 = "fanout-queue2";

    @Before
    public void before() throws IOException {
        ConnFactoryCfg connFactoryCfg = ConnFactoryCfg.builder().hostName("localhost").port(5672).userName("test").password("test").virtualHost("test").build();
        ConnectionFactory connectionFactory = rabbitMqUtils.createConnectionFactory(connFactoryCfg);

        Connection connection = rabbitMqUtils.createConnection(connectionFactory);

        this.channel = rabbitMqUtils.createChannel(connection, false);

        this.rabbitTemplate = rabbitMqUtils.createRabbitTemplate(connectionFactory);
    }

    @Test
    public void testDeleteAll() throws IOException {
        this.rabbitMqUtils.removeQueue(this.channel, queue1);
        this.rabbitMqUtils.removeQueue(this.channel, queue2);
        this.rabbitMqUtils.removeExchange(this.channel, exchange);
    }

    @Test
    public void testCreateFanoutAll() throws IOException {
        testDeleteAll();

        ExchangeCfg exchangeCfg = ExchangeCfg.builder().exchange(this.exchange).exchangeType(BuiltinExchangeType.FANOUT).autoDelete(false).durable(true).build();
        AMQP.Exchange.DeclareOk exchangeOk = rabbitMqUtils.createExchange(this.channel, exchangeCfg);
        Assert.assertNotNull(exchangeOk);

        log.info("Create Exchange: {}", exchangeOk);


        //Queue1
        QueueCfg queueCfg1 = QueueCfg.builder().queue(this.queue1).autoDelete(false).exclusive(false).durable(true).build();
        AMQP.Queue.DeclareOk queueOk1 = rabbitMqUtils.createQueue(this.channel, queueCfg1);
        Assert.assertNotNull(queueOk1);

        log.info("Create Queue1: {}", queueOk1);

        AMQP.Queue.BindOk bindOk1 = rabbitMqUtils.bindQueue(this.channel, exchangeCfg, queueCfg1, this.queue1);
        Assert.assertNotNull(bindOk1);

        log.info("Create Bind1: {}", bindOk1);


        //Queue2
        QueueCfg queueCfg2 = QueueCfg.builder().queue(this.queue2).autoDelete(false).exclusive(false).durable(true).build();
        AMQP.Queue.DeclareOk queueOk2 = rabbitMqUtils.createQueue(this.channel, queueCfg2);
        Assert.assertNotNull(queueOk2);

        log.info("Create Queue2: {}", queueOk2);

        AMQP.Queue.BindOk bindOk2 = rabbitMqUtils.bindQueue(this.channel, exchangeCfg, queueCfg2, this.queue2);
        Assert.assertNotNull(bindOk2);

        log.info("Create Bind2: {}", bindOk2);
    }

    @Test
    public void testPurgeMessage() throws IOException {
        rabbitMqUtils.queuePurge(this.channel, this.queue1);
        rabbitMqUtils.queuePurge(this.channel, this.queue2);
    }

    @Test
    public void testSendMessage() throws IOException {
        TestConsumer consumer = new TestConsumer(this.channel);
        this.channel.basicConsume(this.queue1, false, consumer);
        this.channel.basicConsume(this.queue2, false, consumer);

        Map<String, String> message = new HashMap<>();
        message.put("Hello", "World");

        RabbitMsgDTO rabbitMsgDto = RabbitMsgDTO.builder().msgType(RabbitMsgType.PLAIN).data(message).build();
        rabbitMsgSender.sendMsg(this.rabbitTemplate, rabbitMsgDto, this.exchange, this.queue1, 1000L);
    }
}
