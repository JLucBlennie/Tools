package com.jlb.tools.metamodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jlb.tools.logging.ILogger;

/**
 * Classe Description : Description du mod�le de donn�es.
 * 
 * @author JLuc
 *
 */
public class Description {

    /**
     * Mapping entre les types d'entit� et le noms de table.
     */
    private Map<String, String> mClassToTableName = new HashMap<String, String>();

    /**
     * Logger.
     */
    private ILogger mLogger;

    /**
     * Constructeur.
     * 
     * @param clazzs
     *            Liste des types d'entit�
     * @param logger
     *            Logger
     */
    public Description(final List<Class<? extends Entity>> clazzs, final ILogger logger) {
        mLogger = logger;
        for (Class<? extends Entity> clazz : clazzs) {
            try {
                Entity p = clazz.newInstance();
                String tableName = p.getTableName();
                List<Class<? extends Entity>> authorizedChildrenClass = p.getAuthorizedChildrenClass();
                createDescription(clazz.getName(), tableName, authorizedChildrenClass);
            } catch (InstantiationException | IllegalAccessException e) {
                mLogger.error(this, "Erreur lors de la recuperation des descriptions.", e);
            }
        }
    }

    /**
     * Cr�ation d'une description de type d'entit�.
     * 
     * @param entityClassName
     *            Nom de la classe d'entit�
     * @param entityTableName
     *            Nom de la table
     * @param authorizedChildrenClass
     *            Liste des type d'enfants
     */
    private void createDescription(final String entityClassName, final String entityTableName,
            final List<Class<? extends Entity>> authorizedChildrenClass) {
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

    /**
     * R�cup�ration du nom de la table.
     * 
     * @param className
     *            Nom du type d'entit� dont on r�cup�re le nom de la table
     * @return Nom de la table
     */
    public final String getTableName(final String className) {
        return mClassToTableName.get(className);
    }

    /**
     * R�cup�ration du nom de la classe.
     * 
     * @param tableName
     *            Nom de la table dont on veut r�cup�rer le nom du type d'entit�
     * @return Nom du type d'entit�
     */
    public final String getClassName(final String tableName) {
        for (String className : mClassToTableName.keySet()) {
            if (mClassToTableName.get(className).equals(tableName)) {
                return className;
            }
        }
        return null;
    }

    /**
     * R�cup�ration de la liste des types d'entit�.
     * 
     * @return La liste des types d'entit�
     */
    public final Set<String> getClasseNames() {
        return mClassToTableName.keySet();
    }

    /**
     * R�cup�ration de la liste des noms de table.
     * 
     * @return La liste des noms de table
     */
    public final Collection<String> getTableNames() {
        return mClassToTableName.values();
    }
}
