package com.jlb.tools.metamodel.attributes.impl;

import com.jlb.tools.metamodel.attributes.IAttribute;

public class StringAttribute implements IAttribute<String> {

	private String mName;
	private String mValue;
	private String mUnit = "";

	public StringAttribute(String name) {
		mName = name;
		mValue = null;
		mUnit = "";
	}

	public StringAttribute(String name, String value) {
		mName = name;
		mValue = value;
		mUnit = "";
	}

	public StringAttribute(String name, String value, String unit) {
		mName = name;
		mValue = value;
		mUnit = unit;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getValue() {
		return mValue;
	}

	@Override
	public void setValue(String value) {
		mValue = value;
	}

	@Override
	public String getUnit() {
		return mUnit;
	}

	@Override
	public String getType() {
		return "string";
	}

	@Override
	public String toString() {
		return "Attribut (String) " + mName + " = " + mValue + (mUnit.isEmpty() ? "" : " " + mUnit);
	}
}
