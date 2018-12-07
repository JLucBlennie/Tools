package org.jlb.tools.resources.kvfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * Controle personnalisé pour le chargement de dictionnaires au format UTF8.
 * 
 * @author JLuc
 */
public class Utf8BundleControl extends Control {

	/**
	 * Constructeur.
	 */
	public Utf8BundleControl() {
		super();
	}

	@Override
	public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
		String bundleName = toBundleName(baseName, locale);
		ResourceBundle bundle = null;
		if ("java.class".equals(format)) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends ResourceBundle> bundleClass = (Class<? extends ResourceBundle>) loader.loadClass(bundleName);

				// If the class isn't a ResourceBundle subclass, throw a
				// ClassCastException.
				if (ResourceBundle.class.isAssignableFrom(bundleClass)) {
					bundle = bundleClass.newInstance();
				} else {
					throw new ClassCastException(bundleClass.getName() + " cannot be cast to ResourceBundle");
				}
			} catch (ClassNotFoundException e) {
				e.getMessage();
			}
		} else if ("java.properties".equals(format)) {
			final String resourceName = toResourceName(bundleName, "properties");
			final ClassLoader classLoader = loader;
			final boolean reloadFlag = reload;
			InputStream stream = null;
			try {
				stream = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
					@Override
					public InputStream run() throws IOException {
						InputStream is = null;
						if (reloadFlag) {
							URL url = classLoader.getResource(resourceName);
							if (url != null) {
								URLConnection connection = url.openConnection();
								if (connection != null) {
									// Disable caches to get fresh data for
									// reloading.
									connection.setUseCaches(false);
									is = connection.getInputStream();
								}
							}
						} else {
							is = classLoader.getResourceAsStream(resourceName);
						}
						return is;
					}
				});
			} catch (PrivilegedActionException e) {
				throw (IOException) e.getException();
			}
			if (stream != null) {
				try {
					bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
				} finally {
					stream.close();
				}
			}
		} else {
			throw new IllegalArgumentException("unknown format: " + format);
		}
		return bundle;
	}

}
