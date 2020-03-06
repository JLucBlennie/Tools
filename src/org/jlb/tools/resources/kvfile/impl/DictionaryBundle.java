package org.jlb.tools.resources.kvfile.impl;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Représente un dictionnaire.
 * 
 * @author JLuc
 */
public class DictionaryBundle extends GenericKeyValueFile {
	/**
	 * Constructeur.
	 */
	public DictionaryBundle() {
		this("");
	}

	/**
	 * Constructeur.
	 * 
	 * @param id
	 *            Nom de la resource
	 * 
	 */
	public DictionaryBundle(final String id) {
		super(id);
		reload();
	}

	@Override
	public final void reload() {
		String bundleName = getBundleName();
		if (bundleName != null && bundleName.length() > 0) {
			try {
				ResourceBundle.clearCache();
				loadBundle(bundleName, Locale.getDefault());
			} catch (MissingResourceException e) {
				System.err.println(e.getMessage());
				setException(e);
			} catch (RuntimeException e) {
				e.printStackTrace();
				setException(e);
			}
		}
		mIsLoaded = true;
		mLastUpdate = System.currentTimeMillis();
	}

}
