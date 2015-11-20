package com.shaojiexu.www.service;

import java.util.List;

public interface NumberEncodingService {
	
	public List<String> encodeAsWhole(String number, boolean singleDigitPermited);
	
	public List<String> encode(String number);
	
}
