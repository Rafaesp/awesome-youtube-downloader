package es.rafaespillaque.ayd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;

public class Utils {
	
	private static Properties props;
	
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
	
	public static Properties getProperties(){
		if(props == null){
			props = new Properties();
			File file = new File(Utils.getCurrentPath() + "\\config.properties");
			if(file.exists()){
				try {
					props.load(new FileInputStream(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return props;
	}
	
	public static String getProp(String key, String defaultValue){
		return getProperties().getProperty(key, defaultValue);
	}
	
	public static Boolean getProp(String key, Boolean defaultValue){
		return Boolean.valueOf(getProperties().getProperty(key, defaultValue.toString()));
	}
	
}
