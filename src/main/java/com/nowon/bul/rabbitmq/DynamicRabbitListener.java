package com.nowon.bul.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.nowon.bul.domain.dto.MyMessage;


@Component
public class DynamicRabbitListener {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	public void receiveMessage(MyMessage message) {
		System.out.println(">>>>수신된 메세지:"+ message);
		if(message.getFrom().equals("user")) {
			simpMessagingTemplate.convertAndSend("/topic/order/"+message.getName(), message);
		}else {
			simpMessagingTemplate.convertAndSend("/topic/order/"+message.getName()+"/"+message.getKey(), message);
		}
	}

}