package es.rafaespillaque.ayd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

	private static Properties props;
	private static Proxy proxy;

	public static String read(InputStream is) {
		try {
			return new Scanner(is).useDelimiter("\\A").next();
		} catch (NoSuchElementException e) {
			return "";
		}
	}

	public static String getCurrentPath() {
		String path = Utils.class.getProtectionDomain().getCodeSource().getLocation().toString();
		path = path.replace("target/classes/", ""); // Para que funcione desde
													// Eclipse
		path = path.replace("file:/", "");
		path = path.replace("/", "\\");
		return path;
	}

	public static Properties getProperties() {
		if (props == null) {
			props = new Properties();
			File file = new File(Utils.getCurrentPath() + "\\config.properties");
			if (file.exists()) {
				try {
					props.load(new FileInputStream(file));
				} catch (FileNotFoundException e) {
					Logger.getGlobal().log(Level.WARNING,
							"Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
				} catch (IOException e) {
					Logger.getGlobal().log(Level.WARNING,
							"Excepción de tipo " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
				}
			}
		}
		return props;
	}

	public static String getProp(String key, String defaultValue) {
		return getProperties().getProperty(key, defaultValue);
	}

	public static Boolean getProp(String key, Boolean defaultValue) {
		return Boolean.valueOf(getProperties().getProperty(key, defaultValue.toString()));
	}

	public static Proxy getProxy() {
		if (proxy == null) {
			if (Utils.getProp("proxy.useProxy", false)) {
				String proxyHost = Utils.getProp("proxy.host", "");
				String port = Utils.getProp("proxy.port", "");
				final String username = Utils.getProp("proxy.user", "");
				final String password = Utils.getProp("proxy.password", "");
				Authenticator.setDefault(new Authenticator() {

					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password.toCharArray());
					}

				});
				Properties systemProperties = System.getProperties();
				systemProperties.setProperty("http.proxyHost", proxyHost);
				systemProperties.setProperty("http.proxyPort", port);
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.valueOf(port)));
			} else {
				proxy = Proxy.NO_PROXY;
			}
		}
		return proxy;
	}

}
