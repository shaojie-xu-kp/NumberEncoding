package com.shaojiexu.www.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shaojiexu.www.config.ConfigurationConstant;
import com.shaojiexu.www.config.NumerEncodingInitializer;
import com.shaojiexu.www.util.NumberEncodingUtil;

@Service
public class NumberEncodingServiceImplt implements NumberEncodingService {
	
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
			return this.encodeAsWhole(number);
		}
	}
	
	

	/**
	 * look up in the number alphabet map to find the mapping elements for each digit
	 */
	private List<String> lookUp(char digit) {
		
		List<String> words = new LinkedList<String>();
		NumerEncodingInitializer.numberAlpahbetMap.get(Character.getNumericValue(digit))
		.stream()
		.filter(element -> NumerEncodingInitializer.dictionary.get(element) != null)
		.forEach(element -> words.addAll(NumerEncodingInitializer.dictionary.get(element)));
		return words;
	}
	
	
	/**
	 * encode the number string as a whole word
	 * @param number
	 * @return
	 */
	private List<String> encodeAsWhole(String number) {
		
		//wordsMap which has clean word without umlaut and value with umlaut
		Map<String,String> wordsMap = new HashMap<String, String>();

		// filter out the word which has the same length of the input number and put them into wordsMap
		this.lookUp(number.charAt(0)).stream()
									 .filter(word -> NumberEncodingUtil.cleanDashAndDoubleQuote(word).length() == number.length())
									 .forEach(word -> wordsMap.put(NumberEncodingUtil.cleanDashAndDoubleQuote(word), word));
		
		/*
		 * for each position index of the number, get a set of possible mapping elements
		 * remove the word in the wordsMap if the char at position index of word is not part of the mapping elements
		 */
		for(int i = 1; i < number.length(); i++) {
			int index = i;
			List<Character> elements = NumerEncodingInitializer.numberAlpahbetMap.get(Character.getNumericValue(number.charAt(index)));
			wordsMap.keySet().removeIf(key -> !elements.contains(key.charAt(index)));
		}
		return new ArrayList<String>(wordsMap.values());
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
			
			this.removeInvalidSingleDigitedEncodings(allEncodings);
			
			return allEncodings;
		}else{
			return Collections.emptyList();
		}
		
	}
	
	/**
	 * for a list of all possible encodings, filter out the invalid single digit encoded encodings
	 * if the encoding has digit inside
	 * for the position of the digit of this encoding, check out the all the char of same position of all
	 * if find out that the char in that position of other encodings is not digit, mean from this position, other words in the 
	 * dictionary has been found and this encoding is invalid
	 * @param allEncodings
	 */
	private void removeInvalidSingleDigitedEncodings( List<String> allEncodings) {

		if (allEncodings.size() > 1) {
			List<String> badEncodings = new ArrayList<>();
			allEncodings.forEach(str -> {
				String simpleStr = NumberEncodingUtil.cleanDashAndDoubleQuoteAndEmptySpace(str);
				search: {
					if (simpleStr.matches(ConfigurationConstant.NUMBER_MATCHE_REGEX)) {
						for (int i = 0; i < simpleStr.length() - 1; i++) {
							if (Character.isDigit(simpleStr.charAt(i))) {
								for (String st : allEncodings) {
									if (!Character.isDigit(st.charAt(i)) && !badEncodings.contains(st)) {
										badEncodings.add(str);
										break search;
									}
								}
							}
						}
					}
				}
			});
			allEncodings.removeAll(badEncodings);
		}
	}
	
}
