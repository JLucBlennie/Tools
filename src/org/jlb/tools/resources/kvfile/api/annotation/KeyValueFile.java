package org.jlb.tools.resources.kvfile.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour préciser qu'une classe est un dictionnaire de clés/valeurs.
 * <p>
 * La propriété "comments" permet d'associer un commentaire qui sera écrit comme
 * entête de fichier dans le template de fichier.
 * <p>
 * La propriété "commentsDelimiter" sert à spécifier quelle chaine de caractères
 * sert à délimiter les commentaires (généralement c'est "#" pour les fichiers
 * de propriétés).
 * <p>
 * La propriété "bundle" décrit l'emplacement du bundle sous-jacent. Par défaut
 * un objet IKeyValueFile annoté avec bundle="resource/myfile" sera chargé
 * depuis le fichier "resource/myfile.properties" du classpath (en fonction de
 * la Locale, le chargement du fichier s'adaptera et ajoutera le suffixe
 * correspondant à la langue de l'utilisateur).
 * <p>
 * La propriété "autoReload" permet de recharger automatiquement les changements
 * à chaud.
 * 
 * @author JLuc
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ ElementType.TYPE })
public @interface KeyValueFile {

	/**
	 * Retourne le caractere de debut de commentaire.
	 * 
	 * @return le caractere
	 */
	String commentsDelimiter() default "#";

	/**
	 * Retourne le commentaire de la valeur.
	 * 
	 * @return le commentaire
	 */
	String comments() default "";

	/**
	 * Le bundle du fichier de configuration.
	 * 
	 * @return le bundle
	 */
	String bundle() default "";

	/**
	 * Autorise le chargement a chaud.
	 * 
	 * @return vrai si le chargement a chaud est actif
	 */
	boolean autoReload() default false;
}
