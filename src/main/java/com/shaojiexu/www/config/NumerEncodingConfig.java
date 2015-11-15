package com.shaojiexu.www.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
public class NumerEncodingConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(NumerEncodingConfig.class);

	
	public static Map<Integer,List<Character>> NumberAlpahbetMap = new HashMap<>();

	@Value("${number.alphabet.mapping}")
	private String mappingFile;
	
	private static final String PIPE_DELIMITER = "\\|";
	
	private static final String WHITE_SPACE_DELIMITER = "\\s+";
	
	
	@PostConstruct
	private void init(){
		logger.info("Start loading the mapping file.");
		try {
			
			FileInputStream fis = new FileInputStream(mappingFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			
			String line = null;
			int numberOfLines = NumberEncodingUtil.countLines(new File(mappingFile));
						
			for (int i = 0; (line = br.readLine()) != null && i < numberOfLines-1; i++) {
				// each block of a line between two pipes such as "J N Q"
				String[] blocks = line.split(PIPE_DELIMITER);
				for(int j = 0; j < blocks.length; j++) {
					List<Character> chars = new ArrayList<>();
					for(String block : blocks[j].split(WHITE_SPACE_DELIMITER)) {
						if(block.length() >= 1)
							chars.add(block.charAt(0));
					}
					
					if(NumberAlpahbetMap.get(j) != null) {
						chars.addAll(NumberAlpahbetMap.get(j));
					}
					NumberAlpahbetMap.put(j, chars);
				}
			}
		 
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.info("End loading the mapping file.");
	}
	
}
