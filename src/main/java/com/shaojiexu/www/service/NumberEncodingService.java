package com.shaojiexu.www.service;

import java.util.List;

import com.shaojiexu.www.model.NumberObject;

public interface NumberEncodingService {
	
	public List<String> lookUp(char digit);
	
	public List<String> encodeAsWhole(String number);
	
	public List<List<String>> searchEncodings(NumberObject number);
	

}
