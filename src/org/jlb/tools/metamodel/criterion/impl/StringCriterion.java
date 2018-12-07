package org.jlb.tools.metamodel.criterion.impl;

import org.jlb.tools.metamodel.criterion.ICriterion;
import org.jlb.tools.metamodel.criterion.Operator;

/**
 * Critère de type String.
 * 
 * @author JLuc
 *
 */
public class StringCriterion implements ICriterion<String> {

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
	private final String mValue;

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
	public StringCriterion(final String tableName, final String attributeName, final Operator operator, final String value) {
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
	public final String getValue() {
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
