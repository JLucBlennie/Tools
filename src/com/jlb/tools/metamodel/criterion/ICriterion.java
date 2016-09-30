package com.jlb.tools.metamodel.criterion;

/**
 * Interface ICriterion : Crit�re.
 * 
 * @author JLuc
 *
 * @param <T>
 *            Type
 */
public interface ICriterion<T> {

    /**
     * R�cup�ration du nom de la table.
     * 
     * @return Nom de la table
     */
    String getTableName();

    /**
     * R�cup�ration de la valeur.
     * 
     * @return Valeur
     */
    T getValue();

    /**
     * R�cup�ration de l'op�rateur de la requ�te.
     * 
     * @return Op�rateur
     */
    Operator getOperator();

    /**
     * R�cup�ration du nom de l'attribut.
     * 
     * @return Le nom de l'attribut
     */
    String getAttributeName();
}
