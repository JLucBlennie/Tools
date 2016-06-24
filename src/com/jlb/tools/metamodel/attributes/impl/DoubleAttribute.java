package com.jlb.tools.metamodel.attributes.impl;

import com.jlb.tools.metamodel.attributes.IAttribute;

public class DoubleAttribute implements IAttribute<Double> {

	private String mName;
	private double mValue;
	private String mUnit;

	public DoubleAttribute(String name) {
		mName = name;
		mValue = -1.0;
		mUnit = "";
	}

	public DoubleAttribute(String name, String unit) {
		mName = name;
		mValue = -1.0;
		mUnit = unit;
	}

	public DoubleAttribute(String name, double value) {
		mName = name;
		mValue = value;
		mUnit = "";
	}

	public DoubleAttribute(String name, double value, String unit) {
		mName = name;
		mValue = value;
		mUnit = unit;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public Double getValue() {
		return mValue;
	}

	@Override
	public void setValue(Double value) {
		mValue = value;
	}

	@Override
	public String getUnit() {
		return mUnit;
	}

	@Override
	public String getType() {
		return "double";
	}

	@Override
	public String toString() {
		return "Attribut (Double) " + mName + " = " + mValue + (mUnit.isEmpty() ? "" : " " + mUnit);
	}
}
