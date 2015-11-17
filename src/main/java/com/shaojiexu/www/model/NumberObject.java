package com.shaojiexu.www.model;

import java.util.List;
import java.util.Map;

public class NumberObject {
	
	private String number;
	
	private List<String> encodings;
	
	private Map<String, List<String>> wordMap;
	
	public NumberObject(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<String> getEncodings() {
		return encodings;
	}

	public void setEncodings(List<String> encodings) {
		this.encodings = encodings;
	}

	public Map<String, List<String>> getWordMap() {
		return wordMap;
	}

	public void setWordMap(Map<String, List<String>> wordMap) {
		this.wordMap = wordMap;
	}

	

}
