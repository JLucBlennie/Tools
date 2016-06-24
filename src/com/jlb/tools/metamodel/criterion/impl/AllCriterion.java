package com.jlb.tools.metamodel.criterion.impl;

import com.jlb.tools.metamodel.Entity;
import com.jlb.tools.metamodel.criterion.E_OPERATOR;
import com.jlb.tools.metamodel.criterion.ICriterion;

public class AllCriterion implements ICriterion<Entity> {

	private String mTableName;

	public AllCriterion(String tableName) {
		mTableName = tableName;
	}

	@Override
	public String getTableName() {
		return mTableName;
	}

	@Override
	public Entity getValue() {
		return null;
	}

	@Override
	public E_OPERATOR getOperator() {
		return null;
	}

	@Override
	public String getAttributeName() {
		return null;
	}

	@Override
	public String toString() {
		return "Tous les elements de type " + mTableName;
	}
}
