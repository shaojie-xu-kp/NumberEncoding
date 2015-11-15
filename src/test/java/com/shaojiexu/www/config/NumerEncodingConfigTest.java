package com.shaojiexu.www.config;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.shaojiexu.www.NumberEncodingApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NumberEncodingApplication.class)
public class NumerEncodingConfigTest {
	
	@Autowired
	NumerEncodingConfig config;
	
	@Test
	public void mappingFileLoadTest(){
		
		for(Map.Entry<Integer,List<Character>> entry : NumerEncodingConfig.numberAlpahbetMap.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		
		for(String word : NumerEncodingConfig.dictionary) {
			System.out.println(word);
		}
		
	}

}
