package org.jlb.tools.logging;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jlb.tools.resources.Tools;

/**
 * Trace.
 * <p>
 * Title: Trace
 * </p>
 * Cette classe permet de gérer un système de traces. Elle permet d'afficher
 * dans la console des traces suivant le niveau d'affichage en cours. Il existe
 * 4 niveaux standard de trace :
 * <ul>
 * <li>les erreurs, Elles sont tracées lors de l'appel aux méthodes nommées "
 * Error() ".
 * <li>les warnings, Elles sont tracées lors de l'appel aux méthodes nommées "
 * Warning() ".
 * <li>les informations. Elles sont tracées lors de l'appel aux méthodes nommées
 * " Info() ".
 * <li>les informations de débug. Elles sont tracées lors de l'appel aux
 * méthodes nommées "traceDebug()".
 * </ul>
 */
public final class LogTracer {
	/** Group. */
	protected static final String LOG_GROUP = Tools.getText(DicoLoggingConfig.INSTANCE, DicoLoggingConfig.APPLI_NAME);

	/** Default logger. */
	protected static final Logger LOGGER = Logger.getLogger(LOG_GROUP);

	static {
		initLogConfiguration(Tools.getText(DicoLoggingConfig.INSTANCE, DicoLoggingConfig.LOG4J_FILE));
	}

	/**
	 * Constructeur par defaut.
	 */
	private LogTracer() {

	}

	/**
	 * Init de la config.
	 * 
	 * @param resourceFilePath
	 *            chemin du fichier de configuration
	 */
	public static void initLogConfiguration(final String resourceFilePath) {
		URL logConfigurationURL = ClassLoader.getSystemResource(resourceFilePath);
		if (logConfigurationURL == null) {
			LOGGER.warn("Log configuration file '" + resourceFilePath + "' not found. Running into default log mode");
		} else {
			PropertyConfigurator.configure(logConfigurationURL);
		}
	}

	/**
	 * Retourne le logger pour le groupe de trace demande.
	 * 
	 * @return le logger pour ce groupe de trace.
	 */
	public static Logger getLogger() {
		return LOGGER;
	}
}
