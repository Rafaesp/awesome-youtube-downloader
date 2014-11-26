package es.rafaespillaque.ayd;

import java.io.InputStream;
import java.net.URISyntaxException;
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
	
	public static String getCurrentPath(){
		String path = Utils.class.getProtectionDomain().getCodeSource().getLocation().toString();
		System.out.println(path);
		path = path.replaceAll("target/classes/", "").replaceAll("file:/", "").replaceAll("/", "\\");
		System.out.println(path);
		return path;
	}
	
}
