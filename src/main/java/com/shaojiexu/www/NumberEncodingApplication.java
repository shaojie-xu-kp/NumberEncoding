package com.shaojiexu.www;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.shaojiexu.www.config.ConfigurationConstant;
import com.shaojiexu.www.model.NumberObject;
import com.shaojiexu.www.service.NumberEncodingService;

@SpringBootApplication
public class NumberEncodingApplication {

    public static void main(String[] args) {
        SpringApplication.run(NumberEncodingApplication.class, args);
    }
    
}
@Component
class EncodeRunner implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(EncodeRunner.class);

	@Autowired
	NumberEncodingService encodingService;
	
	@Value("${phone.list}")
	private String phoneListPath;
	
	@Value("${encoding.file.path}")
	private String encodingFilePath;
	
	@Value("${encoding.file.name}")
	private String encodingFileName;


    public void run(String... args) {
    	
		logger.info(String.format("The path to the phone list : %s", phoneListPath));
		
		logger.info("*******Encoding Started************");
		
		try {
			
			FileWriter encodingFileWriter = new FileWriter(encodingFilePath + File.separator +encodingFileName);
			
			FileInputStream fis = new FileInputStream(phoneListPath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			
			while ((line = br.readLine()) != null) {
				NumberObject numberObject = new NumberObject(line);
				this.encodingService.searchEncodings(numberObject);
				for(String str : numberObject.getEncodings()) {
					encodingFileWriter.append(numberObject.getNumber()+ ": "+str);
					encodingFileWriter.append(ConfigurationConstant.NEW_LINE);
					logger.info(numberObject.getNumber()+ ": "+str);
				}
			}
			
			br.close();
			encodingFileWriter.flush();
			encodingFileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		logger.info("*******Encoding Ended************");
		logger.info(String.format("The ecodings file has been generated : %s%s%s", encodingFilePath, File.separator, encodingFileName));

    }

}