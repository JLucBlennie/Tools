package com.jlb.tools.metamodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jlb.tools.logging.ILogger;

public class Description {

	private Map<String, String> mClassToTableName = new HashMap<String, String>();
	private ILogger mLogger;

	public Description(List<Class<? extends Entity>> clazzs, ILogger logger) {
		mLogger = logger;
		for (Class<? extends Entity> clazz : clazzs) {
			try {
				Entity p = clazz.newInstance();
				String tableName = p.getTableName();
				List<Class> authorizedChildrenClass = p.getAuthorizedChildrenClass();
				createDescription(clazz.getName(), tableName, authorizedChildrenClass);
			} catch (InstantiationException | IllegalAccessException e) {
				mLogger.error(this, "Erreur lors de la recuperation des descriptions.", e);
			}
		}
	}

	private void createDescription(String entityClassName, String entityTableName,
			List<Class> authorizedChildrenClass) {
		mClassToTableName.put(entityClassName, entityTableName);
		for (Class<? extends Entity> child : authorizedChildrenClass) {
			try {
				Entity t = child.newInstance();
				createDescription(child.getName(), t.getTableName(), t.getAuthorizedChildrenClass());
			} catch (IllegalAccessException | IllegalArgumentException | SecurityException | InstantiationException e) {
				mLogger.error(this, "Erreur lors de la creation de la description", e);
			}
		}
	}

	public String getTableName(String className) {
		return mClassToTableName.get(className);
	}

	public String getClassName(String tableName) {
		for (String className : mClassToTableName.keySet()) {
			if (mClassToTableName.get(className).equals(tableName)) {
				return className;
			}
		}
		return null;
	}

	public Set<String> getClasseNames() {
		return mClassToTableName.keySet();
	}

	public Collection<String> getTableNames() {
		return mClassToTableName.values();
	}
}
