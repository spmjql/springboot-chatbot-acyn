package com.nowon.bul.rabbitmq;

import java.util.Vector;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

//어플리케이션 종료 시 실행 되는 클래스
@RequiredArgsConstructor
@Component
public class ApplicationContextListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
    	//SimpleMessageListenerContainer 종료
    	Vector<SimpleMessageListenerContainer> containers = RabbitMQService.simpleMessageListenerContainerActivateList;
    	System.out.println(">>>>>리스너 컨테이너 종료"+containers);
    	containers.forEach(container->container.stop());
    }
}