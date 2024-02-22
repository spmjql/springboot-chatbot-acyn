package com.nowon.bul.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.nowon.bul.domain.dto.MyMessage;


@Component
public class DynamicRabbitListener {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	public void receiveMessage(Question message) {
		System.out.println(">>>>receiveMessage수신된 메세지:"+ message);
		simpMessagingTemplate.convertAndSend("/topic/order/"+message.getKey(), message);
	}
	
	public void chatbotMessage(Question message) {
		System.out.println(">>>>chatbotMessage수신된 메세지:"+ message);
		simpMessagingTemplate.convertAndSend("/topic/order/"+message.getKey(), message);
	}
}