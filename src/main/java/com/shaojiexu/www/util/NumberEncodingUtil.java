package com.shaojiexu.www.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

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


}
