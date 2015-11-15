package com.shaojiexu.www.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.shaojiexu.www.NumerEncodingApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NumerEncodingApplication.class)
public class NumerEncodingConfigTest {
	
	@Autowired
	NumerEncodingConfig config;
	
	@Test
	public void mappingFileLoadTest(){
		
	}

}
