package com.jlb.tools.metamodel;

/**
 * Classe Link : Description d'un lien.
 * 
 * @author JLuc
 *
 */
public class Link {

    /**
     * Nom du lien.
     */
    private String mName;

    /**
     * Entit� source.
     */
    private Entity mSource;

    /**
     * Entit� cible.
     */
    private Entity mDestination;

    /**
     * Constructeur.
     * 
     * @param src
     *            Entit� source
     * @param dest
     *            Entit� cible
     */
    public Link(final Entity src, final Entity dest) {
        mName = "Link" + src.getClass().getSimpleName() + dest.getClass().getSimpleName();
        mSource = src;
        mDestination = dest;
    }

    /**
     * R�cup�re l'entit� source.
     * 
     * @return l'entit� source
     */
    public final Entity getSource() {
        return mSource;
    }

    /**
     * R�cup�re l'entit� cible.
     * 
     * @return l'entit� cible
     */
    public final Entity getDestination() {
        return mDestination;
    }

    /**
     * R�cup�ration du nom du lien.
     * 
     * @return le nom du lien
     */
    public final String getName() {
        return mName;
    }

    @Override
    public final String toString() {
        return "Link " + mName + " - " + mSource + " --> " + mDestination;
    }
}
