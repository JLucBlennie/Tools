package com.jlb.tools.metamodel.criterion;

/**
 * Interface ICriterion : Critère.
 * 
 * @author JLuc
 *
 * @param <T>
 *            Type
 */
public interface ICriterion<T> {

    /**
     * Récupération du nom de la table.
     * 
     * @return Nom de la table
     */
    String getTableName();

    /**
     * Récupération de la valeur.
     * 
     * @return Valeur
     */
    T getValue();

    /**
     * Récupération de l'opérateur de la requète.
     * 
     * @return Opérateur
     */
    Operator getOperator();

    /**
     * Récupération du nom de l'attribut.
     * 
     * @return Le nom de l'attribut
     */
    String getAttributeName();
}
