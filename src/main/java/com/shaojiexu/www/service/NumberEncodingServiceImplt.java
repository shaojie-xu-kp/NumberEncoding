package com.shaojiexu.www.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shaojiexu.www.config.NumerEncodingInitializer;

@Service
public class NumberEncodingServiceImplt implements NumberEncodingService {
	

	@Override
	public List<String> lookUp(char digit) {

		List<String> words = new ArrayList<String>();
		List<Character> elements = NumerEncodingInitializer.numberAlpahbetMap.get(Character.getNumericValue(digit));
		for (Character element : elements) {
			words.addAll(NumerEncodingInitializer.dictionary.get(element) == null ? Collections.emptyList() : NumerEncodingInitializer.dictionary.get(element));
		}

		return words;
	}

	@Override
	public List<String> encode(String number, int position, List<String> encodings) {
		
		char[] digits = number.toCharArray();
		List<String> words = this.lookUp(digits[position]);
		
		if( words!= null) {
			
			int wordLength = 0;
			for(int j = 1, i = position + j; i < digits.length; i++, j++) {
				Iterator<String> itr = words.iterator();
				List<Character> elements = NumerEncodingInitializer.numberAlpahbetMap.get(Character.getNumericValue(digits[i]));
				boolean matched = false;
				while(itr.hasNext()) {
					String word = itr.next();
					if(word.length() <= j) {
						matched = true;
						continue;
					}
					if(!elements.contains(word.charAt(j))) {
						itr.remove();
					}
				}
				
				if(matched) {
					return this.encode(number, i, words);
				}
				
				wordLength = j;
			}
			
			wordLength++;
			
			Iterator<String> itr = words.iterator();
			
			while(itr.hasNext()) {
				if(itr.next().length() > wordLength) {
					itr.remove();
				}
			}
			
			List<String> result = new ArrayList<>();
			
			if(encodings != null && encodings.size() > 0) {
				for(String encoding : encodings){
					for(String word : words){
						result.add(encoding + " " + word);
					}
				}
			}else{
				result = words;
			}
			
			return result;
			
		}else{
			return this.encode(number, position+1, Collections.emptyList());
		}
		
	}

}
