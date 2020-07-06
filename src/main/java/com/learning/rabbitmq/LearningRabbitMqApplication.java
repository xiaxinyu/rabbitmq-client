package com.learning.rabbitmq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

/**
 * Learning RabbitMq Application
 *
 * @author XIAXINYU3
 * @date 2020.4.15
 */
@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
public class LearningRabbitMqApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearningRabbitMqApplication.class, args);
    }
}
