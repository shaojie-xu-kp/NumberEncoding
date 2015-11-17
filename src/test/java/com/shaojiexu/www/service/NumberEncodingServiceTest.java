package com.shaojiexu.www.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.shaojiexu.www.NumberEncodingApplication;
import com.shaojiexu.www.model.NumberObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NumberEncodingApplication.class)
public class NumberEncodingServiceTest {

	@Autowired
	NumberEncodingService encodingService;
		
	@Test
	public void testEncoding(){
		
//		List<String> encodings = new ArrayList<>();
//		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("56-2")));
//		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("4824")));
//		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("482")));
//		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("10/7")));
//		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("10")));
//		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("783")));
//		System.out.println(this.encodingService.encodeAsWhole(NumberEncodingUtil.numberString2Number("78")));
		
	}
	
	@Test
	public void testEncodingSearch(){
		
		Map<String, List<String>> bucketedWords = new HashMap<>();
		NumberObject numberObject = new NumberObject("562482");
		bucketedWords.put("562", Arrays.asList("mir", "Mix"));
		bucketedWords.put("482", Arrays.asList("Tor"));
//		bucketedWords.put("10", Arrays.asList("je"));
//		bucketedWords.put("107", Arrays.asList("neu"));
//		bucketedWords.put("83", Arrays.asList("o\"d"));
//		bucketedWords.put("78", Arrays.asList("Bo\""));
//		bucketedWords.put("35", Arrays.asList("da"));
//		bucketedWords.put("783", Arrays.asList("bo\"s"));
//		bucketedWords.put("5", Arrays.asList("5"));
//		bucketedWords.put("0", Arrays.asList("0"));
//		bucketedWords.put("4", Arrays.asList("4"));
//		bucketedWords.put("482", Arrays.asList("Tor"));
//		bucketedWords.put("4824", Arrays.asList("Torf", "fort"));

		numberObject.setWordMap(bucketedWords);
		
		System.out.println(this.encodingService.searchEncodings(numberObject));

	}
	

}
