package com.shaojiexu.www.config;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

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
	NumerEncodingInitializer config;
	
	@Test
	public void mappingFileLoadTest(){
		
		for(Map.Entry<Integer,List<Character>> entry : NumerEncodingInitializer.numberAlpahbetMap.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		
		for(Map.Entry<Character, List<String>> entry : NumerEncodingInitializer.dictionary.entrySet()) {
					System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		
	}
	
	@Test
	public void testSpecialCharMatch(){
		
		assertEquals("bo\"s".contains(ConfigurationConstant.DOUBLE_QUOTE), true);
		assertEquals("5624-82".contains(ConfigurationConstant.DASH), true);
		assertEquals("10/783".indexOf(ConfigurationConstant.SLASH),2);

	}

}
