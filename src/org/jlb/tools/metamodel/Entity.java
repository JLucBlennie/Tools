package org.jlb.tools.metamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

import org.jlb.tools.metamodel.attributes.IAttribute;

/**
 * Classe abstraite Entity : base d'une Entité.
 * 
 * @author JLuc
 *
 */
public abstract class Entity {

	/**
	 * Dictionnaire de ressources.
	 */
	protected static final ResourceBundle DICO_PROPERTIES = ResourceBundle.getBundle("resources/dico", Locale.getDefault());

	/**
	 * Nom de la table.
	 */
	protected String mTableName;

	/**
	 * Liste de Type d'entités filles autorisées.
	 */
	protected List<Class<? extends Entity>> mAuthorizedChildrenClass = new ArrayList<Class<? extends Entity>>();

	/**
	 * Liste des attributs.
	 */
	protected List<IAttribute> mAttributes = new ArrayList<IAttribute>();

	/**
	 * Liste des enfants.
	 */
	protected List<Entity> mChildren = new ArrayList<Entity>();

	/**
	 * Liste des liens.
	 */
	protected List<Link> mLinks = new ArrayList<Link>();

	/**
	 * Parent de l'entité.
	 */
	protected Entity mParent;

	/**
	 * Identifiant de l'entité.
	 */
	protected String mId;

	/**
	 * Constructeur.
	 */
	public Entity() {
		mId = UUID.randomUUID().toString();
		mTableName = this.getClass().getSimpleName();
	}

	/**
	 * Constructeur sur l'Idantifiant.
	 * 
	 * @param id
	 *            Identifiant.
	 */
	public Entity(final String id) {
		mId = id;
		mTableName = this.getClass().getSimpleName();
	}

	/**
	 * Récupération de la liste des types d'entité fille.
	 * 
	 * @return La liste des types d'entité fille
	 */
	public final List<Class<? extends Entity>> getAuthorizedChildrenClass() {
		return mAuthorizedChildrenClass;
	}

	/**
	 * Récupération du nom de la table.
	 * 
	 * @return Nom de la table
	 */
	public final String getTableName() {
		return mTableName;
	}

	/**
	 * Mise à jour du parent.
	 * 
	 * @param parent
	 *            Parent de l'entité
	 */
	public final void setParent(final Entity parent) {
		this.mParent = parent;
	}

	/**
	 * Récupération du parent.
	 * 
	 * @return le parent
	 */
	public final Entity getParent() {
		return mParent;
	}

	/**
	 * Récupération de l'idenfiant.
	 * 
	 * @return Identifiant
	 */
	public final String getId() {
		return this.mId;
	}

	/**
	 * Mise à jour de l'identifiant.
	 * 
	 * @param id
	 *            Nouvel identifiant
	 */
	public final void setId(final String id) {
		this.mId = id;
	}

	/**
	 * Récupération des attributs.
	 * 
	 * @return Liste des attributs
	 */
	public final List<IAttribute> getAttributes() {
		return mAttributes;
	}

	/**
	 * Récupération de l'attribut à partir de son nom.
	 * 
	 * @param attrName
	 *            Nom de l'attribut à récupérer
	 * @return L'attribut
	 */
	public final IAttribute getAttribute(final String attrName) {
		IAttribute resAttr = null;
		for (IAttribute attribute : mAttributes) {
			if (attribute.getName().equals(attrName)) {
				resAttr = attribute;
			}
		}
		return resAttr;
	}

	/**
	 * Récupération des enfants du même type.
	 * 
	 * @param className
	 *            Nom du type
	 * @return La liste des enfants du même type
	 */
	public final List<Entity> getChildrenOfType(final String className) {
		List<Entity> results = new ArrayList<Entity>();
		for (Entity child : mChildren) {
			if (child.getClass().getName().equals(className) && !results.contains(child)) {
				results.add(child);
			}
		}
		return results;
	}

	/**
	 * Récupération du fils d'un type et d'un identifiant.
	 * 
	 * @param className
	 *            Type d'enfant
	 * @param id
	 *            Identifiant
	 * @return l'entité fille
	 */
	// TODO : Voir l'intérêt de passer le nom du type d'entité
	public final Entity getChildOfType(final String className, final String id) {
		for (Entity child : getChildrenOfType(className)) {
			if (child.getId() == id) { return child; }
		}

		// Si pas de resultat
		return null;
	}

	/**
	 * Récupération des enfants.
	 * 
	 * @return La liste des enfants
	 */
	public final List<Entity> getChildren() {
		return mChildren;
	}

	/**
	 * Récupération du lien pour la destination.
	 * 
	 * @param dest
	 *            Destination du lien
	 * @return Le lien
	 */
	private Link getLink(final Entity dest) {
		Link lnk = null;

		for (Link l : mLinks) {
			if (l.getDestination().equals(dest)) {
				lnk = l;
				break;
			}
		}

		return lnk;
	}

	/**
	 * Récupération des liens dont la destination est du type className.
	 * 
	 * @param className
	 *            Type de la Destination du lien
	 * @return La liste des lien dont la destination est du type className
	 */
	public final List<Link> getLinksWithDestinationOfType(final String className) {
		List<Link> linksOfType = new ArrayList<Link>();
		for (Link l : mLinks) {
			if (className.contains(l.getDestinationTableName())) {
				linksOfType.add(l);
			}
		}

		return linksOfType;
	}

	/**
	 * Récupération des liens.
	 * 
	 * @return La liste des liens
	 */
	public final List<Link> getLinks() {
		return mLinks;
	}

	/**
	 * Suppression du lien pour la destination.
	 * 
	 * @param dest
	 *            Destination du lien
	 */
	public final void removeLink(final Entity dest) {
		mLinks.remove(getLink(dest));
	}

	/**
	 * Ajout d'un lien pour la destination.
	 * 
	 * @param dest
	 *            Destination du lien
	 */
	public final void addLink(final Entity dest) {
		if (getLink(dest) == null) {
			mLinks.add(new Link(this, dest));
		}
	}
}
