package org.jlb.tools.resources.kvfile.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.jlb.tools.resources.kvfile.impl.GenericKeyValueFile;

/**
 * Représente un fichier de propriétés. L'implémentation de référence est
 * {@link GenericKeyValueFile}.
 * 
 * @author JLuc
 */
public interface IKeyValueFile {

	/**
	 * @return Le nom du fichier de propriétés.
	 */
	String getId();

	/**
	 * @return Le chemin par défaut du bundle ou <code>null</code> si l'instance
	 *         de IKeyValueFile n'est pas chargé comme un un ResourceBundle.
	 */
	String getBundleName();

	/**
	 * Recherche une entrée par sa clé.
	 * 
	 * @param key
	 *            Nom de la clé.
	 * @return La IKeyValueFileEntry correspondante ou <code>null</code> si la
	 *         clé n'existe pas dans ce fichier de propriétés.
	 */
	IKeyValueFileEntry getEntry(String key);

	/**
	 * Retourne la liste des entrées disponibles.
	 * 
	 * @return la liste des entrées disponibles.
	 */
	List<IKeyValueFileEntry> getEntries();

	/**
	 * Retourne la valeur de la clef.
	 * 
	 * @param key
	 *            clef de la valeur
	 * @return la valeur
	 */
	Object getValue(IKeyValueFileEntry key);

	/**
	 * Retourne un fichier de valeurs par défaut avec les commentaires associés
	 * à chaque clé. Le fichier est retourné dans une chaine de caractères.
	 * 
	 * @return Un template de fichier rempli avec les valeurs par défaut.
	 */
	String getTemplateFileAsString();

	/**
	 * Retourne le fichier de valeurs avec les commentaires associés à chaque
	 * clé. Le fichier est retourné dans une chaine de caractères.
	 * 
	 * @return Unfichier rempli avec les valeurs chargées en mémoire.
	 */
	String getValueFileAsString();

	/**
	 * Utilise la dernière méthode de chargement utilisée pour charger le
	 * fichier ({@link #loadBundle(ResourceBundle)}, {@link #loadUrl(URL)}
	 * ...etc.) et recharge le fichier à partir du disque.
	 * 
	 * @throws IOException
	 *             En cas de problème de chargement.
	 */
	void reload() throws IOException;

	/**
	 * Charge les propriétés fournies dans l'instance de KeyValueFile.
	 * 
	 * @param properties
	 *            Les propriétés à charger.
	 * @throws NullPointerException
	 *             Si <code>properties</code> est <code>null</code>.
	 * @throws IOException
	 *             Si les clés du fichier ne correspondent pas à sa
	 *             spécification (ie. le fichier ne doit pas contenir de clés
	 *             inconnues ou manquer des clés obligatoires).
	 */
	void loadProperties(Properties properties) throws IOException;

	/**
	 * Charge le flux fourni en paramètre avec la méthode
	 * {@link Properties#load(InputStream)} et appelle ensuite
	 * {@link #loadProperties(Properties)}.
	 * 
	 * @param is
	 *            Le flux de données représentant un fichier
	 *            <code>.properties</code>.
	 * @throws NullPointerException
	 *             Si <code>is</code> est <code>null</code>.
	 * @throws IOException
	 *             Si la lecture du flux pose problème ou si les clés du fichier
	 *             ne correspondent pas à sa spécification (ie. le fichier ne
	 *             doit pas contenir de clés inconnues ou manquer des clés
	 *             obligatoires).
	 */
	void loadInputStream(InputStream is) throws IOException;

	/**
	 * Charge le fichier de clés/valeurs à partir d'une URL ouvert comme un flux
	 * grâce à la méthode {@link URL#openStream()}. Ensuite le chargement passe
	 * par {@link #loadInputStream(InputStream)}.
	 * 
	 * @param url
	 *            L'URL du fichier à charger.
	 * @throws NullPointerException
	 *             Si <code>url</code> est <code>null</code>.
	 * @throws IOException
	 *             Si la lecture du flux pose problème ou si les clés du fichier
	 *             ne correspondent pas à sa spécification (ie. le fichier ne
	 *             doit pas contenir de clés inconnues ou manquer des clés
	 *             obligatoires).
	 */
	void loadUrl(URL url) throws IOException;

	/**
	 * Charge les propriétés du bundle dans l'instance de KeyValueFile.
	 * 
	 * @param bundle
	 *            le bundle à charger.
	 * @throws NullPointerException
	 *             Si <code>bundle</code> est <code>null</code>.
	 * @throws IOException
	 *             Si la lecture du bundle pose problème ou si les clés du
	 *             fichier ne correspondent pas à sa spécification (ie. le
	 *             fichier ne doit pas contenir de clés inconnues ou manquer des
	 *             clés obligatoires).
	 */
	void loadBundle(ResourceBundle bundle) throws NullPointerException, IOException;

	/**
	 * Méthode de chargement privilégiée.
	 * <p>
	 * Charge le fichier de propriétés en prenant comme hypothèse que le
	 * paramètre 'bundleName' est un nom de ResourceBundle valide.
	 * 
	 * @param bundleName
	 *            nom du bundle, relatif au classpath système.
	 * @param locale
	 *            langue du bundle.
	 * @throws NullPointerException
	 *             Si <code>bundleName</code> est <code>null</code>.
	 * @throws MissingResourceException
	 *             Si le bundle correspondant est introuvable.
	 * @throws IOException
	 *             Si la lecture du bundle pose problème ou si les clés trouvées
	 *             ne correspondent pas à la spécification (ie. le fichier ne
	 *             doit pas contenir de clés inconnues ou manquer des clés
	 *             obligatoires).
	 */
	void loadBundle(String bundleName, Locale locale) throws MissingResourceException;

	/**
	 * Charge un fichier de propriétés à partir d'un chemin. Equivalent à
	 * {@link #loadInputStream(InputStream)} avec le FileInputStream
	 * correspondant au chemin fourni en paramèter.
	 * 
	 * @param path
	 *            Chemin vers le fichier <code>.properties</code>.
	 * @throws NullPointerException
	 *             Si <code>path</code> est <code>null</code>.
	 * @throws IOException
	 *             Si le chemin n'existe pas ou si la lecture du fichier pose
	 *             problème ou si les clés trouvées ne correspondent pas à la
	 *             spécification (ie. le fichier ne doit pas contenir de clés
	 *             inconnues ou manquer des clés obligatoires).
	 */
	void loadPath(String path) throws IOException;

	/**
	 * @return Le timestamp de la dernière mise à jour du dico.
	 */
	long lastUpdate();
}
