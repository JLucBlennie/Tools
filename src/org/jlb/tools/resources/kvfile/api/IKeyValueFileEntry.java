package org.jlb.tools.resources.kvfile.api;

/**
 * Une entrée d'un fichier de propriétés. Elle est identifiée par une clé et a
 * une valeur par défaut. Pour les clés qui ne peuvent que recevoir qu'un nombre
 * fini de valeurs, la méthode {@link #getPossibleValues()} permet de connaitre
 * les valeurs acceptées.
 * 
 * @author JLuc
 */
public interface IKeyValueFileEntry {
	/**
	 * Clé de l'entrée.
	 * 
	 * @return La clé de l'entrée.
	 */
	String getKey();

	/**
	 * La valeur par défaut de l'entrée lorsqu'aucune valeur n'est précisée.
	 * 
	 * @return La valeur par défaut.
	 */
	Object getDefaultValue();

	/**
	 * Tableau des valeurs acceptées.
	 * 
	 * @return Le tableau des valeurs acceptées.
	 */
	Object[] getPossibleValues();

	/**
	 * Détermine si l'entrée du fichier de propriétés est obligatoire ou non.
	 * 
	 * @return <code>true</code> si l'entrée est obligatoire.
	 */
	boolean isMandatory();
}
