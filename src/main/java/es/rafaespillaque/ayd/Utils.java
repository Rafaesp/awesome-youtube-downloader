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
		path = path.replace("target/classes/", ""); //Para que funcione desde Eclipse
		path = path.replace("file:/", "");
		path = path.replace("/", "\\");
		return path;
	}
	
}
