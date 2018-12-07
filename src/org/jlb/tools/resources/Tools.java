package org.jlb.tools.resources;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jlb.tools.resources.kvfile.api.IKeyValueFile;
import org.jlb.tools.resources.kvfile.api.IKeyValueFileEntry;
import org.jlb.tools.resources.kvfile.impl.GenericKeyValueFile;

/**
 * Class d'utilitaire divers.
 * 
 * @author JLuc
 */
public final class Tools {

    /** Separateur ','. */
    private static final String SEPARATOR_COMMA = ",";
    /** EXpression reguliere. */
    private static final String REGEXP = "([^'])'([^'])";
    /** Expression de remplacement. */
    private static final String REGEXP_REPLACEMENT = "$1''$2";
    /** Prefix env. */
    private static final String ENV_PREFIX = "env:";

    /**
     * Constructor prive.
     */
    private Tools() {

    }

    /**
     * Ajoute un élément dans une Map sachant que les valeurs de la map sont des
     * set et que potentiellement la valeur d'une clé peut être
     * <code>null</code>. Si c'est le cas, le set qui est créée sera une HashSet
     * et l'élément 'obj' sera ajouté dedans.
     * 
     * @param map
     *            La map dans laquelle ajouter l'élément.
     * @param key
     *            La clé
     * @param value
     *            L'objet à ajouter.
     * @param <K>
     *            Classe de clef
     * @param <V>
     *            Classe de valeur
     */
    public static <K, V> void putValueInMapOfCollection(Map<K, Collection<V>> map, K key, V value) {
        Collection<V> collection = map.get(key);
        if (collection == null) {
            collection = new HashSet<V>();
            map.put(key, collection);
        }
        collection.add(value);
    }

    /**
     * La méthode prend en argument un fichier de ressources (bundle, fichier de
     * configuration...etc.) qui est normalement un singleton (voir
     * documentation de {@link GenericKeyValueFile} pour savoir comment déclarer
     * un nouveau fichier de propriétés).
     * 
     * @param fromFile
     *            fichier de proprietes
     * @param key
     *            clef
     * @param args
     *            arguments
     * @return la valeur
     */
    public static String getText(IKeyValueFile fromFile, IKeyValueFileEntry key, Object... args) {
        String s = "";
        Object result = fromFile.getValue(key);
        if (result == null) {
            result = String.valueOf(key.getDefaultValue());
        }
        if (!(result instanceof String)) {
            result = String.valueOf(result);
        }
        if (args == null) {
            s = MessageFormat.format(((String) result).replaceAll(REGEXP, REGEXP_REPLACEMENT), new Object[0]);
        } else {
            s = MessageFormat.format(((String) result).replaceAll(REGEXP, REGEXP_REPLACEMENT), args);
        }
        return s;
    }

    /**
     * Charge une couleur définie dans un fichier de paires clés/valeurs.
     * 
     * @param fromFile
     *            Le dictionnaire Java qui représente le fichier .properties.
     * @param key
     *            La clé Java qui représente la clé du dictionnaire .properties.
     * @return La couleur trouvée ou bien la couleur par défaut si la clé
     *         n'existe pas.
     */
    public static Color getColor(IKeyValueFile fromFile, IKeyValueFileEntry key) {
        // Recupere la valeur sous forme de chaine de caractere au format
        // Red,Green,Blue,Alpha
        String textualValue = getText(fromFile, key);
        Color color = parseColor(textualValue);
        return color;
    }

    /**
     * La méthode prend en argument un fichier de ressources (bundle, fichier de
     * configuration...etc.) qui est normalement un singleton (voir
     * documentation de {@link GenericKeyValueFile} pour savoir comment déclarer
     * un nouveau fichier de propriétés).
     * 
     * @param fromFile
     *            fichier de properties
     * @param key
     *            clef
     * @return la valeur
     */
    public static int getInt(IKeyValueFile fromFile, IKeyValueFileEntry key) {
        int result = ((Integer) fromFile.getValue(key)).intValue();
        return result;
    }

    /**
     * Retourne la valeur du bundle sous forme d'un objet File. La valeur texte
     * peut contenir des champs remplaçables du style "MessageFormat" ({0},
     * {1}...)
     * 
     * @param fromFile
     *            Dictionnaire dans lequel chercher la valeur.
     * @param key
     *            Clé recherchée.
     * @param args
     *            Arguments servant à remplacer les champs remplaçables.
     * @return la valeur
     */
    public static File getFile(IKeyValueFile fromFile, IKeyValueFileEntry key, Object... args) {
        String value = getText(fromFile, key, args);
        File result = new File(value);
        return result;
    }

    /**
     * La méthode prend en argument un fichier de ressources (bundle, fichier de
     * configuration...etc.) qui est normalement un singleton (voir
     * documentation de {@link GenericKeyValueFile} pour savoir comment déclarer
     * un nouveau fichier de propriétés).
     * 
     * @param fromFile
     *            fichier de propriete
     * @param key
     *            clef
     * @return la valeur
     */
    public static long getLong(IKeyValueFile fromFile, IKeyValueFileEntry key) {
        long result = ((Long) fromFile.getValue(key)).longValue();
        return result;
    }

    /**
     * La méthode prend en argument un fichier de ressources (bundle, fichier de
     * configuration...etc.) qui est normalement un singleton (voir
     * documentation de {@link GenericKeyValueFile} pour savoir comment déclarer
     * un nouveau fichier de propriétés).
     * 
     * @param fromFile
     *            fichier de proprietes
     * @param key
     *            clef
     * @return la valeur
     */
    public static double getDouble(IKeyValueFile fromFile, IKeyValueFileEntry key) {
        double result = ((Double) fromFile.getValue(key)).doubleValue();
        return result;
    }

    /**
     * Retourne une dimension dont la valeur est exprimée sous la forme
     * "dimension=width,height" dans le fichier de propriétés.
     * 
     * @param fromFile
     *            Le dictionnaire dans lequel trouver la valeur.
     * @param entry
     *            La clé du dictionnaire dont on cherche la valeur.
     * @return L'objet dimension correspondant aux mesures trouvées dans le
     *         fichier de propriétés du dictionnaire.
     */
    public static Dimension getDimension(IKeyValueFile fromFile, IKeyValueFileEntry entry) {
        String sDimension = getText(fromFile, entry);
        Dimension dim = parseDimension(sDimension);
        return dim;
    }

    /**
     * La méthode prend en argument un fichier de ressources (bundle, fichier de
     * configuration...etc.) qui est normalement un singleton (voir
     * documentation de {@link GenericKeyValueFile} pour savoir comment déclarer
     * un nouveau fichier de propriétés).
     * 
     * @param fromFile
     *            fichier de proprietes
     * @param key
     *            clef
     * @return la valeur
     */
    public static boolean getBoolean(IKeyValueFile fromFile, IKeyValueFileEntry key) {
        boolean result = ((Boolean) fromFile.getValue(key)).booleanValue();
        return result;
    }

    /**
     * Convertit une chaine en une couleur.
     * 
     * @param inTextualValue
     *            La valeur à convertir.
     * @return La couleur trouvée ou <code>null</code> si aucune correspondance
     *         n'a été trouvée.
     */
    public static Color parseColor(String inTextualValue) {
        String textualValue = inTextualValue;
        int indiceVirgule;
        String couleurPrimaire;
        Color color = null;

        // Recupere la valeur sous forme de chaine de caractere au format
        // Red,Green,Blue,Alpha
        if (textualValue != null && textualValue.length() > 0) {
            try {
                // extraction du rouge
                indiceVirgule = textualValue.indexOf(SEPARATOR_COMMA);
                couleurPrimaire = textualValue.substring(0, indiceVirgule);
                int red = Integer.parseInt(couleurPrimaire.trim());

                // supression de la chaine des caractere corespondant au rouge
                // ainsi que la virgule
                textualValue = textualValue.substring(indiceVirgule + 1).trim();

                // extraction du vert
                indiceVirgule = textualValue.indexOf(SEPARATOR_COMMA);
                couleurPrimaire = textualValue.substring(0, indiceVirgule);
                int green = Integer.parseInt(couleurPrimaire.trim());

                // supression de la chaine des caractere corespondant au vert
                // ainsi que la
                // virgule
                textualValue = textualValue.substring(indiceVirgule + 1);

                // extraction du bleu
                indiceVirgule = textualValue.indexOf(SEPARATOR_COMMA);

                if (indiceVirgule < 0) {
                    couleurPrimaire = textualValue;
                    int blue = Integer.parseInt(couleurPrimaire.trim());
                    // transforme les valeurs en couleur
                    color = new Color(red, green, blue);
                } else {
                    // Transparence
                    couleurPrimaire = textualValue.substring(0, indiceVirgule);
                    int blue = Integer.parseInt(couleurPrimaire.trim());

                    // extraction de la transparence
                    // supression de la chaine des caractere corespondant au
                    // bleu ainsi que la virgule
                    String sTransparence = textualValue.substring(indiceVirgule + 1);
                    int alpha = Integer.parseInt(sTransparence.trim());
                    color = new Color(red, green, blue, alpha);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return color;
    }

    /**
     * Retourne une dimension dont la valeur est exprimée sous la forme
     * "width,height". Les deux nombres doivent être des entiers.
     * 
     * @param sDimension
     *            La dimension sous forme d'une chaine de caractères.
     * @return L'objet dimension correspondant aux mesures trouvées dans le
     *         fichier de propriétés du dictionnaire.
     */
    public static Dimension parseDimension(String sDimension) {
        int indexOfVirgule = sDimension.indexOf(SEPARATOR_COMMA);
        String sWidth = sDimension.substring(0, indexOfVirgule).trim();
        String sHeight = sDimension.substring(indexOfVirgule + 1, sDimension.length()).trim();
        int width = Integer.parseInt(sWidth);
        int height = Integer.parseInt(sHeight);
        return new Dimension(width, height);
    }

    /**
     * Remplace les champs remplaçables du style "${variable}" d'une chaine de
     * caractères par les valeurs trouvées dans le dictionnaire passé en
     * argument.
     * 
     * @param value
     *            Valeur contenant des champs remplaçables du style
     *            "${variable}".
     * @param bundle
     *            Dictionnaire dans lequel on peut trouver la variable.
     * @return Une nouvelle chaine de caractères où les champs remplaçables sont
     *         remplacés par leur valeur trouvée dans le dictionnaire passé en
     *         argument.
     */
    public static String replacePlaceholders(String value, IKeyValueFile bundle) {
        String result = value;
        StringBuffer sb = new StringBuffer();
        if (value != null && value.length() > 0) {
            Pattern regexpPattern = Pattern.compile("\\$\\{([^\\}]*)\\}");
            Pattern backslashPattern = Pattern.compile("[^\\\\]\\\\[^\\\\]");
            Matcher regexpMatcher = regexpPattern.matcher(value);

            while (regexpMatcher.find()) {
                String capturedProperty = regexpMatcher.group(1);
                String capturedPropertyValue = null;

                if (capturedProperty != null && capturedProperty.length() > 0) {
                    if (capturedProperty.startsWith(ENV_PREFIX)) {
                        capturedProperty = capturedProperty.substring(ENV_PREFIX.length());
                        capturedPropertyValue = System.getenv(capturedProperty);
                    } else {
                        IKeyValueFileEntry bundleEntry = bundle.getEntry(capturedProperty);
                        if (bundleEntry != null) {
                            capturedPropertyValue = String.valueOf(bundle.getValue(bundleEntry));
                        }
                    }
                    if (capturedPropertyValue != null) {
                        // On fait le même travail récursivement sur la valeur
                        // remplaçante car
                        // elle contient potentiellement elle aussi des ${XXXX}
                        capturedPropertyValue = replacePlaceholders(capturedPropertyValue, bundle);

                        // Pour pallier à une limitation de la méthode
                        // Matcher.appendReplacement():
                        // remplacement des caractères \\ par \\\\.
                        Matcher backslashMatcher = backslashPattern.matcher(capturedPropertyValue);
                        if (backslashMatcher.find()) {
                            capturedPropertyValue = capturedPropertyValue.replaceAll("\\\\", "\\\\\\\\");
                        }
                        regexpMatcher.appendReplacement(sb, Matcher.quoteReplacement(capturedPropertyValue));
                    } else {
                        regexpMatcher.appendReplacement(sb, capturedProperty);
                    }
                }
            }
            regexpMatcher.appendTail(sb);
            result = sb.toString();
        }
        return result;
    }

    /**
     * Retourne le nom de package d'une classe en convertissant les séparateurs
     * "." en caractère "${pathSeparator}".
     * 
     * @param clazz
     *            La classe dont on veut avoir le nom du package.
     * @param pathSeparator
     *            Le caractère de séparation des packages.
     * @return le nom du package
     */
    public static String getPackageName(Class<?> clazz, String pathSeparator) {
        String separator = pathSeparator;
        if (separator == null) {
            separator = ".";
        }
        Package pkg = clazz.getPackage();
        String pkgName = pkg.getName();
        return pkgName.replaceAll("\\.", separator);
    }

    /**
     * Retourne le caractere de find de fichier.
     * 
     * @return le caractere de fin de fichier
     */
    public static String getCharEOF() {
        return System.getProperty("line.separator");
    }

    /**
     * Supprime les 'n' derniers caractères d'un StringBuilder.
     * 
     * @param buffer
     *            Le StringBuilder duquel on veut supprimer des caractères.
     * @param nbCharsToRemove
     *            Nombre de caractères à supprimer.
     * @return L'objet passé en argument, minoré des 'nbCharsToRemove' derniers
     *         caractères.
     */
    public static StringBuilder removeLastCharsFromBuffer(StringBuilder buffer, int nbCharsToRemove) {
        int length = buffer.length();
        buffer.delete(length - nbCharsToRemove, length);
        return buffer;
    }

}
