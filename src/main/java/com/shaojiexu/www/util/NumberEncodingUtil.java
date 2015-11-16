package com.shaojiexu.www.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import com.shaojiexu.www.config.ConfigurationConstant;

public class NumberEncodingUtil {
	
	/**
	 * 
	 * @param aFile
	 * @return numer of lines in the file
	 * @throws IOException
	 */
	public static int countLines(File aFile) throws IOException {
		
	    LineNumberReader reader = null;
	    try {
	        reader = new LineNumberReader(new FileReader(aFile));
	        while ((reader.readLine()) != null);
	        return reader.getLineNumber();
	    } catch (Exception ex) {
	        return -1;
	    } finally { 
	        if(reader != null) 
	            reader.close();
	    }
	}
	
	
	/**
	 * 
	 * @param number
	 * 		  the input number string which might contain slash and dash 
	 * @return
	 * 		  return a number string which contains only digits
	 */
	public static String cleanNumberString(String number) {
		
		if(number == null) 
			throw new IllegalArgumentException("input number string should not be null");
		
		return number.replace(ConfigurationConstant.DASH, ConfigurationConstant.EMPTY_SPACE)
					 .replace(ConfigurationConstant.SLASH, ConfigurationConstant.EMPTY_SPACE);
	}


}
