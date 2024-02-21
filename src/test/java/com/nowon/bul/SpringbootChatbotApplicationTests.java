package com.nowon.bul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nowon.bul.rabbitmq.ChatbotConfig;

@SpringBootTest
class SpringbootChatbotApplicationTests {
	
	@Autowired
	private ChatbotConfig testObj;

	//@Test
	void 테스트() {
		testObj.createChatbotQueue();
	}


}
