package com.shaojiexu.www.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.shaojiexu.www.NumberEncodingApplication;
import com.shaojiexu.www.model.NumberObject;
import com.shaojiexu.www.util.NumberEncodingUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NumberEncodingApplication.class)
public class NumberEncodingServiceTest {

	@Autowired
	NumberEncodingService encodingService;
		
	
	public void testEncoding(){
		assertTrue(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("56-2"),false).contains("mir"));
		assertTrue(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("4824"),false).contains("Torf"));
		assertTrue(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("482"),false).contains("Tor"));
		assertTrue(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("10/7"),false).contains("neu"));
	}
	
	@Test
	public void testEncodingSearch(){
		NumberObject numberObject = new NumberObject("4824");
		this.encodingService.searchEncodings(numberObject);
//		List<String> encodingsExpected = Arrays.asList("je Bo\" da", "je bo\"s 5", "neu o\"d 5");
//		assertThat(numberObject.getEncodings(), is(encodingsExpected));
		System.out.println(numberObject.getEncodings());
	}
	
}
