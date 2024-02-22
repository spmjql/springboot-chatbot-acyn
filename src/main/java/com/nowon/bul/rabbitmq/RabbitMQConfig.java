package com.nowon.bul.rabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.RequiredArgsConstructor;


//spring boot의 자동구성을 사용하여 ConnectionFactory, RabbitTemplate 생성
@RequiredArgsConstructor
@Configuration
@EnableRabbit
public class RabbitMQConfig {
	
	private final ConnectionFactory connectionFactory;
	
	//Jackson2JsonMessageConverter는 
	//Spring의 Jackson 라이브러리를 사용하여 객체를 JSON 형식으로 직렬화하고 역직렬화하는 MessageConverter의 구현체
	//이렇게 등록된 MessageConverter는 RabbitMQ와의 통신에서 자동으로 적용되어 메시지의 직렬화 및 역직렬화를 처리
	@Bean
	MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	//RabbitMQ 메시지를 수신하기 위한 리스너 컨테이너를 생성하는 데 사용되는 Bean
	//RabbitMQ와의 통신에 필요한 리스너 컨테이너를 구성하고 관리할 수 있음.
	//1. 리스너 컨테이너의 스레드 관리를 담당합니다. 이를 통해 리스너의 동작을 제어하고, 스레드 풀을 관리하여 메시지 처리의 성능과 확장성을 조절할 수 있음
	//2. 커스텀한 설정을 적용할 수 있습니다. 예를 들어, 메시지 변환기(MessageConverter), Acknowledge 모드, 쓰레드 풀 사이즈 등을 설정할 수 있습니다. 이를 통해 다양한 요구사항에 맞게 리스너 컨테이너를 조정할 수 있음
	//3. 여러 개의 리스너를 동시에 처리할 수 있는 컨테이너를 생성할 수 있습니다. 각 리스너는 별도의 설정을 가질 수 있으며, SimpleRabbitListenerContainerFactory를 사용하여 각 리스너의 독립적인 환경을 구성할 수 있음
	@Primary
	@Bean
	SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory=new SimpleRabbitListenerContainerFactory();
		System.out.println(">>>>:"+connectionFactory);
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter());
		return factory;
	}
	

}