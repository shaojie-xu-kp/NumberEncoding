package com.shaojiexu.www.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.shaojiexu.www.NumberEncodingApplication;
import com.shaojiexu.www.util.NumberEncodingUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NumberEncodingApplication.class)
public class NumberEncodingServiceTest {

	@Autowired
	NumberEncodingService encodingService;
	
	@Test
	public void testLookUp(){
		
		String cleanNumber = NumberEncodingUtil.numberString2Number("5624-82");
		char[] digits = cleanNumber.toCharArray();
		for(Character digit: digits){
			System.out.println(digit + " : "+this.encodingService.lookUp(digit));
		}
	}
	
	@Test
	public void testEncoding(){
		
//		List<String> encodings = new ArrayList<>();
		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("56-2")));
		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("4824")));
		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("482")));
		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("10/7")));
		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("10")));
		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("783")));
		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("78")));
		
	}
	

}
