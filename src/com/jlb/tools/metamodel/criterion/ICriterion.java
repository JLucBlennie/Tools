package com.jlb.tools.metamodel.criterion;

public interface ICriterion<T> {

	public String getTableName();

	public T getValue();

	public E_OPERATOR getOperator();

	public String getAttributeName();
}
