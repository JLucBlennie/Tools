package com.jlb.tools.metamodel.attributes.impl;

import com.jlb.tools.metamodel.attributes.IAttribute;

/**
 * Classe DoubleAttribute : Attribut de type double.
 * 
 * @author JLuc
 *
 */
public class DoubleAttribute implements IAttribute<Double> {

    /**
     * Nom de l'attribut.
     */
    private String mName;

    /**
     * Valeur de l'attribut.
     */
    private double mValue;

    /**
     * Unité de l'attribut.
     */
    private String mUnit;

    /**
     * Constructeur sur le nom.
     * 
     * @param name
     *            Nom de l'attribut
     */
    public DoubleAttribute(final String name) {
        mName = name;
        mValue = -1.0;
        mUnit = "";
    }

    /**
     * Constructeur sur le nom et l'unité.
     * 
     * @param name
     *            Nom de l'attribut
     * @param unit
     *            Unité de l'attribut
     */
    public DoubleAttribute(final String name, final String unit) {
        mName = name;
        mValue = -1.0;
        mUnit = unit;
    }

    /**
     * Constructeur sur le nom et la valeur.
     * 
     * @param name
     *            Nom de l'attribut
     * @param value
     *            Valeur de l'attribut
     */
    public DoubleAttribute(final String name, final double value) {
        mName = name;
        mValue = value;
        mUnit = "";
    }

    /**
     * Constructeur sur le nom, la valeur et l'unité.
     * 
     * @param name
     *            Nom de l'attribut
     * @param value
     *            Valeur de l'attribut
     * @param unit
     *            Unité de l'attribut
     */
    public DoubleAttribute(final String name, final double value, final String unit) {
        mName = name;
        mValue = value;
        mUnit = unit;
    }

    @Override
    public final String getName() {
        return mName;
    }

    @Override
    public final Double getValue() {
        return mValue;
    }

    @Override
    public final void setValue(final Double value) {
        mValue = value;
    }

    @Override
    public final String getUnit() {
        return mUnit;
    }

    @Override
    public final String getType() {
        return "double";
    }

    @Override
    public final String toString() {
        return "Attribut (Double) " + mName + " = " + mValue + (mUnit.isEmpty() ? "" : " " + mUnit);
    }
}
