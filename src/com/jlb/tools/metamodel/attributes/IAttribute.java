package com.jlb.tools.metamodel.attributes;

/**
 * Interface IAttribut : Attribut.
 * 
 * @author JLuc
 *
 * @param <T>
 *            Type d'attribut
 */
public interface IAttribute<T> {

    /**
     * Récupération du nom.
     * 
     * @return Nom de l'attribut
     */
    String getName();

    /**
     * Récupération de la valeur.
     * 
     * @return La valeur
     */
    T getValue();

    /**
     * Mise à jour de la valeur.
     * 
     * @param value
     *            Valeur
     */
    void setValue(T value);

    /**
     * Récupération de l'unité.
     * 
     * @return L'unité
     */
    String getUnit();

    /**
     * Récupération du type.
     * 
     * @return Nom du type
     */
    String getType();
}
