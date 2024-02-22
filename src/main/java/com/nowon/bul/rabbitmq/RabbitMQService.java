package com.nowon.bul.rabbitmq;

import java.util.Vector;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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
	@Value("${spring.rabbitmq.template.default-receive-queue}")
	private String chatbotQueueName;
	
	
	Queue queue;
	TopicExchange exchange;
	// 큐 생성
    public void createQueue(String branchName) {
    	String queueName=prefixName+branchName+suffixQueueName;
        queue = new Queue(queueName, false);
        amqpAdmin.declareQueue(queue);
    }

    // 익스체인지 생성
    public void createExchange(String branchName) {
        //Exchange exchange = new DirectExchange(exchangeName);
    	String exchangeName=prefixName+branchName;
        exchange = new TopicExchange(exchangeName);
        amqpAdmin.declareExchange(exchange);
    }
    
    // 큐와 익스체인지를 라우팅 키로 바인딩
    public void bindQueueToExchange(String branchName) {
    	String routingKey=branchName+".#";
    	
    	Binding binding = BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(routingKey);
        amqpAdmin.declareBinding(binding);
        
        //바인딩이 완료되면 리스너 생성
        createListenerContainer(queue);
	}
    
    private final DynamicRabbitListener dynamicRabbitListener;
	private final MessageConverter messageConverter;
    private final SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory;
    
    public static Vector<SimpleMessageListenerContainer> simpleMessageListenerContainerActivateList = new Vector<>();
    
    // 동적으로 리스너 컨테이너 생성
    private void createListenerContainer(Queue queue) {
    	String dynamicRabbitListener_methodName = "receiveMessage";
    	if(queue.getName().equals(chatbotQueueName)) {
    		dynamicRabbitListener_methodName = "chatbotMessage";
    	}
    	System.out.println("************"+dynamicRabbitListener_methodName);
        SimpleMessageListenerContainer container = simpleRabbitListenerContainerFactory.createListenerContainer();
        // MyMessageListener 클래스의 handleMessage 메서드를 호출하는 리스너 설정
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(dynamicRabbitListener, dynamicRabbitListener_methodName);
        messageListenerAdapter.setMessageConverter(messageConverter);
        
        container.setQueues(queue);
        container.setMessageListener(messageListenerAdapter);
        container.start();
        //서버종료시 ContextClosedEvent로 컨테이너를 종료하기 위해 객체리스트저장
        simpleMessageListenerContainerActivateList.add(container);
        System.out.println(">>>:리스너 생성"+container);
    }
}