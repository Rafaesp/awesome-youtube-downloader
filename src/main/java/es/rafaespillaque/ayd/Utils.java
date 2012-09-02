package es.rafaespillaque.ayd;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Utils {
	
	public static String read(InputStream is){
		try {
	        return new Scanner(is).useDelimiter("\\A").next();
	    } catch (NoSuchElementException e) {
	        return "";
	    }
	}
	
}
