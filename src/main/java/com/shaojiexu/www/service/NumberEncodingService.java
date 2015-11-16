package com.shaojiexu.www.service;

import java.util.List;

public interface NumberEncodingService {
	
	public List<String> lookUp(char digit);
	
	public List<String> encode(String number, int position, List<String> encodings);

}
