package com.nowon.bul.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

//spring boot의 자동구성을 사용하여 ConnectionFactory, RabbitTemplate 생성
@RequiredArgsConstructor
@Configuration
@EnableRabbit
public class RabbitMQConfig {

	@Value("${spring.rabbitmq.template.exchange}")
	private String exchange;
	@Value("${spring.rabbitmq.template.routing-key}")
	private String routingKey;
	@Value("${spring.rabbitmq.template.default-receive-queue}")
	private String queueName;
	
	private final ConnectionFactory connectionFactory;
	
	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}
	
	@Bean
	TopicExchange exchange() {
		return new TopicExchange(exchange);
	}
	@Bean
	Binding binding() {
		return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
	}
	

    @Bean
    SimpleRabbitListenerContainerFactory myFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //ConnectionFactory connectionFactory = getCustomConnectionFactory();
        configurer.configure(factory, connectionFactory);
        //factory.setMessageConverter(new MyMessageConverter());
        return factory;
    }
    
    @Bean
    SimpleMessageListenerContainer container(Receiver receiver) {
    	SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    	container.setConnectionFactory(connectionFactory);
    	container.setQueueNames(queueName);
    	container.setMessageListener(messageListenerAdapter(receiver));
    	return container;
    }
    
    //
    MessageListenerAdapter messageListenerAdapter(Receiver receiver) {
    	MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "receiveMessage");
    	messageListenerAdapter.setMessageConverter(messageConverter());
    	return messageListenerAdapter;
    }

	//Jackson2JsonMessageConverter는 
	//Spring의 Jackson 라이브러리를 사용하여 객체를 JSON 형식으로 직렬화하고 역직렬화하는 MessageConverter의 구현체
	//이렇게 등록된 MessageConverter는 RabbitMQ와의 통신에서 자동으로 적용되어 메시지의 직렬화 및 역직렬화를 처리
	@Bean
	MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

}