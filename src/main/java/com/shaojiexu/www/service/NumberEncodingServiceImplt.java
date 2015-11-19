package com.shaojiexu.www.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Service;

import com.shaojiexu.www.config.ConfigurationConstant;
import com.shaojiexu.www.config.NumerEncodingInitializer;
import com.shaojiexu.www.model.NumberObject;
import com.shaojiexu.www.util.NumberEncodingUtil;

@Service
public class NumberEncodingServiceImplt implements NumberEncodingService {
	
//	private static final Logger logger = LoggerFactory.getLogger(NumberEncodingServiceImplt.class);

	@Override
	public List<String> lookUp(char digit) {

		List<String> words = new ArrayList<String>();
		NumerEncodingInitializer.numberAlpahbetMap.get(Character.getNumericValue(digit))
								.stream()
								.filter(element -> NumerEncodingInitializer.dictionary.get(element) != null)
								.forEach(element -> words.addAll(NumerEncodingInitializer.dictionary.get(element)));
		return words;
	}

	@Override
	public void searchEncodings(NumberObject number) {
		List<List<String>> encodings = searchEncodings(NumberEncodingUtil.numberString2Number(number.getNumber()), 0, true);
		this.postProcessing(number, encodings);
	}
	
	@Override
	public List<String> encodeAsWhole(String number, boolean singleDigitPermited) {
		if(singleDigitPermited && number.length() == 1) {
			return Arrays.asList(number);
		}else{
			return this.encodeAsWhole(number);
		}
	}

	private List<String> encodeAsWhole(String number) {
		
		char[] digits = number.toCharArray();
		List<String> words = this.lookUp(digits[0]);
		Map<String,String> wordsMap = new HashMap<String, String>();
		Iterator<String> itr = words.iterator();
		while(itr.hasNext()) {
			String word = itr.next();
			String cleanWord = NumberEncodingUtil.cleanDashAndDoubleQuote(word);
			if(cleanWord.length() != digits.length) {
				itr.remove();
			}else{
				wordsMap.put(cleanWord, word);
			}
		}
		
		Set<String> ws = new CopyOnWriteArraySet<>();
		if (wordsMap.keySet() != null) {
			ws.addAll(wordsMap.keySet());
			for(String word : ws) {
				for(int i = 1; i < word.length(); i++) {
					List<Character> elements = NumerEncodingInitializer.numberAlpahbetMap.get(Character.getNumericValue(digits[i]));
					if(!elements.contains(word.charAt(i))) {
						ws.remove(word);
					}
				}
			}
		}
		
		List<String> encodings = new ArrayList<>();
		
		for(String encodingKey : ws) {
			encodings.add(wordsMap.get(encodingKey));
		}
		
		return encodings;
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


	private void postProcessing(NumberObject numberObject,
			List<List<String>> encodings) {

		if (encodings != null && encodings.size() > 0) {

			List<String> allEncodings = new ArrayList<>();
			List<String> badEncodings = new ArrayList<>();

			for (List<String> strs : encodings) {
				StringBuffer sbf = new StringBuffer();
				for (int i = 0; i < strs.size(); i++) {
							sbf.append(strs.get(i)).append(ConfigurationConstant.EMPTY_SPACE);
				}
				allEncodings.add(sbf.toString().trim());
			}

			if (allEncodings.size() > 1) {
				for (String str : allEncodings) {
					String simpleStr = NumberEncodingUtil.cleanDashAndDoubleQuote(str);
					search: {
						if (simpleStr.matches(ConfigurationConstant.NUMBER_MATCHE_REGEX)) {
							for (int i = 0; i < simpleStr.length() - 1; i++) {
								if (Character.isDigit(simpleStr.charAt(i))) {
									for (String st : allEncodings) {
										if (!Character.isDigit(st.charAt(i))&& !badEncodings.contains(st)) {
											badEncodings.add(str);
											break search;
										}
									}
								}
							}
						}
					}
				}
			}

			for (String badEncoding : badEncodings) {
				allEncodings.remove(badEncoding);
			}

			numberObject.setEncodings(allEncodings);
		}
	}

}
