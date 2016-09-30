package com.jlb.tools.metamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.jlb.tools.metamodel.attributes.IAttribute;

/**
 * Classe abstraite Entity : base d'une Entit�.
 * 
 * @author JLuc
 *
 */
public abstract class Entity {

    /**
     * Dictionnaire de ressources.
     */
    protected static final ResourceBundle DICO_PROPERTIES = ResourceBundle.getBundle("resources/dico",
            Locale.getDefault());

    /**
     * Nom de la table.
     */
    private String mTableName;

    /**
     * Liste de Type d'entit�s filles autoris�es.
     */
    private List<Class<? extends Entity>> mAuthorizedChildrenClass = new ArrayList<Class<? extends Entity>>();

    /**
     * Liste des attributs.
     */
    private List<IAttribute> mAttributes = new ArrayList<IAttribute>();

    /**
     * Liste des enfants.
     */
    private List<Entity> mChildren = new ArrayList<Entity>();

    /**
     * Liste des liens.
     */
    private List<Link> mLinks = new ArrayList<Link>();

    /**
     * Parent de l'entit�.
     */
    private Entity mParent;

    /**
     * Identifiant de l'entit�.
     */
    private int mId;

    /**
     * Constructeur.
     */
    public Entity() {
        mTableName = this.getClass().getSimpleName();
    }

    /**
     * Constructeur sur l'Idantifiant.
     * 
     * @param id
     *            Identifiant.
     */
    public Entity(final int id) {
        mId = id;
        mTableName = this.getClass().getSimpleName();
    }

    /**
     * R�cup�ration de la liste des types d'entit� fille.
     * 
     * @return La liste des types d'entit� fille
     */
    public final List<Class<? extends Entity>> getAuthorizedChildrenClass() {
        return mAuthorizedChildrenClass;
    }

    /**
     * R�cup�ration du nom de la table.
     * 
     * @return Nom de la table
     */
    public final String getTableName() {
        return mTableName;
    }

    /**
     * Mise � jour du parent.
     * 
     * @param parent
     *            Parent de l'entit�
     */
    public final void setParent(final Entity parent) {
        this.mParent = parent;
    }

    /**
     * R�cup�ration du parent.
     * 
     * @return le parent
     */
    public final Entity getParent() {
        return mParent;
    }

    /**
     * R�cup�ration de l'idenfiant.
     * 
     * @return Identifiant
     */
    public final int getId() {
        return this.mId;
    }

    /**
     * Mise � jour de l'identifiant.
     * 
     * @param id
     *            Nouvel identifiant
     */
    public final void setId(final int id) {
        this.mId = id;
    }

    /**
     * R�cup�ration des attributs.
     * 
     * @return Liste des attributs
     */
    public final List<IAttribute> getAttributes() {
        return mAttributes;
    }

    /**
     * R�cup�ration de l'attribut � partir de son nom.
     * 
     * @param attrName
     *            Nom de l'attribut � r�cup�rer
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
     * R�cup�ration des enfants du m�me type.
     * 
     * @param className
     *            Nom du type
     * @return La liste des enfants du m�me type
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
     * R�cup�ration du fils d'un type et d'un identifiant.
     * 
     * @param className
     *            Type d'enfant
     * @param id
     *            Identifiant
     * @return l'entit� fille
     */
    // TODO : Voir l'int�r�t de passer le nom du type d'entit�
    public final Entity getChildOfType(final String className, final int id) {
        for (Entity child : getChildrenOfType(className)) {
            if (child.getId() == id) {
                return child;
            }
        }

        // Si pas de resultat
        return null;
    }

    /**
     * R�cup�ration des enfants.
     * 
     * @return La liste des enfants
     */
    public final List<Entity> getChildren() {
        return mChildren;
    }

    /**
     * R�cup�ration du lien pour la destination.
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
     * R�cup�ration des liens.
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
