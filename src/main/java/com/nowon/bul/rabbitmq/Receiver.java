package com.nowon.bul.rabbitmq;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.nowon.bul.comoran.KomoranService;
import com.nowon.bul.domain.entity.Answer;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class Receiver {
	
	private final SimpMessagingTemplate smt;
	private final Komoran komoran;
	private final KomoranService komoranService;

	
	//RabbitTemplate Template 에서 전달한 메세지가 전송된다.
	public void receiveMessage(Question dto) {
		System.out.println(">>>>>>>"+dto);
		
		//의도분석 -> 응답메세지 작성
		Answer answer=null;
		System.out.println("코모란리턴>>>>>>>>"+komoranService.nlpAnalyze(dto.getContent()).getAnswer().getContent());
		if(dto.getContent().equals("인사말")) {
			answer=Answer.builder().content(dto.getName()+"님! 안녕하세요!").build();
		} else {
			//komoran에 질문내용을 보내서 의도분석
			
			KomoranResult analyzeResultList = komoran.analyze(dto.getContent());

		    System.out.println(analyzeResultList.getPlainText());
		    
		    List<Token> tokenList = analyzeResultList.getTokenList();
		    for (Token token : tokenList) {
		        System.out.format("(%2d, %2d) %s/%s\n", token.getBeginIndex(), token.getEndIndex(), token.getMorph(), token.getPos());
		    }
		    
		    answer=Answer.builder().content(dto.getName()+"님! 안녕하세요!").build();
		}
		
		smt.convertAndSend("/topic/question/"+dto.getKey(), answer);
	}
}
