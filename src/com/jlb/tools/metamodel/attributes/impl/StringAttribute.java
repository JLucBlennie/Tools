package com.jlb.tools.metamodel.attributes.impl;

import com.jlb.tools.metamodel.attributes.IAttribute;

/**
 * Classe StringAttribute : Attribut de type String.
 * 
 * @author JLuc
 *
 */
public class StringAttribute implements IAttribute<String> {

    /**
     * Nom de l'attribut.
     */
    private String mName;

    /**
     * Valeur de l'attribut.
     */
    private String mValue;

    /**
     * Unité de l'attribut.
     */
    private String mUnit = "";

    /**
     * Constructeur avec le nom.
     * 
     * @param name
     *            Nom de l'attribut
     */
    public StringAttribute(final String name) {
        mName = name;
        mValue = null;
        mUnit = "";
    }

    /**
     * Constructeur avec nom et valeur.
     * 
     * @param name
     *            Nom de l'attribut
     * @param value
     *            Valeur de l'attribut
     */
    public StringAttribute(final String name, final String value) {
        mName = name;
        mValue = value;
        mUnit = "";
    }

    /**
     * Constructeur avec nom, valeur et unité.
     * 
     * @param name
     *            Nom de l'attribut
     * @param value
     *            Valeur de l'attribut
     * @param unit
     *            Unité de l'attribut
     */
    public StringAttribute(final String name, final String value, final String unit) {
        mName = name;
        mValue = value;
        mUnit = unit;
    }

    @Override
    public final String getName() {
        return mName;
    }

    @Override
    public final String getValue() {
        return mValue;
    }

    @Override
    public final void setValue(final String value) {
        mValue = value;
    }

    @Override
    public final String getUnit() {
        return mUnit;
    }

    @Override
    public final String getType() {
        return "string";
    }

    @Override
    public final String toString() {
        return "Attribut (String) " + mName + " = " + mValue + (mUnit.isEmpty() ? "" : " " + mUnit);
    }
}
