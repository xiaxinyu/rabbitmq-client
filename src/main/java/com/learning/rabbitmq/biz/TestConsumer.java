package com.learning.rabbitmq.biz;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

/**
 * @author XIAXINYU3
 * @date 2020.7.2
 */
@Slf4j
public class TestConsumer extends DefaultConsumer {
    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public TestConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body) throws IOException {

        Object correlation = properties.getHeaders().get("spring_returned_message_correlation");
        String correlationId = "None";
        if (Objects.nonNull(correlation)) {
            correlationId = correlation.toString();
        }
        String message = new String(body, "UTF-8");
        log.info("Received Message: correlationId={}, content={}", correlationId, message);
    }
}
