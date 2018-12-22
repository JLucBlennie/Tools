package org.jlb.tools.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jlb.tools.database.IDatabaseServices;
import org.jlb.tools.logging.LogTracer;
import org.jlb.tools.metamodel.attributes.IAttribute;
import org.jlb.tools.metamodel.criterion.ICriterion;
import org.jlb.tools.metamodel.criterion.Operator;
import org.jlb.tools.metamodel.criterion.impl.AllCriterion;
import org.jlb.tools.metamodel.criterion.impl.StringCriterion;

/**
 * Classe decrivant le modele de donnees Elle permet aussi d'acceder aux
 * donnees.
 * 
 * @author JLuc
 *
 */
public abstract class DataProcessorServices implements IDataProcessorServices {

	/**
	 * Liste des classes derivees de Entity.
	 */
	protected static List<Class<? extends Entity>> mClazzs = new ArrayList<Class<? extends Entity>>();

	/**
	 * Description.
	 */
	protected Description mDescription;

	/**
	 * Service de base de donnees.
	 */
	protected IDatabaseServices mDatabaseServices;

	/**
	 * Cache des Entites.
	 * <p/>
	 * Il faut garder en memoire les objets charges lors des requetes pour ne
	 * pas creer les objets plusieurs fois... Gestion des Entities chargees, la
	 * clef est le nom de la table (nom simple de la classe) et l'Id
	 */
	protected Map<String, Entity> mCacheEntities = new HashMap<String, Entity>();

	/**
	 * Cache des Liens.
	 * <p/>
	 * Gestion du chache des liens, la clef est le nom de la table + id de la
	 * source et le nom de la table et l'Id de la destination.
	 */
	protected Map<String, Link> mCacheLinks = new HashMap<String, Link>();

	/**
	 * Constructeur.
	 * 
	 * @param logger
	 *            Service de log
	 */
	public DataProcessorServices() {
		mDescription = new Description(mClazzs);
	}

	/**
	 * Ajout de classe dans la liste des classes a gerer.
	 * 
	 * @param classToAdd
	 *            Classe a ajouter
	 * 
	 */
	public static void addClass(final Class<? extends Entity> classToAdd) {
		if (!mClazzs.contains(classToAdd)) {
			mClazzs.add(classToAdd);
		}
	}

	/**
	 * Mise a jour des Services de base de donnees.
	 * 
	 * @param databaseServices
	 *            Service de base de donnees
	 */
	public final void setDatabaseServices(final IDatabaseServices databaseServices) {
		mDatabaseServices = databaseServices;
	}

	/**
	 * Accesseur sur la description.
	 * 
	 * @return la description
	 */
	public final Description getDescription() {
		return mDescription;
	}

	/**
	 * Creation d'une entite.
	 * 
	 * @param className
	 *            Nom de la classe
	 * @param rs
	 *            resultat de la requete
	 * @return L'entite creee
	 * @throws SQLException
	 *             Erreur de requete
	 */
	protected abstract Entity createEntity(String className, ResultSet rs) throws SQLException;

	/**
	 * Creation de lien.
	 * 
	 * @param src
	 *            Entite source
	 * @param dest
	 *            Entite destination
	 * @return Le lien entre source et destination
	 */
	protected final Link createLink(final Entity src, final Entity dest) {
		return new Link(src, dest);
	}

	@Override
	public final void createDatabase() {
		for (String className : mDescription.getClasseNames()) {
			Entity entityDesc = null;
			try {
				Class<Entity> clazz = (Class<Entity>) ClassLoader.getSystemClassLoader().loadClass(className);
				entityDesc = clazz.newInstance();
				LogTracer.getLogger().info("Creation du schema de la base de donnees " + entityDesc.getTableName());
				createTable(entityDesc);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				LogTracer.getLogger().error("Erreur lors de la creation de la table " + ((entityDesc != null) ? entityDesc.getTableName() : "de description nulle."), e);
			}
		}
		// On s'occupe des Liens
		LogTracer.getLogger().info("Suppression de la table des lien");
		try {
			mDatabaseServices.dropTable("Link");

			LogTracer.getLogger().info("Creation de la table des liens");
			String attrs = " (idSrc integer,typeSrc string,idDest integer,typeDest string)";
			mDatabaseServices.createTable("Link", attrs);
		} catch (SQLException e) {
			LogTracer.getLogger().error("Erreur lors de la creation de la table des liens", e);
		}
	}

	@Override
	public final void storeEntities(final List<Entity> entities) {
		for (Entity obj : entities) {
			try {
				String tableName = mDescription.getTableName(obj.getClass().getName());
				String parentTableName = obj.getParent() != null ? mDescription.getTableName(obj.getParent().getClass().getName()) : "";
				String attributes = "";
				for (int i = 0; i < obj.getAttributes().size(); i++) {
					IAttribute attr = obj.getAttributes().get(i);
					attributes += "'" + attr.getValue() + "'" + ((i < obj.getAttributes().size() - 1) ? "," : "");
				}
				LogTracer.getLogger().info("Recuperation des attributs de l'objet " + obj + " -> " + attributes);

				LogTracer.getLogger().info("Persistence des attributs de l'objet " + obj);
				String values = obj.getId() + "','" + (obj.getParent() != null ? obj.getParent().getId() : -1) + "','" + parentTableName + "','" + obj.getChildren().size() + (attributes.isEmpty() ? "'" : "'," + attributes);
				mDatabaseServices.insertData(tableName, values);
				// On s'occupe des fils
				LogTracer.getLogger().info("Persistence des fils de l'objet " + obj);
				storeEntities(obj.getChildren());

				// On ajoute l'entity dans le cache
				mCacheEntities.put(obj.getTableName() + "_" + obj.getId(), obj);

				// On s'occupe des liens
				for (Link lnk : obj.getLinks()) {
					String valuesLink = lnk.getSource() + "','" + lnk.getSourceTableName() + "','" + lnk.getDestination() + "','" + lnk.getDestinationTableName();
					mDatabaseServices.insertData("Link", valuesLink);
					// On ajoute le lien dans le cache
					mCacheLinks.put(lnk.getSourceTableName() + "_" + lnk.getSource() + "_" + lnk.getDestinationTableName() + "_" + lnk.getDestination(), lnk);
				}

				LogTracer.getLogger().info("Persistence des liens de l'objet " + obj);
			} catch (SQLException e) {
				LogTracer.getLogger().error("Erreur lors de la persistence de l'objet " + obj, e);
			}
		}
	}

	@Override
	public final List<Entity> requestEntities(final ICriterion criterion) {
		List<Entity> result = new ArrayList<Entity>();
		String tableName = criterion.getTableName();
		String attrName = criterion.getAttributeName();

		// On cherche dans le cache
		// TODO : Gerer le cas d'un critere ALLCriterion.
		if (criterion instanceof AllCriterion) {
			result = getEntitiesOfTypeFromCache(criterion.getTableName());
		} else {
			for (String key : mCacheEntities.keySet()) {
				if (key.contains(tableName)) {
					Entity ent = mCacheEntities.get(key);
					if (criterion.getOperator() == Operator.EQUALS) {
						if (attrName.equalsIgnoreCase("Id")) {
							if (ent.getId().equals(criterion.getValue())) {
								result.add(ent);
							}
						} else if (attrName.equalsIgnoreCase("idParent")) {
							if (ent.getParent().getId().equals(criterion.getValue())) {
								result.add(ent);
							}
						} else if (attrName.equals("typeParent")) {
							if (ent.getParent().getTableName().equals(criterion.getValue())) {
								result.add(ent);
							}
						} else if (ent.getAttribute(attrName).getValue() == criterion.getValue()) {
							result.add(ent);
						}
					} else if (criterion.getOperator() == Operator.GREATER) {
						if (attrName.equalsIgnoreCase("Id")) {
							if (ent.getId().compareTo((String) criterion.getValue()) > 0) {
								result.add(ent);
							}
						} else if (attrName.equalsIgnoreCase("idParent")) {
							if (ent.getParent().getId().compareTo((String) criterion.getValue()) > 0) {
								result.add(ent);
							}
						} else if (attrName.equals("typeParent")) {
							if (ent.getParent().getTableName().contains((String) criterion.getValue())) {
								result.add(ent);
							}
						} else if (ent.getAttribute(attrName).getType().equalsIgnoreCase("integer")) {
							if ((int) ent.getAttribute(attrName).getValue() > (int) criterion.getValue()) {
								result.add(ent);
							}
						} else if (ent.getAttribute(attrName).getType().equalsIgnoreCase("double")) {
							if ((double) ent.getAttribute(attrName).getValue() > (double) criterion.getValue()) {
								result.add(ent);
							}
						} else {
							LogTracer.getLogger().error("Le critere de recherche n'est pas pris en compte " + criterion, null);
						}
					} else if (criterion.getOperator() == Operator.LOWER) {
						if (attrName.equalsIgnoreCase("Id")) {
							if (ent.getId().compareTo((String) criterion.getValue()) < 0) {
								result.add(ent);
							}
						} else if (attrName.equalsIgnoreCase("idParent")) {
							if (ent.getParent().getId().compareTo((String) criterion.getValue()) < 0) {
								result.add(ent);
							}
						} else if (attrName.equals("typeParent")) {
							if (ent.getParent().getTableName().contains((String) criterion.getValue())) {
								result.add(ent);
							}
						} else if (ent.getAttribute(attrName).getType().equalsIgnoreCase("integer")) {
							if ((int) ent.getAttribute(attrName).getValue() < (int) criterion.getValue()) {
								result.add(ent);
							}
						} else if (ent.getAttribute(attrName).getType().equalsIgnoreCase("double")) {
							if ((double) ent.getAttribute(attrName).getValue() < (double) criterion.getValue()) {
								result.add(ent);
							}
						} else {
							LogTracer.getLogger().error("Le critere de recherche n'est pas pris en compte " + criterion, null);
						}
					}
				}
			}
		}

		// on recherche en base
		ResultSet rs;
		try {
			if (attrName == null) {
				rs = mDatabaseServices.executeSelectFrom(tableName);
			} else {
				rs = mDatabaseServices.executeSelectFromWhere(tableName, attrName + " " + criterion.getOperator() + " '" + criterion.getValue() + "'");
			}

			Class<Entity> tableClass = (Class<Entity>) Class.forName(mDescription.getClassName(tableName));
			Entity entity = null;
			while (rs.next()) {
				String tableClassName = tableClass.getName();
				entity = createEntity(tableClassName, rs);
				if (!mCacheEntities.containsKey(entity.getTableName() + "_" + entity.getId())) {
					mCacheEntities.put(entity.getTableName() + "_" + entity.getId(), entity);
					result.add(entity);
					// On doit s'occuper des fils
					if (rs.getInt("nbFils") > 0) {
						for (Class childClass : entity.getAuthorizedChildrenClass()) {
							ICriterion<String> childrenIdCriterion = new StringCriterion(mDescription.getTableName(childClass.getName()), "idParent", Operator.EQUALS, entity.getId());
							for (Entity ent : requestEntities(childrenIdCriterion)) {
								ent.setParent(entity);
								entity.getChildren().add(ent);
							}
						}
					}
				}
			}
		} catch (SQLException | ClassNotFoundException | SecurityException e) {
			LogTracer.getLogger().error("Erreur lors de la requete " + criterion, e);
		}
		return result;
	}

	/**
	 * Recupere les entites d'un meme type depuis le cache.
	 * 
	 * @param tableName
	 *            Nom du type d'entite
	 * @return la liste des entites
	 */
	private List<Entity> getEntitiesOfTypeFromCache(final String tableName) {
		List<Entity> result = new ArrayList<Entity>();
		for (String key : mCacheEntities.keySet()) {
			if (key.contains(tableName)) {
				result.add(mCacheEntities.get(key));
			}
		}
		return result;
	}

	@Override
	public final List<Link> requestLinks(final Entity entity) {
		List<Link> links = new ArrayList<Link>();
		// On cherche dans le cache des liens.
		String idEntity = entity.getId();
		String entityTableName = entity.getTableName();
		for (String linkId : mCacheLinks.keySet()) {
			if (linkId.contains(entityTableName + "_" + idEntity)) {
				links.add(mCacheLinks.get(linkId));
			}
		}

		// Recuperer les liens qui ont pour source ou destination l'entite src
		try {
			ResultSet rs = mDatabaseServices.executeSelectFromWhere("Link", "idSrc='" + entity.getId() + "' or  idDest='" + entity.getId() + "'");
			while (rs.next()) {
				// On recherche l'entite destination
				String idSrc = rs.getString("idSrc");
				String typeSrc = rs.getString("typeSrc");
				String idDest = rs.getString("idDest");
				String typeDest = rs.getString("typeDest");
				if (idSrc.equals(entity.getId())) {
					Class<Entity> tableClass = (Class<Entity>) Class.forName(mDescription.getClassName(typeDest));
					ICriterion criterionDest = new StringCriterion(typeDest, "Id", Operator.EQUALS, idDest);
					List<Entity> listDest = requestEntities(criterionDest);
					for (Entity dest : listDest) {
						links.add(new Link(entity, dest));
					}
				} else if (idDest.equals(entity.getId())) {
					Class<Entity> tableClass = (Class<Entity>) Class.forName(mDescription.getClassName(typeSrc));
					ICriterion criterion = new StringCriterion(typeSrc, "Id", Operator.EQUALS, idSrc);
					List<Entity> listEntity = requestEntities(criterion);
					for (Entity src : listEntity) {
						links.add(new Link(src, entity));
					}
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			LogTracer.getLogger().error("Erreur lors de la requete sur les liens depuis " + entity, e);
		}
		return links;
	}

	@Override
	public final void deleteEntities(final List<Entity> objects) {
		for (Entity obj : objects) {
			try {
				mDatabaseServices.deleteDataWhere(mDescription.getTableName(obj.getClass().getName()), "Id='" + obj.getId() + "'");
				// On doit s'occuper des fils
				if (obj.getChildren().size() > 0) {
					for (Class childClass : obj.getAuthorizedChildrenClass()) {
						ICriterion<String> childrenIdCriterion = new StringCriterion(mDescription.getTableName(childClass.getName()), "idParent", Operator.EQUALS, obj.getId());
						deleteEntities(requestEntities(childrenIdCriterion));
					}
				}
				mCacheEntities.remove(obj.getTableName() + "_" + obj.getId());

				// On doit s'occuper des liens
				// Recuperation des liens qui pointent obj
				List<Link> links = requestLinks(obj);
				deleteLinks(links);
			} catch (SQLException e) {
				LogTracer.getLogger().error("Erreur lors de la suppression de l'objet " + obj, e);
			}
		}
	}

	@Override
	public final void deleteLinks(final List<Link> links) {
		for (Link link : links) {
			try {
				mDatabaseServices.deleteDataWhere("Link", "idSrc='" + link.getSource() + "' and idDest='" + link.getDestination() + "'");
				mCacheLinks.remove(link.getSourceTableName() + "_" + link.getSource() + "_" + link.getDestinationTableName() + "_" + link.getDestination());
			} catch (SQLException e) {
				LogTracer.getLogger().error("Erreur lors de la suppression du lien " + link, e);
			}
		}
	}

	/**
	 * creation d'une table a partir de la description d'un type d'entite.
	 * 
	 * @param tableDesc
	 *            Description du type d'entite
	 */
	private void createTable(final Entity tableDesc) {
		String tableName = mDescription.getTableName(tableDesc.getClass().getName());
		try {
			// Recuperation des attributs
			String attributes = "";
			for (int i = 0; i < tableDesc.getAttributes().size(); i++) {
				IAttribute attr = tableDesc.getAttributes().get(i);
				attributes += attr.getName() + " " + attr.getType().toLowerCase() + ((i < tableDesc.getAttributes().size() - 1) ? "," : "");
			}
			LogTracer.getLogger().info("Recuperation des attributs de " + tableName + " -> " + attributes);

			LogTracer.getLogger().info("Suppression de la table " + tableName);
			mDatabaseServices.dropTable(tableName);

			LogTracer.getLogger().info("Creation de la table " + tableName);
			String attrs = " (Id integer,idParent integer,typeParent string,nbFils integer" + (attributes.isEmpty() ? "" : "," + attributes) + ")";
			mDatabaseServices.createTable(tableName, attrs);

		} catch (SQLException e) {
			LogTracer.getLogger().error("Erreur lors de la creation de la table " + tableName, e);
		}
	}

	@Override
	public final void endDatabaseService() {
		try {
			LogTracer.getLogger().info("Fin du service de base de donnees");
			mDatabaseServices.endService();
		} catch (SQLException e) {
			LogTracer.getLogger().error("Erreur lors de la fermerture de la base de donnee", e);
		}
	}
}
