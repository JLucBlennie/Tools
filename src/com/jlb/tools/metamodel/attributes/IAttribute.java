package com.jlb.tools.metamodel.attributes;

public interface IAttribute<T> {

	public String getName();

	public T getValue();

	public void setValue(T value);

	public String getUnit();

	public String getType();
}
