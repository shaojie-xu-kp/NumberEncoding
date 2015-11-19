package com.shaojiexu.www.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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

	public static Map<Character, Queue<String>> dictionary = new HashMap<>();

	@Value("${number.alphabet.mapping}")
	private String mappingFilePath;

	@Value("${alphabet.dictionary}")
	private String dictionaryFilePath;

	@PostConstruct
	private void init() {
		this.loadMappingFile();
		this.loadDictionary();
	}

	/**
	 * load the dictionary into memory from the dictionary file where the path is configured in the property file
	 */
	private void loadDictionary() {
		
		logger.info("Start loading the dictionary file : " + dictionaryFilePath);
		int wordCount = 0;
		try {
			FileInputStream fis = new FileInputStream(dictionaryFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			while ((line = br.readLine()) != null) {
				Queue<String> words = dictionary.containsKey(line.charAt(0)) ? dictionary.get(line.charAt(0)) : new LinkedList<>();
				words.offer(line);
				wordCount++;
				dictionary.put(line.charAt(0), words);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.info(String.format("There are totally %d words loaded into dictionary", wordCount));

		logger.info("End loading the dictionary file.");
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
