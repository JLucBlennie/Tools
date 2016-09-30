package com.jlb.tools.metamodel.criterion.impl;

import com.jlb.tools.metamodel.Entity;
import com.jlb.tools.metamodel.criterion.Operator;
import com.jlb.tools.metamodel.criterion.ICriterion;

/**
 * Classe AllCriterion : Crit�re pour r�cup�rer toutes les entit�s du m�me type.
 * 
 * @author JLuc
 *
 */
public class AllCriterion implements ICriterion<Entity> {

    /**
     * Nom de la table pour le type d'entit� � r�cup�rer.
     */
    private String mTableName;

    /**
     * Constructeur.
     * 
     * @param tableName
     *            Table de recherche
     */
    public AllCriterion(final String tableName) {
        mTableName = tableName;
    }

    @Override
    public final String getTableName() {
        return mTableName;
    }

    @Override
    public final Entity getValue() {
        return null;
    }

    @Override
    public final Operator getOperator() {
        return null;
    }

    @Override
    public final String getAttributeName() {
        return null;
    }

    @Override
    public final String toString() {
        return "Tous les elements de type " + mTableName;
    }
}
