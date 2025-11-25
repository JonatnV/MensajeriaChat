package com.proto.MensajeriaChat.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "chat.exchange";
    public static final String GENERAL_QUEUE = "chat.general";

    @Bean
    public TopicExchange chatExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue generalQueue() {
        return QueueBuilder.durable(GENERAL_QUEUE).build();
    }

    @Bean
    public Binding binding(Queue generalQueue, TopicExchange chatExchange) {
        // escucha a routing keys room.# para enrutar mensajes de todas las salas
        return BindingBuilder.bind(generalQueue).to(chatExchange).with("room.#");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        RabbitTemplate rt = new RabbitTemplate(cf);
        rt.setMessageConverter(converter);
        return rt;
    }
}