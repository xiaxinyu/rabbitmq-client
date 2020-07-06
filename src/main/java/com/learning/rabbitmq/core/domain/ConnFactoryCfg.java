package com.learning.rabbitmq.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author XIAXINYU3
 * @date 2020.7.2
 */
@Setter
@Getter
@ToString
@Builder
public class ConnFactoryCfg {
    private String hostName;
    private Integer port;
    private String userName;
    private String password;
    private String virtualHost;
}
