package org.jlb.tools.resources.kvfile.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jlb.tools.logging.LogTracer;
import org.jlb.tools.resources.Tools;
import org.jlb.tools.resources.kvfile.Utf8BundleControl;
import org.jlb.tools.resources.kvfile.api.IKeyValueFile;
import org.jlb.tools.resources.kvfile.api.IKeyValueFileEntry;
import org.jlb.tools.resources.kvfile.api.annotation.Comments;
import org.jlb.tools.resources.kvfile.api.annotation.KeyValueFile;

import com.sun.javafx.fxml.PropertyNotFoundException;

/**
 * Implémentation générique d'un fichier de paires clé/valeur. Le constructeur
 * charge les valeurs par défaut par introspection des champs "static final
 * IKeyValueFileEntry".
 * <p>
 * Exemple de déclaration d'une entrée de fichier :
 * 
 * <pre>
 * &#064;Comments(&quot;Liste des serveurs&quot;)
 * public static final IKeyValueFileEntry LIST_SERVER_ADRESS = new GenericKeyValueFileEntry(&quot;LIST_SERVER_ADRESS&quot;, &quot;127.0.0.1&quot;, true);
 * </pre>
 * 
 * Pour la concision, vous pouvez utiliser l'alias de classe "E" à la place de
 * GenericKeyValueFileEntry :
 * 
 * <pre>
 * &#064;Comments(&quot;Liste des serveurs&quot;)
 * public static final E LIST_SERVER_ADRESS = new E(&quot;LIST_SERVER_ADRESS&quot;, &quot;127.0.0.1&quot;, true);
 * </pre>
 */
public class GenericKeyValueFile implements IKeyValueFile {
	/** Methode de chargement URL. */
	private static final String LOADING_METHOD_URL = "loadUrl";
	/** Methode de chargement Bundle. */
	private static final String LOADING_METHOD_BUNDLE = "loadBundle";

	/** Line Seperator. */
	private static final String LS = System.getProperty("line.separator");
	/** Line Seperator 2x. */
	private static final String LS2 = LS + LS;
	/** New line with one slash. */
	private static final String NEW_LINE_SLASH = "\\n";
	/** New line with double slash. */
	private static final String NEW_LINE_DOUBLE_SLASH = "\\\\n";

	/** Espace. */
	private static final String ESPACE = " ";
	/** Egal. */
	private static final String EGAL = "=";

	/** Commentaire sur la clef. */
	private static final String EN_OPT_KEY_ADVERT = "Please uncomment the following line and change its value.";
	/** Commentaire sur le fichier de template. */
	private static final String EN_TEMPLATE_FILE_ADVERT = "This is a template file. Please edit default values before using it.";
	/** Commentaire sur Mandatory. */
	private static final String EN_MANDATORY = "Required";
	/** Commentaire sur Valeur par defaut. */
	private static final String EN_DEFAULT_VALUE = "Default value";
	/** Commentaire sur Valeurs possibles. */
	private static final String EN_POSSIBLE_VALUES = "Possible values";
	/** Commentaire sur Clefs obligatoires manquantes. */
	private static final String EN_MISSING_MANDATORY_KEYS = "Missing mandatory keys: ";
	/** Commentaire sur valeur en dehors des bornes. */
	private static final String EN_VALUE_OUT_OF_BOUNDS = "Value out of bounds or not in accepted value set: {0} = {1} (expected: {2})";

	/** Watchable files. */
	private static Map<String, Collection<Path>> mWatchableFiles;
	/** KV files. */
	private static Map<String, IKeyValueFile> mKVFiles;
	/** Watch Service. */
	private static WatchService mWatcher;

	/** Etat du chargement. */
	protected boolean mIsLoaded;
	/** Derniere mise a jour. */
	protected long mLastUpdate;
	/** Exception du chargement. */
	private Throwable mLoadException;
	/** Nom de la resource. */
	private String mId;
	/** Les proprietes. */
	private final Properties mValues;
	/** Les entrees. */
	private final List<IKeyValueFileEntry> mEntries;

	// Pour se rappeler quelle a été la dernière façon dont a été chargé le dico
	// (pour le reload())
	/** Methode de la derniere mise a jour. */
	private String mLastLoadingMethod;
	/** Arguments de la methode de la derniere mise a jour. */
	private Object mLastLoadingMethodArg;

	static {
		mWatchableFiles = new HashMap<>();
		mKVFiles = new HashMap<>();
	}

	/**
	 * Constructeur.
	 * 
	 * @param id
	 *            Nom de la resource
	 */
	public GenericKeyValueFile(final String id) {
		mId = id;
		mEntries = new ArrayList<IKeyValueFileEntry>();
		mValues = new Properties();
		try {
			Field[] fields = getClass().getFields();
			for (Field field : fields) {
				int mod = field.getModifiers();
				if (Modifier.isStatic(mod) /* && Modifier.isFinal(mod) */) {
					Object fieldValue = field.get(null);
					if (IKeyValueFileEntry.class.isInstance(fieldValue)) {
						IKeyValueFileEntry entry = (IKeyValueFileEntry) fieldValue;
						mEntries.add(entry);
						mValues.put(entry.getKey(), entry.getDefaultValue());
					}
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			setException(e);
		}
	}

	/**
	 * Demande l'observation d'un fichier.
	 * 
	 * @param kvFile
	 *            Fichier clef / valeur
	 */
	private static void watch(final GenericKeyValueFile kvFile) {
		KeyValueFile specs = kvFile.getClass().getAnnotation(KeyValueFile.class);
		if (specs != null && specs.autoReload()) {
			File dicoFile = null;
			String bundle = specs.bundle();
			if (bundle != null) {
				URL resource = kvFile.getClass().getResource("/" + bundle + ".properties");
				if (resource != null) {
					dicoFile = new File(resource.getFile());
				}
			}

			if (dicoFile != null && dicoFile.exists() && dicoFile.isFile() && dicoFile.getParentFile() != null) {
				try {
					Path dirPath = dicoFile.getParentFile().toPath().normalize();
					String dir = dirPath.toFile().getAbsolutePath();
					if (!mWatchableFiles.containsKey(dir)) {
						dirPath.register(mWatcher, StandardWatchEventKinds.ENTRY_MODIFY);
						// System.out.println("[DIRECTORY-WATCH] Bundle '" +
						// bundle + "' --> Scrutation du répertoire '" + dirPath
						// + "'");
					}
					Tools.putValueInMapOfCollection(mWatchableFiles, dir, dicoFile.toPath().normalize());
					mKVFiles.put(dicoFile.getName(), kvFile);
				} catch (IOException e) {
					LogTracer.getLogger().error("Impossible de scruter le dictionnaire " + bundle, e);
				}
			}
		}
	}

	/**
	 * Permet de conserver les problèmes de chargement car il n'est pas opportun
	 * de lever une exception dans le constructeur. En effet, les erreurs
	 * d'instanciation des singletons de dictionnaires sont affichées dans un
	 * JDialog bloquant, ce qui peut être gênant pour les applications sans IHM.
	 * En cas d'exception dans un constructeur de dictionnaire, mieux vaut donc
	 * la catcher et la stocker pour la lancer plus tard à l'appel de
	 * {@link #getValue(IKeyValueFileEntry)} par exemple.
	 * 
	 * @param e
	 *            Exception
	 */
	protected final void setException(final Throwable e) {
		mLoadException = e;
	}

	/**
	 * Retourne la dernière exception qui est apparue au chargement du fichier.
	 * 
	 * @return la dernière exception qui est apparue au chargement du fichier.
	 */
	protected final Throwable getException() {
		return mLoadException;
	}

	@Override
	public final String getId() {
		return mId;
	}

	/**
	 * Met a jour l'id.
	 * 
	 * @param id
	 *            Nouvel id
	 */
	public final void setId(final String id) {
		mId = id;
	}

	@Override
	public final String getBundleName() {
		String result = "";
		KeyValueFile specs = getClass().getAnnotation(KeyValueFile.class);
		if (specs != null) {
			result = specs.bundle();
		}
		return result;
	}

	@Override
	public final IKeyValueFileEntry getEntry(final String key) {
		IKeyValueFileEntry result = null;
		for (Iterator<IKeyValueFileEntry> it = mEntries.iterator(); it.hasNext();) {
			IKeyValueFileEntry entry = it.next();
			if (entry.getKey().equals(key)) {
				result = entry;
			}
		}
		return result;
	}

	@Override
	public final List<IKeyValueFileEntry> getEntries() {
		return mEntries;
	}

	@Override
	public Object getValue(final IKeyValueFileEntry key) {
		checkLoaded();
		Object value = mValues.get(key.getKey());
		if (value instanceof String) {
			value = Tools.replacePlaceholders((String) value, this);
		}
		return value;
	}

	/**
	 * Retourne la valeur.
	 * 
	 * @param key
	 *            Clef
	 * @param defaultValue
	 *            Valeur par defaut
	 * @return la valeur
	 */
	private Object getValue(final IKeyValueFileEntry key, final boolean defaultValue) {
		if (defaultValue) { return key.getDefaultValue(); }
		return getValue(key);
	}

	@Override
	public final void loadProperties(final Properties properties) throws IOException {
		List<IKeyValueFileEntry> missingEntries = new ArrayList<IKeyValueFileEntry>();
		for (IKeyValueFileEntry entry : mEntries) {
			Object value = properties.get(entry.getKey());
			if (entry.isMandatory() && value == null) {
				missingEntries.add(entry);
			} else if (value != null) {
				Object v = entry.getDefaultValue().getClass().cast(value);
				boolean isAccepted = false;
				if (entry.getPossibleValues().length > 0) {
					for (Object possibleValue : entry.getPossibleValues()) {
						if (possibleValue.equals(v)) {
							isAccepted = true;
						}
					}
				} else {
					isAccepted = true;
				}
				if (isAccepted) {
					mValues.put(entry.getKey(), v);
				} else {
					throw new IOException(MessageFormat.format(EN_VALUE_OUT_OF_BOUNDS, entry.getKey(), value, Arrays.toString(entry.getPossibleValues())));
				}
			}
		}
		if (missingEntries.isEmpty()) {
			mIsLoaded = true;
		} else {
			StringBuilder sb = new StringBuilder(EN_MISSING_MANDATORY_KEYS).append(LS);
			for (IKeyValueFileEntry missingEntry : missingEntries) {
				sb.append(missingEntry.getKey()).append(LS);
			}
			throw new IOException(sb.toString());
		}
	}

	/**
	 * Reload() non supporté avec cette méthode de chargement.
	 */
	@Override
	public final void loadInputStream(final InputStream is) throws IOException {
		Properties loadedProperties = new Properties();
		try {
			loadedProperties.load(is);
		} finally {
			if (is != null) {
				is.close();
			}
		}
		loadProperties(loadedProperties);
	}

	@Override
	public final void loadUrl(final URL url) throws IOException {
		mLastLoadingMethod = LOADING_METHOD_URL;
		mLastLoadingMethodArg = url;

		InputStream is = null;
		try {
			is = url.openStream();
		} finally {
			if (is != null) {
				is.close();
			}
		}
		loadInputStream(is);
	}

	@Override
	public final void loadBundle(final String bundleName, final Locale locale) throws MissingResourceException {
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, new Utf8BundleControl());
		loadBundle(bundle);
	}

	@Override
	public final void loadBundle(final ResourceBundle bundle) {
		mLastLoadingMethod = LOADING_METHOD_BUNDLE;
		mLastLoadingMethodArg = bundle;

		List<IKeyValueFileEntry> missingEntries = new ArrayList<IKeyValueFileEntry>();
		for (IKeyValueFileEntry entry : mEntries) {
			try {
				Object value = bundle.getString(entry.getKey());
				Class<? extends Object> classOfDefaultValue = entry.getDefaultValue().getClass();
				if (classOfDefaultValue == int.class || classOfDefaultValue == Integer.class) {
					value = Integer.parseInt((String) value);
				} else if (classOfDefaultValue == double.class || classOfDefaultValue == Double.class) {
					value = Double.parseDouble((String) value);
				} else if (classOfDefaultValue == long.class || classOfDefaultValue == Long.class) {
					String sLong = (String) value;
					if (sLong.toUpperCase().endsWith("L")) {
						sLong = sLong.substring(0, sLong.length() - 1);
					}
					value = Long.parseLong(sLong);
				} else if (classOfDefaultValue == boolean.class || classOfDefaultValue == Boolean.class) {
					value = Boolean.valueOf((String) value);
				}

				// Vérification de la valeur
				boolean isAccepted = false;
				if (entry.getPossibleValues().length > 0) {
					for (Object possibleValue : entry.getPossibleValues()) {
						if (possibleValue.equals(value)) {
							isAccepted = true;
							break;
						}
					}
				} else {
					isAccepted = true;
				}
				if (isAccepted) {
					mValues.put(entry.getKey(), value);
				} else {
					throw new IllegalArgumentException(MessageFormat.format(EN_VALUE_OUT_OF_BOUNDS, entry.getKey(), value, Arrays.toString(entry.getPossibleValues())));
				}
			} catch (MissingResourceException e) {
				if (entry.isMandatory()) {
					missingEntries.add(entry);
				}
			}
		}
		if (missingEntries.isEmpty()) {
			mIsLoaded = true;
		} else {
			StringBuilder sb = new StringBuilder(EN_MISSING_MANDATORY_KEYS).append(LS);
			for (IKeyValueFileEntry missingEntry : missingEntries) {
				sb.append(missingEntry.getKey()).append(LS);
			}
			throw new IllegalArgumentException(sb.toString());
		}

		if (mIsLoaded && mWatcher != null) {
			watch(this);
		}
	}

	@Override
	public final void loadPath(final String path) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void reload() throws IOException {
		mIsLoaded = false;
		mLoadException = null;
		if (mLastLoadingMethod == LOADING_METHOD_URL) {
			loadUrl((URL) mLastLoadingMethodArg);
		} else if (mLastLoadingMethod == LOADING_METHOD_BUNDLE) {
			ResourceBundle.clearCache();
			loadBundle((ResourceBundle) mLastLoadingMethodArg);
		}
		mLastUpdate = System.currentTimeMillis();
	}

	/**
	 * Retourne le caractère à utiliser pour débuter une nouvelle ligne de
	 * caractères.
	 * 
	 * @return le caractère à utiliser pour débuter une nouvelle ligne de
	 *         caractères.
	 */
	protected final String getCommentDelimiter() {
		String result = "#";
		KeyValueFile comments = getClass().getAnnotation(KeyValueFile.class);
		if (comments != null) {
			result = comments.commentsDelimiter();
		}
		return result;
	}

	/**
	 * Appelé par {@link #getFileAsString(boolean)}. Concatène un header dans le
	 * StringBuilder (nom de fichier, avertissements, date de
	 * génération...etc.).
	 * 
	 * @param isTemplate
	 *            Paramètre qui vaut <code>true</code> si le fichier à générer
	 *            est un template.
	 * @param sb
	 *            Le Stringbuilder dans lequel concaténer le header de fichier.
	 */
	protected final void appendFileHeader(final boolean isTemplate, final StringBuilder sb) {
		String lineTiret = "--------------------------------------";
		String commentDelimiter = getCommentDelimiter();
		sb.append(commentDelimiter).append(ESPACE).append(lineTiret).append(LS);
		sb.append(commentDelimiter).append(" BundleId   = ").append(getId()).append(LS);
		sb.append(commentDelimiter).append(" BundleName = ").append(getBundleName()).append(LS);
		if (isTemplate) {
			sb.append(commentDelimiter).append(ESPACE).append(EN_TEMPLATE_FILE_ADVERT).append(LS);
		}
		sb.append(commentDelimiter).append(ESPACE).append(lineTiret).append(LS);
		sb.append(commentDelimiter).append(LS2);
	}

	/**
	 * Ajoute un commentaire avant chaque déclaration de clé optionnelle. Le but
	 * du commentaire est de prévenir l'utilisateur que la clé est optionnelle.
	 * <p>
	 * Exemple de ligne générée :
	 * 
	 * <pre>
	 * # Please uncomment the following line and change its value.
	 * </pre>
	 * 
	 * @param sb
	 *            Le buffer dans lequel mettre le commentaire.
	 */
	protected final void appendOptionalKeyAdvertising(final StringBuilder sb) {
		String commentDelimiter = getCommentDelimiter();
		sb.append(commentDelimiter).append(ESPACE).append(EN_OPT_KEY_ADVERT).append(LS);
	}

	/**
	 * Ajoute un commentaire avant la déclaration des clés non obligatoires.
	 * 
	 * @param entry
	 *            Entree
	 * @param sb
	 *            Le buffer dans lequel mettre le commentaire.
	 */
	protected final void appendEntrySpecifications(final IKeyValueFileEntry entry, final StringBuilder sb) {
		String fleche = "-> ";
		String commentDelimiter = getCommentDelimiter();
		sb.append(commentDelimiter).append(ESPACE).append(fleche).append(EN_MANDATORY).append(EGAL).append(entry.isMandatory()).append(LS);
		String val = String.valueOf(entry.getDefaultValue()).replaceAll(NEW_LINE_SLASH, NEW_LINE_DOUBLE_SLASH);
		sb.append(commentDelimiter).append(ESPACE).append(fleche).append(EN_DEFAULT_VALUE).append(EGAL).append(val).append(LS);

		if (entry.getPossibleValues().length > 0) {
			String possibleValues = Arrays.toString(entry.getPossibleValues()).replaceAll(NEW_LINE_SLASH, NEW_LINE_DOUBLE_SLASH);
			sb.append(commentDelimiter).append(ESPACE).append(fleche).append(EN_POSSIBLE_VALUES).append(EGAL).append(possibleValues).append(LS);
		}
	}

	/**
	 * Méthode pour s'assurer que les valeurs ont bien été chargées.
	 * 
	 * @throws RuntimeException
	 *             Exception durant le lancement
	 */
	protected final void checkLoaded() throws PropertyNotFoundException {
		String message = "Values are not loaded correctly.";
		if (!mIsLoaded) {
			if (getException() != null) {
				throw new PropertyNotFoundException(message, getException());
			} else {
				throw new PropertyNotFoundException(message);
			}
		}
	}

	@Override
	public final String getTemplateFileAsString() {
		return getFileAsString(true);
	}

	@Override
	public final String getValueFileAsString() {
		checkLoaded();
		return getFileAsString(false);
	}

	/**
	 * Retourne le fichier comme un texte.
	 * 
	 * @param isTemplate
	 *            Est un template
	 * @return le fichier sous forme d'un texte
	 */
	private String getFileAsString(final boolean isTemplate) {
		StringBuilder sb = new StringBuilder();
		String commentDelimiter = getCommentDelimiter();

		KeyValueFile fileSpec = getClass().getAnnotation(KeyValueFile.class);
		if (fileSpec != null) {
			sb.append(commentDelimiter).append(ESPACE).append(fileSpec.comments()).append(LS);
		}
		appendFileHeader(isTemplate, sb);

		Field[] fields = getClass().getFields();
		for (Field field : fields) {
			boolean isGoodType = false;
			try {
				isGoodType = IKeyValueFileEntry.class.isInstance(field.get(null));
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				LogTracer.getLogger().debug(e1.getMessage(), e1);
			}

			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) /* && Modifier.isFinal(mod) */) {
				if (isGoodType) {
					try {
						IKeyValueFileEntry entry = (IKeyValueFileEntry) field.get(null);
						Comments entryComments = field.getAnnotation(Comments.class);

						// On met les commentaires de l'utilisateur, ensuite les
						// spécifications, puis un avertissement si c'est une
						// clé optionnelle et enfin la paire clé=valeur.
						if (entryComments != null) {
							// Commentaires
							Pattern pattern = Pattern.compile(NEW_LINE_SLASH);
							Matcher matcher = pattern.matcher(entryComments.value());
							String replaceAll = matcher.replaceAll(LS + commentDelimiter + ESPACE);
							sb.append(commentDelimiter).append(ESPACE).append(Matcher.quoteReplacement(replaceAll));
							sb.append(LS).append(commentDelimiter).append(LS);
						}

						appendEntrySpecifications(entry, sb);

						// Si la clé n'est pas obligatoire on la met en
						// commentaire
						if (!entry.isMandatory()) {
							appendOptionalKeyAdvertising(sb);
							sb.append(getCommentDelimiter()).append(ESPACE);
						}
						// clé=valeur
						String val = String.valueOf(getValue(entry, isTemplate)).replaceAll(NEW_LINE_SLASH, NEW_LINE_DOUBLE_SLASH);
						val = val.replaceAll("([^'])'([^'])", "$1''$2");
						sb.append(entry.getKey()).append(EGAL).append(val);
						sb.append(LS2);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						LogTracer.getLogger().debug(e.getMessage(), e);
					}
				}
			}
		}
		return sb.toString();
	}

	@Override
	public final long lastUpdate() {
		return mLastUpdate;
	}

	/**
	 * Retourne la valeur de type String.
	 * 
	 * @param entry
	 *            l'entree
	 * @return la valeur
	 */
	public final String getString(final IKeyValueFileEntry entry) {
		return Tools.getText(this, entry);
	}

	/**
	 * Retourne la valeur de type int.
	 * 
	 * @param entry
	 *            l'entree
	 * @return la valeur
	 */
	public final int getInt(final IKeyValueFileEntry entry) {
		return Tools.getInt(this, entry);
	}

	/**
	 * Retourne la valeur de type long.
	 * 
	 * @param entry
	 *            l'entree
	 * @return la valeur
	 */
	public final long getLong(final IKeyValueFileEntry entry) {
		return Tools.getLong(this, entry);
	}

	/**
	 * Retourne la valeur de type double.
	 * 
	 * @param entry
	 *            l'entree
	 * @return la valeur
	 */
	public final double getDouble(final IKeyValueFileEntry entry) {
		return Tools.getDouble(this, entry);
	}

	/**
	 * Retourne la valeur de type boolean.
	 * 
	 * @param entry
	 *            l'entree
	 * @return la valeur
	 */
	public final boolean getBoolean(final IKeyValueFileEntry entry) {
		return Tools.getBoolean(this, entry);
	}

	/**
	 * Retourne la valeur de type Color.
	 * 
	 * @param entry
	 *            l'entree
	 * @return la valeur
	 */
	public final Color getColor(final IKeyValueFileEntry entry) {
		return Tools.getColor(this, entry);
	}

	/**
	 * Retourne la valeur de type Dimension.
	 * 
	 * @param entry
	 *            l'entree
	 * @return la valeur
	 */
	public final Dimension getDimension(final IKeyValueFileEntry entry) {
		return Tools.getDimension(this, entry);
	}

	/**
	 * Retourne la valeur de type File.
	 * 
	 * @param entry
	 *            l'entree
	 * @return la valeur
	 */
	public final File getFile(final IKeyValueFileEntry entry) {
		return Tools.getFile(this, entry);
	}

	@Override
	public final String toString() {
		try {
			return getValueFileAsString();
		} catch (Throwable e) {
			return mValues.toString();
		}
	}

	/**
	 * Alias pour la classe GenericKeyValueFileEntry pour rendre les
	 * déclarations moins longues.
	 */
	public static class E extends GenericKeyValueFileEntry {
		/**
		 * Constructeur.
		 * 
		 * @param key
		 *            Clef
		 * @param defaultValue
		 *            Valeur par defaut
		 * @param isMandatory
		 *            est obligatoire
		 */
		public E(final String key, final Object defaultValue, final boolean isMandatory) {
			super(key, defaultValue, isMandatory);
		}

		/**
		 * Constructeur.
		 * 
		 * @param key
		 *            Clef
		 * @param defaultValue
		 *            Valeur pas defaut
		 * @param possibleValues
		 *            Valeurs possibles
		 * @param isMandatory
		 *            est obligatoire
		 */
		public E(final String key, final Object defaultValue, final Object[] possibleValues, final boolean isMandatory) {
			super(key, defaultValue, possibleValues, isMandatory);
		}
	}

	/**
	 * Idem alias "E" mais paramétré par le type de la valeur attendue dans le
	 * bundle. Les types autorisés sont : Integer, Double, Long, String et
	 * Color.
	 * 
	 * @param <T>
	 *            Type de la valeur
	 */
	public static class TE<T> extends E {
		/**
		 * Constructeur.
		 * 
		 * @param key
		 *            Clef
		 * @param defaultValue
		 *            Valeur par defaut
		 * @param isMandatory
		 *            est obligatoire
		 */
		public TE(final String key, final T defaultValue, final boolean isMandatory) {
			super(key, defaultValue, isMandatory);
		}

		/**
		 * Constructeur.
		 * 
		 * @param key
		 *            Clef
		 * @param defaultValue
		 *            Valeur par defaut
		 * @param possibleValues
		 *            Valeurs possbiles
		 * @param isMandatory
		 *            est obligatoire
		 */
		public TE(final String key, final T defaultValue, final T[] possibleValues, final boolean isMandatory) {
			super(key, defaultValue, possibleValues, isMandatory);
		}

		/**
		 * Retourne la valeur.
		 * 
		 * @param args
		 *            Arguments
		 * @return le valeur
		 */
		@SuppressWarnings("unchecked")
		public final T getValue(final Object... args) {
			Object value = getOwner().getValue(this);
			Object defaultValue = getDefaultValue();
			if (value == null) {
				value = defaultValue;
			}
			if (value instanceof String) {
				value = Tools.getText(getOwner(), this, args);

				if (defaultValue instanceof File) {
					value = new File((String) value);
				} else if (defaultValue instanceof Dimension) {
					value = Tools.parseDimension((String) value);
				} else if (defaultValue instanceof Color) {
					value = Tools.parseColor((String) value);
				}
			}

			return (T) value;
		}
	}
}
