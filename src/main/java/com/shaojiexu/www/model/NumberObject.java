package com.shaojiexu.www.model;

import java.util.ArrayList;
import java.util.List;

public class NumberObject {
	
	private String number;
	
	private List<String> encodings;
		
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
	
	public void addEncoding(String encoding){
		if(encodings == null) {
			encodings = new ArrayList<>();
		}
		encodings.add(encoding);
	}

	public String toString(){
		return String.format("%s: %s", this.number, this.encodings);
	}

}
