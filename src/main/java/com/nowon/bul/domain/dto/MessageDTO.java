package com.nowon.bul.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MessageDTO {
	
	private String today;
	private String time;
	
	private AnswerDTO answer;

	public MessageDTO today(String today) {
		this.today=today;
		return this;		
	}
	public MessageDTO answer(AnswerDTO answer) {
		this.answer=answer;
		return this;		
	}
}
