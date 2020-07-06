package com.learning.rabbitmq.core.domain;

import lombok.*;

import java.util.Map;

/**
 * 交换机模式
 *
 * @author XIAXINYU3
 * @date 2020.5.20
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class QueueCfg {
    private ExchangeCfg exchangeDetail;
    private String queue;
    private boolean durable;
    private boolean exclusive;
    private boolean autoDelete;
    private Map<String, Object> args;
}
