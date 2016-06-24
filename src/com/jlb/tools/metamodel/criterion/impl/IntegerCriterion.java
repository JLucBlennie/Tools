package com.jlb.tools.metamodel.criterion.impl;

import com.jlb.tools.metamodel.criterion.E_OPERATOR;
import com.jlb.tools.metamodel.criterion.ICriterion;

public class IntegerCriterion implements ICriterion<Integer> {

	private String mTableName;
	private E_OPERATOR mOperator;
	private int mValue;
	private String mAttributeName;

	public IntegerCriterion(String tableName, String attributeName, E_OPERATOR operator, int value) {
		mTableName = tableName;
		mOperator = operator;
		mAttributeName = attributeName;
		mValue = value;
	}

	@Override
	public String getTableName() {
		return mTableName;
	}

	@Override
	public Integer getValue() {
		return mValue;
	}

	@Override
	public E_OPERATOR getOperator() {
		return mOperator;
	}

	@Override
	public String getAttributeName() {
		return mAttributeName;
	}

	@Override
	public String toString() {
		return mTableName + " - " + mAttributeName + " " + mOperator.name() + " " + mValue;
	}
}
