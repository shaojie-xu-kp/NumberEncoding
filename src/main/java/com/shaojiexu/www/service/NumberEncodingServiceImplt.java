package com.shaojiexu.www.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shaojiexu.www.config.ConfigurationConstant;
import com.shaojiexu.www.config.NumerEncodingInitializer;
import com.shaojiexu.www.model.NumberObject;
import com.shaojiexu.www.util.NumberEncodingUtil;

@Service
public class NumberEncodingServiceImplt implements NumberEncodingService {
	
	private static final Logger logger = LoggerFactory.getLogger(NumberEncodingServiceImplt.class);


	@Override
	public List<String> lookUp(char digit) {

		List<String> words = new ArrayList<String>();
		List<Character> elements = NumerEncodingInitializer.numberAlpahbetMap.get(Character.getNumericValue(digit));
		for (Character element : elements) {
			words.addAll(NumerEncodingInitializer.dictionary.get(element) == null ? Collections
					.emptyList() : NumerEncodingInitializer.dictionary
					.get(element));
		}

		return words;
	}

	
	private List<String> encodeAsWhole(String number) {

		List<String> encodings = new ArrayList<>();
		char[] digits = number.toCharArray();
		List<String> words = this.lookUp(digits[0]);
		Map<String,String> wordsMap = new HashMap<String, String>();
		Iterator<String> itr = words.iterator();
		while(itr.hasNext()) {
			String word = itr.next();
			String cleanWord = NumberEncodingUtil.cleanDashAndDoubleQuote(word);
			if(cleanWord.length() != number.length()) {
				itr.remove();
			}else{
				wordsMap.put(cleanWord, word);
			}
		}
		
		if (words != null) {
			for(String word : wordsMap.keySet()) {
				Object[][] arrs = new Object[number.length()][];
				for (int i=0; i < number.length(); i++) {
					List<Character> elements = NumerEncodingInitializer.numberAlpahbetMap.get(Character.getNumericValue(digits[i]));
					arrs[i] = elements.toArray();
				}
				List<String> combos = new ArrayList<>();
				NumberEncodingUtil.recursivePermutation(combos, arrs, 0, "");
				for (String combi : combos) {
						if (combi.equals(word)) {
							encodings.add(wordsMap.get(word));
						}
				}

			}

		}
		
		return encodings;

	}
	
	
	@Override
	public List<String> encodeAsWhole(String number, boolean singleDigitPermited) {
		if(singleDigitPermited && number.length() == 1) {
			return Arrays.asList(number);
		}else{
			return this.encodeAsWhole(number);
		}
	}

	private List<List<String>> searchEncodings(String number, int startAt, boolean singleDigitPermited) {
		  List<List<String>> result = new LinkedList<>();
		  if(startAt == number.length()) {
		    result.add(new LinkedList<String>());
		    return result;
		  }
		  for(int endAt = startAt + 1; endAt <= number.length(); endAt++) {
		    List<String> words = this.encodeAsWhole(number.substring(startAt, endAt), singleDigitPermited);
		    if(words != null && words.size() > 0) {
		    	
		    	List<List<String>> encodings = null;
		    	if(words.size() == 1 && words.get(0).length() == 1){
		    		encodings = searchEncodings(number, endAt, false);
		    	}
		    	else{
		    		encodings = searchEncodings(number, endAt, true);
		    	}
		      for(String word: words) {
		        for(List<String> encoding: encodings) {
		          List<String> enc = new LinkedList<>(encoding);
		          enc.add(0, word);
		          result.add(enc);
		        }
		      }
		    }
		  }
		  return result;
		}
	
	public void searchEncodings(NumberObject number) {
		List<List<String>> encodings = searchEncodings(NumberEncodingUtil.numberString2Number(number.getNumber()), 0, true);
		this.postProcessing(number, encodings);
	}


	private void postProcessing(NumberObject numberObject, List<List<String>> encodings) {
		
		if(encodings == null || encodings.size() == 0) {
			logger.info(numberObject.getNumber() + " has no encoding.");
		}
		
		for(List<String> strs : encodings) {
			StringBuffer sbf = new StringBuffer();
			for(int i = 0; i < strs.size(); i++) {
				sbf.append(strs.get(i)).append(ConfigurationConstant.EMPTY_SPACE);
			}
			numberObject.addEncoding(sbf.toString().trim());
			logger.info(String.format("%s: %s", numberObject.getNumber(), sbf.toString().trim()));
		}
	}

}
