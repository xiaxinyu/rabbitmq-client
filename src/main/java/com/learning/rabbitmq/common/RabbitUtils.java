package com.learning.rabbitmq.common;

import java.util.UUID;

/**
 * @author XIAXINYU3
 * @date 2020.7.2
 */
public class RabbitUtils {

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
