package org.jlb.tools.metamodel.criterion.impl;

import org.jlb.tools.metamodel.criterion.ICriterion;
import org.jlb.tools.metamodel.criterion.Operator;

/**
 * Critère de type integer.
 * 
 * @author JLuc
 *
 */
public class IntegerCriterion implements ICriterion<Integer> {

	/**
	 * Nom de la table.
	 */
	private final String mTableName;

	/**
	 * Opérateur.
	 */
	private final Operator mOperator;

	/**
	 * Valeur.
	 */
	private final int mValue;

	/**
	 * Nom de l'attribut.
	 */
	private final String mAttributeName;

	/**
	 * Constructeur.
	 * 
	 * @param tableName
	 *            Nom de la table
	 * @param attributeName
	 *            Nom de l'attribut
	 * @param operator
	 *            Opérateur
	 * @param value
	 *            Valeur
	 */
	public IntegerCriterion(final String tableName, final String attributeName, final Operator operator, final int value) {
		mTableName = tableName;
		mOperator = operator;
		mAttributeName = attributeName;
		mValue = value;
	}

	@Override
	public final String getTableName() {
		return mTableName;
	}

	@Override
	public final Integer getValue() {
		return mValue;
	}

	@Override
	public final Operator getOperator() {
		return mOperator;
	}

	@Override
	public final String getAttributeName() {
		return mAttributeName;
	}

	@Override
	public final String toString() {
		return mTableName + " - " + mAttributeName + " " + mOperator.name() + " " + mValue;
	}
}
