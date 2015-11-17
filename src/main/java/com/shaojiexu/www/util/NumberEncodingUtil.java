package com.shaojiexu.www.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;

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
	public static String numberString2Number(String number) {
		
		if(number == null) 
			throw new IllegalArgumentException("input number string should not be null");
		
		return number.replace(ConfigurationConstant.DASH, ConfigurationConstant.EMPTY_SPACE)
					 .replace(ConfigurationConstant.SLASH, ConfigurationConstant.EMPTY_SPACE);
	}
	
	/**
	 * 
	 * @param combos
	 * @param arrs
	 * @param position
	 * @param s
	 * @return
	 */
	public static List<String> recursivePermutation (List<String> combos, Object[][] arrs, int position, String s) {
        if (position == arrs.length) {
        	combos.add(s);
        } else {
            for (Object o : arrs[position]) {
            	recursivePermutation(combos, arrs, position + 1, s + o);
            }
        }
        return combos;
    }
	
	public static String cleanDashAndDoubleQuote(String word){
		
		if(word == null) 
			throw new IllegalArgumentException("input word string should not be null");

		return word.replace(ConfigurationConstant.DASH, ConfigurationConstant.EMPTY_SPACE)
				   .replace(ConfigurationConstant.DOUBLE_QUOTE, ConfigurationConstant.EMPTY_SPACE);
		
	}



}
