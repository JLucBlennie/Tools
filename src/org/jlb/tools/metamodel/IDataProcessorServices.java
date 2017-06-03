package com.jlb.tools.metamodel;

import java.util.List;

import com.jlb.tools.metamodel.criterion.ICriterion;

/**
 * Interface IDataProcessorServices : Services d'acc�s � la base de donn�es.
 * 
 * @author JLuc
 *
 */
public interface IDataProcessorServices {

    /**
     * Cr�ation de la base de donn�es.
     */
    void createDatabase();

    /**
     * Sauvegarde des entit�s.
     * 
     * @param entities
     *            Liste des entit�s � sauvegarder
     */
    void storeEntities(List<Entity> entities);

    /**
     * Requ�te sur les entit�s.
     * 
     * @param criterion
     *            Crit�re de requ�te
     * @return La liste des entit�s r�sultat
     */
    List<Entity> requestEntities(ICriterion criterion);

    /**
     * Requ�te sur les liens.
     * 
     * @param entity
     *            Entit� du lien
     * @return Liste des liens
     */
    List<Link> requestLinks(Entity entity);

    /**
     * Suppression des entit�s.
     * 
     * @param entities
     *            Liste des entit�s � supprimer
     */
    void deleteEntities(List<Entity> entities);

    /**
     * Suppression des liens.
     * 
     * @param links
     *            Liste de liens � supprimer
     */
    void deleteLinks(List<Link> links);

    /**
     * Fermeture du service de base de donn�es.
     */
    void endDatabaseService();
}
