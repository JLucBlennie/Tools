package org.jlb.tools.resources.kvfile.impl;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.jlb.tools.resources.kvfile.api.IKeyValueFileEntry;

/**
 * Représente un dictionnaire de configuration de l'application qui est
 * obligatoire. Dans le constructeur par défaut, le chargement du fichier est
 * réalisé par la méthode {@link #loadBundle(String, java.util.Locale)}.
 * 
 * @author JLuc
 */
public class MandatoryConfigurationBundle extends GenericKeyValueFile {

	/**
	 * Constructeur.
	 * 
	 * @throws MissingResourceException
	 *             Si le chargement du bundle échoue (fichier introuvable, clés
	 *             manquantes...etc.)
	 */
	public MandatoryConfigurationBundle() {
		super("");
		this.setId(getClass().getName());
		reload();
	}

	@Override
	public final void reload() {
		String bundleName = getBundleName();
		if (bundleName != null && bundleName.length() > 0) {
			try {
				ResourceBundle.clearCache();
				loadBundle(bundleName, Locale.getDefault());
				mIsLoaded = true;
				mLastUpdate = System.currentTimeMillis();
			} catch (RuntimeException e) {
				setException(e);
			}
		} else {
			// Rend le dico inutilisable pour la suite sans faire planter
			// l'initialisation de la classe
			setException(new NullPointerException("Missing parameter 'bundle' of annotation @KeyValueFile in " + getId()));
		}
	}

	@Override
	public final Object getValue(final IKeyValueFileEntry key) {
		if (!mIsLoaded) {
			String bundleName = getBundleName();
			if (bundleName != null && bundleName.length() > 0) {
				loadBundle(bundleName, Locale.getDefault());
				mIsLoaded = true;
			}
		}

		return super.getValue(key);
	}
}
