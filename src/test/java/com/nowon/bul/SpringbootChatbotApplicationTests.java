package com.nowon.bul;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nowon.bul.domain.entity.Answer;
import com.nowon.bul.domain.entity.AnswerRepository;
import com.nowon.bul.domain.entity.ChatBotIntention;
import com.nowon.bul.domain.entity.ChatBotIntentionRepository;
import com.nowon.bul.domain.entity.DeptEntity;
import com.nowon.bul.domain.entity.DeptRepository;
import com.nowon.bul.domain.entity.EmpEntity;
import com.nowon.bul.domain.entity.EmpRepository;

@SpringBootTest
class SpringbootChatbotApplicationTests {

	@Autowired
	DeptRepository deptRepository;
	//@Test
	void 부서등록() {
		//deptRepository.save(DeptEntity.builder().name("그린")	.build());
		deptRepository.save(DeptEntity.builder().name("코드블룸").upper(deptRepository.findByName("그린").orElseThrow()).build());
	}

	@Autowired
	EmpRepository empRepository;
	//@Test
	void 사원등록() {
		//empRepository.save(EmpEntity.builder().name("조재청").phone("1111").dept(deptRepository.findByName("그린").orElseThrow()).build());
		
		String[] names= {"고형철","조영진","한재훈","남원호","조영훈"};
		String[] phones= {"3001","3002","3003","3004","3005"};
		
		IntStream.rangeClosed(0, 5).forEach(i->{
			empRepository.save(EmpEntity.builder().name(names[i]).phone(phones[i]).dept(deptRepository.findByName("코드블룸").orElseThrow()).build());
		});
	}
	
	@Autowired
	ChatBotIntentionRepository chatBotIntentionRepository;
	@Autowired
	AnswerRepository answerRepository;
	//@Test
	void 의도등록() {
		chatBotIntentionRepository.save(ChatBotIntention.builder()
				.name("안녕")
				.answer(answerRepository.save(Answer.builder()
						.keyword("안녕")
						.content("안녕하세요<br> 저는 조청BOT입니다.")
						.build()))
				.build());
	}
	//@Test
	void 기타등록() {
		chatBotIntentionRepository.save(ChatBotIntention.builder()
				.name("기타")
				.answer(answerRepository.save(Answer.builder()
						.keyword("기타")
						.content("질문이 정확하지 않습니다.<br> 정확하게 입력해주세요")
						.build()))
				.build());
	}
	@Test
	void 전화번호() {
		chatBotIntentionRepository.save(ChatBotIntention.builder()
				.name("전화번호")
				.answer(answerRepository.save(Answer.builder()
						.keyword("전화번호")
						.content("전화번호 안내해드립니다.<br>")
						.build()))
				.build());
	}
	@Test
	void 전화번호2() {
		chatBotIntentionRepository.save(ChatBotIntention.builder()
				.name("전화")
				.answer(answerRepository.findByKeyword("전화번호").get())
				.build());
	}


}
