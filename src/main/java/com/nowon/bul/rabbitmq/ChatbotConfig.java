package com.nowon.bul.rabbitmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ChatbotConfig {

	private final RabbitMQService rabbitMQService;
	
	@Value("${spring.rabbitmq.template.prefixName}")
	private String prefixName;
	@Value("${spring.rabbitmq.template.suffixQueueName}")
	private String suffixQueueName;

	private String mainName = "chatbot";
	
	private String queueName = prefixName+mainName+suffixQueueName;
	private String exchangeName = prefixName+mainName;
	
	@PostConstruct
	public void createChatbotQueue() {
		rabbitMQService.createQueue(mainName);
		rabbitMQService.createExchange(mainName);
		rabbitMQService.bindQueueToExchange(mainName);
	}
}

