package com.nowon.bul.rabbitmq;

import lombok.Data;

@Data
public class Question {

	private long key;
	private String name;
	private String content;
}
