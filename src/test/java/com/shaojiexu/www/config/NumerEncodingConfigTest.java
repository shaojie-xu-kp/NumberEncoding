package com.shaojiexu.www.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.shaojiexu.www.NumberEncodingApplication;
import com.shaojiexu.www.util.NumberEncodingUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NumberEncodingApplication.class)
@ActiveProfiles("dev")
public class NumerEncodingConfigTest {
	
	@Autowired
	NumerEncodingInitializer config;
	
	@Test
	public void mappingFileLoadTest(){
		NumerEncodingInitializer.numberAlpahbetMap.entrySet().forEach(entry -> System.out.println(entry.getKey() + ":" + entry.getValue()));
	}
	
	@Test
	public void testSpecialCharMatch(){
		assertEquals("bo\"s".contains(ConfigurationConstant.DOUBLE_QUOTE), true);
		assertEquals("5624-82".contains(ConfigurationConstant.DASH), true);
		assertEquals("10/783".indexOf(ConfigurationConstant.SLASH),2);
		assertEquals("10/783".matches(ConfigurationConstant.NUMBER_MATCHE_REGEX), true);

	}
	
	@Test
	public void testNumberStringClean(){
		assertEquals(NumberEncodingUtil.cleanDashAndSlash("0721/608-4067"),"07216084067");
		assertEquals(NumberEncodingUtil.cleanDashAndSlash(null),"");
	}

}
