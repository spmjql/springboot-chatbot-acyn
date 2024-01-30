package com.nowon.bul.comoran;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.nowon.bul.domain.entity.DeptRepository;
import com.nowon.bul.domain.entity.EmpRepository;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ComoranConfig {
	
	private String USER_DIC="dic.user";
	
	private final DeptRepository deptRepository;
	private final EmpRepository empRepository;

	@Bean
	Komoran komoran() throws Exception {
		//파일에 DB의 정보 세팅
		createDic();
		
		
		Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
		komoran.setUserDic(USER_DIC);
		return komoran;
	}

	private void createDic() throws Exception {
//		ClassPathResource cpr= new ClassPathResource("static/files/");
//		File file = new File(cpr.getPath(),USER_DIC);
		File file = new File(USER_DIC);
		try {if(!file.exists()) file.createNewFile();} catch (IOException e) {}
		
		Set<String> nnpset = new HashSet<>();
		////////////////////////////
		BufferedReader br = new BufferedReader(new FileReader(file));
		String data = null;
		while( (data=br.readLine())!=null ) {
			if( data.startsWith("#") ) continue;
			nnpset.add(data.split("\\t")[0]);
		}
		br.close();
		
		//부서명 등록
		deptRepository.findAll().forEach(e->{
			nnpset.add(e.getName());
		});;
		//사원명 등록
		empRepository.findAll().forEach(e->{
			nnpset.add(e.getName());
		});;
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			nnpset.forEach(name->{
				try {bw.write(name+"\tNNP\n");} catch (IOException e1) {}
			});
			bw.close();
		} catch (IOException e1) {}
	}
}
