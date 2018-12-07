package org.jlb.tools.metamodel;

import java.util.List;

import org.jlb.tools.metamodel.criterion.ICriterion;

/**
 * Interface IDataProcessorServices : Services d'accès à la base de données.
 * 
 * @author JLuc
 *
 */
public interface IDataProcessorServices {

	/**
	 * Création de la base de données.
	 */
	void createDatabase();

	/**
	 * Sauvegarde des entités.
	 * 
	 * @param entities
	 *            Liste des entités à sauvegarder
	 */
	void storeEntities(List<Entity> entities);

	/**
	 * Requéte sur les entités.
	 * 
	 * @param criterion
	 *            Critére de requéte
	 * @return La liste des entités résultat
	 */
	List<Entity> requestEntities(ICriterion criterion);

	/**
	 * Requéte sur les liens.
	 * 
	 * @param entity
	 *            Entité du lien
	 * @return Liste des liens
	 */
	List<Link> requestLinks(Entity entity);

	/**
	 * Suppression des entités.
	 * 
	 * @param entities
	 *            Liste des entités à supprimer
	 */
	void deleteEntities(List<Entity> entities);

	/**
	 * Suppression des liens.
	 * 
	 * @param links
	 *            Liste de liens à supprimer
	 */
	void deleteLinks(List<Link> links);

	/**
	 * Fermeture du service de base de données.
	 */
	void endDatabaseService();
}
