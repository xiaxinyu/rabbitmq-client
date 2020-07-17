package com.rabbitmq.client.spring;

import com.rabbitmq.client.LearningRabbitMqApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LearningRabbitMqApplication.class})
public class DirectTest {


    @Before
    public void before() throws IOException {

    }


    @Test
    public void testCreateFanoutAll() throws IOException {

    }
}
