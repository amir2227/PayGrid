package com.paygrid.wallet.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String USER_REGISTERED_QUEUE = "user.registered.wallet";
    public static final String USER_REGISTERED_EXCHANGE = "user.registered.exchange";
    public static final String EXCHANGE = "wallet.balance.exchange";
    public static final String QUEUE = "wallet.balance.queue";
    public static final String ROUTING_KEY = "wallet.balance.changed";

    @Bean
    public Queue userRegisteredQueue() {
        return new Queue(USER_REGISTERED_QUEUE, true);
    }

    @Bean
    public TopicExchange userRegisteredExchange() {
        return new TopicExchange(USER_REGISTERED_EXCHANGE);
    }

    @Bean
    public Binding userRegisteredBinding() {
        return BindingBuilder
                .bind(userRegisteredQueue())
                .to(userRegisteredExchange())
                .with("user.registered.*");
    }

    @Bean
    public TopicExchange walletBalanceExchange() {
        return new TopicExchange(EXCHANGE,true,false);
    }
    @Bean
    public Queue walletBalanceQueue() {
        return new Queue(QUEUE,true);
    }
    @Bean
    public Binding walletBalanceBinding() {
        return BindingBuilder
                .bind(walletBalanceQueue())
                .to(walletBalanceExchange())
                .with(ROUTING_KEY);
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
