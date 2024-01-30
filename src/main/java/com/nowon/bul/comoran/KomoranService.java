package com.nowon.bul.comoran;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nowon.bul.domain.dto.AnswerDTO;
import com.nowon.bul.domain.dto.MessageDTO;
import com.nowon.bul.domain.entity.Answer;
import com.nowon.bul.domain.entity.ChatBotIntention;
import com.nowon.bul.domain.entity.ChatBotIntentionRepository;

import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class KomoranService {
	
	
	private final Komoran komoran;
	
	private final ChatBotIntentionRepository intention;
	
	
	public MessageDTO nlpAnalyze(String message) {
		
		KomoranResult result=komoran.analyze(message);
		
		//문자에서 명사들만 추출한 목록 중복제거해서 set
		Set<String> nouns=result.getNouns().stream()
				.collect(Collectors.toSet());
		nouns.forEach((noun)->{
			System.out.println(">>>:"+ noun);
		});;//메세지에서 명사추출
		
		return analyzeToken(nouns);	
	}

	//입력된 목적어를 하나씩 파악하여 DB에서 검색하기위해 decisionTree()메서드로 전달
	private MessageDTO analyzeToken(Set<String> nouns) {
		
		LocalDateTime today=LocalDateTime.now();
		//DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
		DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern("a H:mm");
		MessageDTO messageDTO=MessageDTO.builder()
								.time(today.format(timeFormatter))//시간세팅
								.build();
		
		//1차의도가 존재하는지 파악
		for(String token:nouns) {
			
			Optional<ChatBotIntention> result=decisionTree(token, null);
			if(result.isEmpty())continue;//존재하지 않으면 다음토큰 검색
			
			//1차 토근확인시 실행
			System.out.println(">>>>1차:"+token);
			//1차목록 복사
			Set<String> next=nouns.stream().collect(Collectors.toSet());
			//목록에서 1차토큰 제거
			next.remove(token);
			
			//2차분석 메서드
			AnswerDTO answer=analyzeToken(next, result).toAnswerDTO();
			
			//전화인경우 전화,전화번호 번호탐색
			if(token.contains("전화")) {
				//PhoneInfo phone=analyzeTokenIsPhone(next);
				//answer.phone(phone);//전화인경우에만 전화 데이터 
				
			}else if(token.contains("안녕")){
				DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
				messageDTO.today(today.format(dateFormatter));//처음 접속할때만 날짜표기
			}
			messageDTO.answer(answer);//토근에대한 응답정보
			
			
			
			return messageDTO;
		}
		//분석 명사들이 등록한 의도와 일치하는게 존재하지 않을경우 null
		AnswerDTO answer=decisionTree("기타", null).get().getAnswer().toAnswerDTO();
		messageDTO.answer(answer);//토근에대한 응답정보
		return messageDTO;
	}

	/*
	@Autowired
	MemberEntityRepository member;
	//전화문의인경우 DB에서 사원을 을 찾아서 처리
	private PhoneInfo analyzeTokenIsPhone(Set<String> next) {
		for(String name : next) {
			Optional<MemberEntity> m=member.findByName(name);
			if(m.isEmpty())continue;
			//존재하면
			String deptName=m.get().getDept().getDname();
			String phone=m.get().getPhone();
			String memberName=m.get().getName();
			return PhoneInfo.builder()
			.deptName(deptName)
			.phone(phone)
			.memberName(memberName)
			.build();

		}
		return null;
	}
	*/

	//1차의도가 존재하면
	//하위의도가 존재하는지 파악
	private Answer analyzeToken(Set<String> next, Optional<ChatBotIntention> upper) {
		for(String token : next) {
			// 1차의도를 부모로하는 토큰이 존재하는지 파악
			Optional<ChatBotIntention> result=decisionTree(token, upper.get());
			if(result.isEmpty())continue;
			System.out.println(">>>>2차:"+token);
			return result.get().getAnswer();//1차-2차 존재하는경우 답변
		}
		return upper.get().getAnswer();//1차만 존재하는 답변
	}

	
	//의도가 존재하는지 DB에서 파악
	private Optional<ChatBotIntention> decisionTree(String token, ChatBotIntention upper) {
		return intention.findByNameAndUpper(token, upper); 
	}


}
