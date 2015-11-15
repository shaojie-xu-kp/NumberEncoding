package com.shaojiexu.www.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NumerEncodingConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(NumerEncodingConfig.class);

	
	public static final Map<Integer,List<Character>> NumAlpahbetMap = new HashMap<>();

	@PostConstruct
	private void init(){
		logger.info("Start loading the mapping file.");
		logger.info("End loading the mapping file.");
	}
}
