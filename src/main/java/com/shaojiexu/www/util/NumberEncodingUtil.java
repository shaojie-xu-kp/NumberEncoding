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
	 * 		  return a Long which contains only digits
	 */
	public static String cleanDashAndSlash(String number) {
		
		if(number == null) 
			return ConfigurationConstant.EMPTY_PLACEHOLDER;
		
		return number.replace(ConfigurationConstant.DASH, ConfigurationConstant.EMPTY_PLACEHOLDER)
					 .replace(ConfigurationConstant.SLASH, ConfigurationConstant.EMPTY_PLACEHOLDER);
	}
	
	/**
	 * 
	 * @param word
	 * 		  the word in the dictionary might contain dash, double quote
	 * @return
	 */
	public static String cleanDashAndDoubleQuote(String word){
		
		if(word == null) 
			return ConfigurationConstant.EMPTY_PLACEHOLDER;

		return word.replace(ConfigurationConstant.DASH, ConfigurationConstant.EMPTY_PLACEHOLDER)
				   .replace(ConfigurationConstant.DOUBLE_QUOTE, ConfigurationConstant.EMPTY_PLACEHOLDER);
		
	}


	/**
	 * 
	 * @param encoding
	 * 		  the encoding might contain dash, double quote, and empty space
	 * @return
	 */
	public static String cleanDashAndDoubleQuoteAndEmptySpace(String encoding) {
		
		if(encoding == null) 
			return ConfigurationConstant.EMPTY_PLACEHOLDER;

		return encoding.replace(ConfigurationConstant.DASH, ConfigurationConstant.EMPTY_PLACEHOLDER)
				   .replace(ConfigurationConstant.DOUBLE_QUOTE, ConfigurationConstant.EMPTY_PLACEHOLDER)
				   .replace(ConfigurationConstant.EMPTY_SPACE, ConfigurationConstant.EMPTY_PLACEHOLDER);
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
		  Integer.parseInt(str);
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}




}
