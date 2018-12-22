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
	 * Id de l'Entité source.
	 */
	private final String mSourceId;

	/**
	 * Nom de Table de la source.
	 */
	private final String mSourceTableName;

	/**
	 * Entité cible.
	 */
	private final String mDestinationId;

	/**
	 * Nom de table de la destination.
	 */
	private final String mDestinationTableName;

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
		mSourceId = src.getId();
		mDestinationId = dest.getId();
		mSourceTableName = src.getTableName();
		mDestinationTableName = dest.getTableName();
	}

	/**
	 * Récupére l'Id de l'entité source.
	 * 
	 * @return l'Id de l'entité source
	 */
	public final String getSource() {
		return mSourceId;
	}

	/**
	 * Récupéré le nom de table de la source.
	 * 
	 * @return nom de table.
	 */
	public final String getSourceTableName() {
		return mSourceTableName;
	}

	/**
	 * Récupére l'Id de l'entité cible.
	 * 
	 * @return l'Id de l'entité cible
	 */
	public final String getDestination() {
		return mDestinationId;
	}

	/**
	 * Récupéré le nom de table de la destnation.
	 * 
	 * @return nom de table.
	 */
	public final String getDestinationTableName() {
		return mDestinationTableName;
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
		return "Link " + mName + " - " + mSourceId + " --> " + mDestinationId;
	}
}
