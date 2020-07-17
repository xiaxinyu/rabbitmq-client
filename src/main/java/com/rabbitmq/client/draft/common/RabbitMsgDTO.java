package com.rabbitmq.client.draft.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * @param <T>
 * @author XIAXINYU3
 * @date 2020.7.2
 */
@Setter
@Getter
@ToString
@Builder
public class RabbitMsgDTO<T> implements Serializable {

    /**
     * 消息类型
     */
    private RabbitMsgType msgType;

    /**
     * 数据
     */
    private T data;
}
