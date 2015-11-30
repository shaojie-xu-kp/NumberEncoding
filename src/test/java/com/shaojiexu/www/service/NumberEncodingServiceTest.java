package com.shaojiexu.www.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

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
public class NumberEncodingServiceTest {

	@Autowired
	NumberEncodingService encodingService;
		
	@Test
	public void testEncoding(){
		assertTrue(this.encodingService.encodeAsWhole(NumberEncodingUtil.cleanDashAndSlash("56-2"),false).contains("mir"));
		assertTrue(this.encodingService.encodeAsWhole(NumberEncodingUtil.cleanDashAndSlash("4824"),false).contains("Torf"));
		assertTrue(this.encodingService.encodeAsWhole(NumberEncodingUtil.cleanDashAndSlash("482"),false).contains("Tor"));
		assertTrue(this.encodingService.encodeAsWhole(NumberEncodingUtil.cleanDashAndSlash("10/7"),false).contains("neu"));
	}
	
	@Test
	public void testEncodingSearch(){
		List<String> encodings = this.encodingService.encode("5624-82");
		List<String> encodingsExpected = Arrays.asList("mir Tor", "Mix Tor");
		assertThat(encodings, is(encodingsExpected));
	}
	
}
