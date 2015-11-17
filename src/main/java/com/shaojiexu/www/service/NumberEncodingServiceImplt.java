package com.shaojiexu.www.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shaojiexu.www.config.NumerEncodingInitializer;
import com.shaojiexu.www.model.NumberObject;
import com.shaojiexu.www.util.NumberEncodingUtil;

@Service
public class NumberEncodingServiceImplt implements NumberEncodingService {
	


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

	@Override
	public List<String> encodeAsWhole(String number) {

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

	
	private List<List<String>> searchEncodings(String number, int startAt) {
		  LinkedList<List<String>> result = new LinkedList<>();
		  if(startAt == number.length()) {
		    result.add(new LinkedList<String>());
		    return result;
		  }
		  for(int endAt = startAt + 1; endAt <= number.length(); endAt++) {
		    List<String> words = this.encodeAsWhole(number.substring(startAt, endAt));
		    if(words != null) {
		      List<List<String>> encodings = searchEncodings(number, endAt);
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
	
	public List<List<String>> searchEncodings(NumberObject number) {
		return searchEncodings(NumberEncodingUtil.numberString2Number(number.getNumber()), 0);
	}

}
