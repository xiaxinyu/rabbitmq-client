package com.learning.rabbitmq.core.domain;

import com.rabbitmq.client.BuiltinExchangeType;
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
public class ExchangeCfg {
    private BuiltinExchangeType exchangeType;
    private String exchange;
    private boolean durable;
    private boolean autoDelete;
    private Map<String, Object> arguments;
}
