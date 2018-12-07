package org.jlb.tools.metamodel;

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
	private final String mName;

	/**
	 * Entité source.
	 */
	private final Entity mSource;

	/**
	 * Entité cible.
	 */
	private final Entity mDestination;

	/**
	 * Constructeur.
	 * 
	 * @param src
	 *            Entité source
	 * @param dest
	 *            Entité cible
	 */
	public Link(final Entity src, final Entity dest) {
		mName = "Link" + src.getClass().getSimpleName() + dest.getClass().getSimpleName();
		mSource = src;
		mDestination = dest;
	}

	/**
	 * Récupére l'entité source.
	 * 
	 * @return l'entité source
	 */
	public final Entity getSource() {
		return mSource;
	}

	/**
	 * Récupére l'entité cible.
	 * 
	 * @return l'entité cible
	 */
	public final Entity getDestination() {
		return mDestination;
	}

	/**
	 * Récupération du nom du lien.
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
