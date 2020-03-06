package org.jlb.tools.resources.kvfile.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.jlb.tools.logging.LogTracer;
import org.jlb.tools.resources.kvfile.api.IKeyValueFileEntry;

/**
 * Spécifie une entrée d'un fichier de clés/valeurs. Une entrée est une clé qui
 * a une valeur par défaut et qui est soit obligatoire ou optionnelle.
 * 
 * @author JLuc
 */
public class GenericKeyValueFileEntry implements IKeyValueFileEntry {
	/** Index initial de la pile. */
	private static final int INITIAL_STACK_ID = 3;
	/** Champ parent. */
	private Field mOwnerField;
	/** Clef. */
	private final String mKey;
	/** Valeur par defaut. */
	private final Object mDefaultValue;
	/** Valeurs possibles. */
	private final Object[] mPossibleValues;
	/** Valeur obligatoire. */
	private final boolean mIsMandatory;

	/**
	 * Constructeur.
	 * 
	 * @param key
	 *            Clé de l'entrée.
	 * @param defaultValue
	 *            Valeur par défaut.
	 * @param isMandatory
	 *            Valeur obligatoire ou optionnelle.
	 */
	GenericKeyValueFileEntry(String key, Object defaultValue, boolean isMandatory) {
		this(key, defaultValue, new Object[0], isMandatory);
	}

	/**
	 * Constructeur.
	 * 
	 * @param key
	 *            Clé de l'entrée.
	 * @param defaultValue
	 *            Valeur par défaut.
	 * @param possibleValues
	 *            Liste de valeurs possibles.
	 * @param isMandatory
	 *            Valeur obligatoire ou optionnelle.
	 */
	GenericKeyValueFileEntry(String key, Object defaultValue, Object[] possibleValues, boolean isMandatory) {
		mKey = key;
		mDefaultValue = defaultValue;
		mIsMandatory = isMandatory;
		mPossibleValues = Arrays.copyOf(possibleValues, possibleValues.length);

		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		try {
			int i = INITIAL_STACK_ID;
			Class<?> ownerClass = Class.forName(stack[2].getClassName());
			while (!GenericKeyValueFile.class.isAssignableFrom(ownerClass) && i < stack.length) {
				ownerClass = Class.forName(stack[i].getClassName());
				i++;
			}

			Field field = ownerClass.getField("INSTANCE");
			if ((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
				mOwnerField = field;
			}
		} catch (Exception e) {
			LogTracer.getLogger().debug(e.getMessage(), e);
		}
	}

	/**
	 * @return La classe dans laquelle est déclarée la
	 *         {@link GenericKeyValueFileEntry}.
	 */
	GenericKeyValueFile getOwner() {
		GenericKeyValueFile owner = null;
		try {
			if (mOwnerField != null) {
				Object o = mOwnerField.get(null);
				if (o instanceof GenericKeyValueFile) {
					owner = (GenericKeyValueFile) o;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LogTracer.getLogger().debug(e.getMessage(), e);
		}
		return owner;
	}

	@Override
	public String getKey() {
		return mKey;
	}

	@Override
	public Object getDefaultValue() {
		return mDefaultValue;
	}

	@Override
	public Object[] getPossibleValues() {
		return mPossibleValues;
	}

	@Override
	public boolean isMandatory() {
		return mIsMandatory;
	}

}
