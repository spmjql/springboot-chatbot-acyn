package com.nowon.bul.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "intention")
@Entity
public class ChatBotIntention {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long no;
	private String name;
	
	@JoinColumn
	@ManyToOne
	private Answer answer;
		
	@JoinColumn
	@ManyToOne
	private ChatBotIntention upper;
}
