package com.shaojiexu.www.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shaojiexu.www.config.ConfigurationConstant;
import com.shaojiexu.www.config.NumerEncodingInitializer;
import com.shaojiexu.www.util.NumberEncodingUtil;

@Service
public class NumberEncodingServiceImplt implements NumberEncodingService {
	
	@Value("${filter.flag}")
	private boolean filterFlag;
	
	/**
	 * entrance method to do encoding
	 */
	@Override
	public List<String> encode(String number) {
		List<List<String>> encodings = searchEncodings(NumberEncodingUtil.cleanDashAndSlash(number), 0, true);
		return this.postProcessing(encodings);
	}
	
	
	/**
	 * based on the permission of single digit encoding, find the encodings for whole number string
	 */
	@Override
	public List<String> encodeAsWhole(String number, boolean singleDigitPermited) {
		if(singleDigitPermited && number.length() == 1) {
			return Arrays.asList(number);
		}else{
			return NumerEncodingInitializer.decodedDictionary.get(number);
		}
	}
	
	
	/**
	 * recursively to search the encodings
	 * 
	 * @param number 
	 * 		  the input number string
	 * @param initPosition
	 * 		  the initial position of the string to do encoding
	 * @param singleDigitPermited
	 * 		  permission to do single digit encoding 
	 *        if the previous digit has been encoded as a single digit, permission false, else permission true
	 * @return
	 */
	private List<List<String>> searchEncodings(String number, int initPosition, boolean singleDigitPermited) {
		
		  List<List<String>> resultEncodings = new LinkedList<>();
		  
		  if(initPosition == number.length()) {
			  resultEncodings.add(new LinkedList<String>());
		    return resultEncodings;
		  }
		  
		  for(int endPosition = initPosition + 1; endPosition <= number.length(); endPosition++) {
		    List<String> words = this.encodeAsWhole(number.substring(initPosition, endPosition), singleDigitPermited);
		    if(words != null && words.size() > 0) {
		    	
		    	/* to check the if previously encoded words are single digit encode
		    	 * if yes, singleDigitPermited set to false for next recursion
		    	 * if no , singleDigitPermited set to true for next recursion
		    	 */
		    	boolean singleDigitPermitedFlag = !(words.size() == 1 && words.get(0).length() == 1);
		    	List<List<String>> encodings = searchEncodings(number, endPosition, singleDigitPermitedFlag);
		    	
		    	/* for each list of encoding found in this recursion
		    	 * push into the first position the encodings found in previous recursion
		    	 */
				words.forEach(word -> {
					encodings.forEach(encoding -> {
						LinkedList<String> enc = new LinkedList<>(encoding);
						enc.addFirst(word);
						resultEncodings.add(enc);
					});
				});
			}
		  }
		  return resultEncodings;
		}


	/**
	 * post processing for the encodings into a formatted list of string for a given number object
	 * filter out the invalid single digit encoded encodings
	 * populate encoding list of the number object
	 * @param numberObject
	 * @param encodings
	 */
	private List<String> postProcessing(List<List<String>> encodingsList) {

		if (encodingsList != null && encodingsList.size() > 0) {

			List<String> allEncodings = new ArrayList<>();

			StringBuffer sbf = new StringBuffer();
			encodingsList.forEach(strs -> {
				strs.forEach(str -> sbf.append(str).append(ConfigurationConstant.EMPTY_SPACE));
				allEncodings.add(sbf.toString().trim());
				sbf.setLength(0);
			});
			
			if(filterFlag){
				this.removeInvalidSingleDigitedEncoding(allEncodings);
			}
			
			return allEncodings;
		}else{
			return Collections.emptyList();
		}
		
	}
	
	/**
	 * for a list of all possible encodings, filter out the invalid single digit encoded encodings
	 * if the encoding has digit inside
	 * for the position of the digit of this encoding, check out the all the char of same position of all
	 * if find out that the char in that position of other encodings is not digit, and the string from 0 to position equals the current
	 * one, meaning that the current one is not valid
	 * @param allEncodings
	 */
	private void removeInvalidSingleDigitedEncoding(List<String> encodings){
		
		List<String> encs = new ArrayList<>(encodings);
		
		if(encodings != null && encodings.size() > 0) {
			Iterator<String> itr = encodings.iterator();
			while(itr.hasNext()) {
				String originalEncoding = itr.next();
				String cleanstr = NumberEncodingUtil.cleanDashAndDoubleQuote(originalEncoding);
				boolean removeFlag = false;
				search: {
					for(int i = 0; i < cleanstr.length(); i++) {
						if(Character.isDigit(cleanstr.charAt(i))) {
							for(String str : encs) {
								String cleanEncoding = NumberEncodingUtil.cleanDashAndDoubleQuote(str);
								if(cleanEncoding.length() > i && cleanstr.substring(0, i).equals(cleanEncoding.substring(0, i)) && !Character.isDigit(cleanEncoding.charAt(i))) {
									removeFlag = true;
									break search;
								}
							}
						}
					}
				}
				if(removeFlag){
					itr.remove();
					encs.remove(originalEncoding);
				}
			}
		}
	}
		
}
