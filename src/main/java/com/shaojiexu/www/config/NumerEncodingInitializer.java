package com.shaojiexu.www.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.shaojiexu.www.util.NumberEncodingUtil;

@Component
public class NumerEncodingInitializer {

	private static final Logger logger = LoggerFactory.getLogger(NumerEncodingInitializer.class);

	public static Map<Integer, List<Character>> numberAlpahbetMap = new HashMap<>();
	
	public static Map<Character, Character> AlpahbetNumberMap = new HashMap<>();
	
	public static Map<String,List<String>> decodedDictionary = new HashMap<>();

	@Value("${number.alphabet.mapping}")
	private String mappingFilePath;

	@Value("${alphabet.dictionary}")
	private String dictionaryFilePath;

	@PostConstruct
	private void init() {
		this.loadMappingFile();
		this.loadAlphaetNumberMap();
		this.decodeDictionary();
	}

	private void loadAlphaetNumberMap() {
		
		logger.info("Start loading Alphaet Number mapping file.");
		
		numberAlpahbetMap.entrySet().forEach(entry -> {
			final int  key = entry.getKey();
			entry.getValue().forEach(value -> {
				AlpahbetNumberMap.put(value, Integer.toString(key).charAt(0));
			});
		});
		
		logger.info("End loading Alphaet Number mapping file.");
	}

	private void decodeDictionary() {
		
		logger.info("Start decoding the dictionary file : " + dictionaryFilePath);
		int wordCount = 0;
		try {
			FileInputStream fis = new FileInputStream(dictionaryFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			StringBuffer sbf = new StringBuffer("");
			while ((line = br.readLine()) != null) {
				String cleanWord = NumberEncodingUtil.cleanDashAndDoubleQuote(line);
				for(int i = 0; i< cleanWord.length(); i++){
					sbf.append(AlpahbetNumberMap.get(cleanWord.charAt(i)));
				}
				String key = sbf.toString();
				if(decodedDictionary.containsKey(key)) {
					List<String> words = new ArrayList<>(decodedDictionary.get(key));
					words.add(line);
					decodedDictionary.put(key, words);
				}else{
					decodedDictionary.put(key, Arrays.asList(line));
				}
				wordCount++;
				sbf.setLength(0);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.info(String.format("There are totally %d words loaded into dictionary", wordCount));

		logger.info("End decoding the dictionary file.");

	}

	/**
	 * load the mapping into memory from the file which the path is configured in the property file
	 */
	private void loadMappingFile() {

		logger.info("Start loading the mapping file.");
		try {

			FileInputStream fis = new FileInputStream(mappingFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line = null;
			int numberOfLines = NumberEncodingUtil.countLines(new File(mappingFilePath));

			for (int i = 0; (line = br.readLine()) != null && i < numberOfLines - 1; i++) {
				// each block of a line between two pipes such as "J N Q"
				String[] blocks = line.split(ConfigurationConstant.PIPE_DELIMITER);
				for (int j = 0; j < blocks.length; j++) {
					List<Character> chars = new ArrayList<>();
					for (String block : blocks[j].split(ConfigurationConstant.WHITE_SPACE_DELIMITER)) {
						if (block.length() >= 1)
							chars.add(block.charAt(0));
					}

					if (numberAlpahbetMap.get(j) != null) {
						chars.addAll(numberAlpahbetMap.get(j));
					}
					numberAlpahbetMap.put(j, chars);
				}
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("End loading the mapping file.");

	}

}
