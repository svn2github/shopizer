package com.personalization.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class FileUtil {
	
	
	public static String readFileAsString(String fileName)
    throws java.io.IOException{
 
        
        InputStream is = null;
        Reader r = null;
        
        try {
			

        is = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
        
        InputStreamReader in =
        	new InputStreamReader(is
        			 );
        
        
        StringBuffer buffer = new StringBuffer();
    	r = new BufferedReader(in);
    	int ch;
    	while ((ch = r.read()) > -1) {
    		buffer.append((char)ch);
    	}
    	r.close();
    	return buffer.toString();
    	
		} finally {
			if(r!=null) {
				try {
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if(is!=null) {
				try {
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}


        //URL url = FileUtil.class.getResource(fileName);
        //String fullyQualifiedFilename = url.getFile();
        
/*        BufferedReader reader = new BufferedReader(
                new FileReader(fileName));

        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            fileData.append(buf, 0, numRead);
        }
        reader.close();
        return fileData.toString();*/
    }


}
