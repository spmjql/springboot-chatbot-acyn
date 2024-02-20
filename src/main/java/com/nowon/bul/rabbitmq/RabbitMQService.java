package com.nowon.bul.rabbitmq;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RabbitMQService {
	
	private final AmqpAdmin amqpAdmin;
	@Value("${spring.rabbitmq.template.prefixName}")
	private String prefixName;
	@Value("${spring.rabbitmq.template.suffixQueueName}")
	private String suffixQueueName;
	
	
	Queue queue;
	// 큐 생성
    public void createQueue(String branchName) {
    	String queueName=prefixName+branchName+suffixQueueName;
        queue = new Queue(queueName);
        amqpAdmin.declareQueue(queue);
    }

    // 익스체인지 생성
    public void createExchange(String branchName) {
        //Exchange exchange = new DirectExchange(exchangeName);
    	String exchangeName=prefixName+branchName;
        Exchange exchange = new TopicExchange(exchangeName);
        amqpAdmin.declareExchange(exchange);
    }
    
    // 큐와 익스체인지를 라우팅 키로 바인딩
    public void bindQueueToExchange(String branchName) {
    	String queueName=prefixName+branchName+suffixQueueName;
    	String exchangeName=prefixName+branchName;
    	String routingKey=branchName+".#";
    	
    	Binding binding = BindingBuilder
                .bind(new Queue(queueName))
                .to(new TopicExchange(exchangeName))
                .with(routingKey);
        amqpAdmin.declareBinding(binding);
        
        //바인딩이 완료되면 리스너 생성
        createListenerContainer(queue);
	}
    
    private final DynamicRabbitListener dynamicRabbitListener;
	private final MessageConverter messageConverter;
    private final SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory;
    
    // 동적으로 리스너 컨테이너 생성
    private void createListenerContainer(Queue queue) {
    	
        SimpleMessageListenerContainer container = simpleRabbitListenerContainerFactory.createListenerContainer();
        container.setQueues(queue);
        // MyMessageListener 클래스의 handleMessage 메서드를 호출하는 리스너 설정
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(dynamicRabbitListener, "receiveMessage");
        messageListenerAdapter.setMessageConverter(messageConverter);
        
        container.setMessageListener(messageListenerAdapter);
        container.start();
        System.out.println(">>>:리스너 생성"+container);
    }
}