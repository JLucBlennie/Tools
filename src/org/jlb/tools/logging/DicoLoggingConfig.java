package org.jlb.tools.logging;

import org.jlb.tools.resources.kvfile.api.annotation.Comments;
import org.jlb.tools.resources.kvfile.api.annotation.KeyValueFile;
import org.jlb.tools.resources.kvfile.impl.MandatoryConfigurationBundle;

/**
 * Classe decrivant le fichier de configuration de l'application.
 * 
 * @author JLuc
 *
 */
@KeyValueFile(comments = "Fichier loggingConfig.properties", commentsDelimiter = "#", bundle = "resources/loggingConfig")
public final class DicoLoggingConfig extends MandatoryConfigurationBundle {

	/**
	 * Clef du nom de l'application.
	 */
	@Comments("Nom de l'application")
	public static final E APPLI_NAME = new E("appli.name", "logging", false);

	/**
	 * Clef du fichier de configuration log4j.
	 */
	@Comments("Auteur de l'application.")
	public static final E LOG4J_FILE = new E("logging.filename", "resources/log4j", false);

	/**
	 * Nom du dictionnaire.
	 */
	// ATTENTION : toujours déclarer cette variable en dernier car le
	// constructeur de GenericKeyValueFile a besoin que les constantes soient
	// déclarées.
	public static final DicoLoggingConfig INSTANCE = new DicoLoggingConfig();

	/**
	 * Constructeur par defaut.
	 */
	private DicoLoggingConfig() {

	}

	/**
	 * Generation du fichier par defaut.
	 * 
	 * @param args
	 *            Arguments de la ligne de commande
	 */
	public static void main(final String[] args) {

		// Dump d'un fichier de valeurs par défaut
		System.out.println(DicoLoggingConfig.INSTANCE.getTemplateFileAsString());
	}

}
