package com.jlb.tools.metamodel.attributes.impl;

import com.jlb.tools.metamodel.attributes.IAttribute;

/**
 * Classe IntegerAttribute : Attribut de type integer.
 * 
 * @author JLuc
 *
 */
public class IntegerAttribute implements IAttribute<Integer> {

    /**
     * Nom de l'attribut.
     */
    private String mName;

    /**
     * Valeur de l'attribut.
     */
    private int mValue;

    /**
     * Unité de l'attribut.
     */
    private String mUnit;

    /**
     * Constructeur avec le nom.
     * 
     * @param name
     *            Nom de l'attribut
     */
    public IntegerAttribute(final String name) {
        mName = name;
        mValue = -1;
        mUnit = "";
    }

    /**
     * Constructeur avec nom et unité.
     * 
     * @param name
     *            Nom de l'attribut
     * @param unit
     *            Unité de l'attribut
     */
    public IntegerAttribute(final String name, final String unit) {
        mName = name;
        mValue = -1;
        mUnit = unit;
    }

    /**
     * Constructeur avec nom et valeur.
     * 
     * @param name
     *            Nom de l'attribut
     * @param value
     *            Valeur de l'attribut
     */
    public IntegerAttribute(final String name, final int value) {
        mName = name;
        mValue = value;
        mUnit = "";
    }

    /**
     * Construteur avec le nom, la valeur et l'unité.
     * 
     * @param name
     *            Nom de l'attribut
     * @param value
     *            Valeur de l'attribut
     * @param unit
     *            Unité de l'attribut
     */
    public IntegerAttribute(final String name, final int value, final String unit) {
        mName = name;
        mValue = value;
        mUnit = unit;
    }

    @Override
    public final String getName() {
        return mName;
    }

    @Override
    public final Integer getValue() {
        return mValue;
    }

    @Override
    public final void setValue(final Integer value) {
        mValue = value;
    }

    @Override
    public final String getUnit() {
        return mUnit;
    }

    @Override
    public final String getType() {
        return "integer";
    }

    @Override
    public final String toString() {
        return "Attribut (Integer) " + mName + " = " + mValue + (mUnit.isEmpty() ? "" : " " + mUnit);
    }
}
