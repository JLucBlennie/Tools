package com.jlb.tools.metamodel.attributes.impl;

import com.jlb.tools.metamodel.attributes.IAttribute;

public class IntegerAttribute implements IAttribute<Integer> {

	private String mName;
	private int mValue;
	private String mUnit;

	public IntegerAttribute(String name) {
		mName = name;
		mValue = -1;
		mUnit = "";
	}

	public IntegerAttribute(String name, String unit) {
		mName = name;
		mValue = -1;
		mUnit = unit;
	}

	public IntegerAttribute(String name, int value) {
		mName = name;
		mValue = value;
		mUnit = "";
	}

	public IntegerAttribute(String name, int value, String unit) {
		mName = name;
		mValue = value;
		mUnit = unit;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public Integer getValue() {
		return mValue;
	}

	@Override
	public void setValue(Integer value) {
		mValue = value;
	}

	@Override
	public String getUnit() {
		return mUnit;
	}

	@Override
	public String getType() {
		return "integer";
	}

	@Override
	public String toString() {
		return "Attribut (Integer) " + mName + " = " + mValue + (mUnit.isEmpty() ? "" : " " + mUnit);
	}
}
