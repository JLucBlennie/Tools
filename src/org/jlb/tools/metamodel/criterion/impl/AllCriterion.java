package org.jlb.tools.metamodel.criterion.impl;

import org.jlb.tools.metamodel.Entity;
import org.jlb.tools.metamodel.criterion.ICriterion;
import org.jlb.tools.metamodel.criterion.Operator;

/**
 * Classe AllCriterion : Critère pour récupérer toutes les entités du même type.
 * 
 * @author JLuc
 *
 */
public class AllCriterion implements ICriterion<Entity> {

	/**
	 * Nom de la table pour le type d'entité à récupérer.
	 */
	private final String mTableName;

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
