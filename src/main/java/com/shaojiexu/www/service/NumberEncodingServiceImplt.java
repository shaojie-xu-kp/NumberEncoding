package com.shaojiexu.www.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

		List<String> words = new LinkedList<String>();
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

	/**
	 * encode the word as a whole one
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
	
	private List<List<String>> searchEncodings(String number, int initPosition, boolean singleDigitPermited) {
		  List<List<String>> resultEncodings = new LinkedList<>();
		  if(initPosition == number.length()) {
			  resultEncodings.add(new LinkedList<String>());
		    return resultEncodings;
		  }
		  
		  for(int endPosition = initPosition + 1; endPosition <= number.length(); endPosition++) {
		    List<String> words = this.encodeAsWhole(number.substring(initPosition, endPosition), singleDigitPermited);
		    List<List<String>> encodings = null;
		    if(words != null && words.size() > 0) {
		    	
		    	/* to check the if previously encoded words are single digit encode
		    	 * if yes, singleDigitPermited set to false for next recursion
		    	 * if no , singleDigitPermited set to true for next recursion
		    	 */
		    	boolean singleDigitPermitedFlag = !(words.size() == 1 && words.get(0).length() == 1);
		    	encodings = searchEncodings(number, endPosition, singleDigitPermitedFlag);
		    	
				for (String word : words) {
					for (List<String> encoding : encodings) {
						LinkedList<String> enc = new LinkedList<>(encoding);
						enc.addFirst(word);
						resultEncodings.add(enc);
					}
				}
		    }
		  }
		  return resultEncodings;
		}


	private void postProcessing(NumberObject numberObject, List<List<String>> encodings) {

		if (encodings != null && encodings.size() > 0) {

			List<String> allEncodings = new ArrayList<>();
			List<String> badEncodings = new ArrayList<>();

			StringBuffer sbf = new StringBuffer();
			encodings.forEach(strs -> {
				strs.forEach(str -> sbf.append(str).append(ConfigurationConstant.EMPTY_SPACE));
				allEncodings.add(sbf.toString().trim());
				sbf.setLength(0);
			});
			
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
