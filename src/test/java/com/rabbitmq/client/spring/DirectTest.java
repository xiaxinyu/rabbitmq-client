package com.rabbitmq.client.spring;

import com.rabbitmq.client.RabbitMqClientApplication;
import com.rabbitmq.client.draft.common.RabbitMsgDTO;
import com.rabbitmq.client.draft.common.RabbitMsgType;
import com.rabbitmq.client.draft.core.RabbitMsgSender;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RabbitMqClientApplication.class})
public class DirectTest {
    @Autowired
    RabbitMsgSender rabbitMsgSender;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Before
    public void before() throws IOException {

    }

    @Test
    public void testCreateFanoutAll() throws IOException, InterruptedException {
        Map<String, String> message = new HashMap<>();
        message.put("Hello", "World");

        RabbitMsgDTO rabbitMsgDto = RabbitMsgDTO.builder().msgType(RabbitMsgType.PLAIN).data(message).build();
        while (true) {
            rabbitMsgSender.sendMsg(this.rabbitTemplate, rabbitMsgDto, "topic001", "spring.summer", 1000L);


            rabbitMsgSender.sendMsg(this.rabbitTemplate, rabbitMsgDto, "topic002", "rabbit.summer", 1000L);


            Thread.sleep(500);
        }
    }
}
