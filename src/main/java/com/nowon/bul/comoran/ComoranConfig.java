package com.nowon.bul.comoran;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;

@Configuration
public class ComoranConfig {

	@Bean
	Komoran komoran() {
		return new Komoran(DEFAULT_MODEL.FULL);
	}
}
