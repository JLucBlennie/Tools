package org.jlb.tools.database.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jlb.tools.database.IDatabaseServices;

/**
 * Classe DatabaseServiceSQLite : Implémentation des services de base de données
 * pour SQLite.
 * 
 * @author JLuc
 *
 */
public class DatabaseServiceSQLite implements IDatabaseServices {

	/**
	 * Constante de time out de connexion à la base de données.
	 */
	private static final int TIME_OUT = 30;

	/**
	 * Statement SQL.
	 */
	private final Statement mStatement;

	/**
	 * Connexion à la base.
	 */
	private final Connection mConnection;

	/**
	 * Constructeur.
	 * 
	 * @param databasePath
	 *            Chemin du fichier de base de données
	 * @throws ClassNotFoundException
	 *             Erreur de chargement de la library sqlite
	 * @throws SQLException
	 *             Erreur SQL
	 * @throws IOException
	 *             Erreur d'accés au fichier
	 */
	public DatabaseServiceSQLite(final String databasePath) throws ClassNotFoundException, SQLException, IOException {
		Class.forName("org.sqlite.JDBC");
		File dbFile = new File(databasePath);
		if (!dbFile.exists()) {
			// TODO : Voir comment creer le fichier ainsi que les repertoires
			dbFile.createNewFile();
		}
		// create a database connection
		mConnection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
		mStatement = mConnection.createStatement();
		mStatement.setQueryTimeout(TIME_OUT); // set timeout to 30 sec.
	}

	@Override
	public final void dropTable(final String tableName) throws SQLException {
		mStatement.execute("drop table if exists " + tableName);
	}

	@Override
	public final void createTable(final String tableName, final String attributesDefinition) throws SQLException {
		mStatement.executeUpdate("create table " + tableName + attributesDefinition);
	}

	@Override
	public final void insertData(final String tableName, final String values) throws SQLException {
		mStatement.executeUpdate("insert into " + tableName + " values('" + values + ")");
	}

	@Override
	public final ResultSet executeSelectFrom(final String tableName) throws SQLException {
		return mStatement.executeQuery("select * from " + tableName);
	}

	@Override
	public final ResultSet executeSelectFromWhere(final String tableName, final String whereClause) throws SQLException {
		return mStatement.executeQuery("select * from " + tableName + " where " + whereClause);
	}

	@Override
	public final void deleteDataWhere(final String tableName, final String whereClause) throws SQLException {
		mStatement.executeUpdate("delete from " + tableName + " where " + whereClause);
	}

	@Override
	public final void endService() throws SQLException {
		if (mConnection != null) {
			mConnection.close();
		}
	}
}
