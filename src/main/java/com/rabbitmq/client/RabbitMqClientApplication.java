package com.rabbitmq.client;
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
public class RabbitMqClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMqClientApplication.class, args);
    }
}
