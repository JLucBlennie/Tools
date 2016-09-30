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
     * R�cup�ration du nom.
     * 
     * @return Nom de l'attribut
     */
    String getName();

    /**
     * R�cup�ration de la valeur.
     * 
     * @return La valeur
     */
    T getValue();

    /**
     * Mise � jour de la valeur.
     * 
     * @param value
     *            Valeur
     */
    void setValue(T value);

    /**
     * R�cup�ration de l'unit�.
     * 
     * @return L'unit�
     */
    String getUnit();

    /**
     * R�cup�ration du type.
     * 
     * @return Nom du type
     */
    String getType();
}
