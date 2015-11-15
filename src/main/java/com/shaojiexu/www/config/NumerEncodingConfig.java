package com.shaojiexu.www.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class NumerEncodingConfig {
	
	public static final Map<Integer,List<Character>> NumAlpahbetMap = new HashMap<>();

	@PostConstruct
	private void init(){
		
	}
}
