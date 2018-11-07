package com.taohong.snapup.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author taohong on 07/11/2018
 */
@Configuration
public class MQConfig {
    public static final String QUEUE = "queue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

}
