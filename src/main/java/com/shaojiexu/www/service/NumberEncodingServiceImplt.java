package com.shaojiexu.www.service;

import java.util.ArrayList;
import java.util.Collections;
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

}
