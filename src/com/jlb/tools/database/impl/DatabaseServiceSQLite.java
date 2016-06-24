package com.jlb.tools.database.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jlb.tools.database.IDatabaseServices;

public class DatabaseServiceSQLite implements IDatabaseServices {

	private Statement mStatement;
	private Connection mConnection;

	public DatabaseServiceSQLite(String databasePath) throws ClassNotFoundException, SQLException, IOException {
		Class.forName("org.sqlite.JDBC");
		File dbFile = new File(databasePath);
		if (!dbFile.exists()){
			// TODO : Voir comment creer le fichier ainsi que les repertoires
			dbFile.createNewFile();
		}
		// create a database connection
		mConnection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
		mStatement = mConnection.createStatement();
		mStatement.setQueryTimeout(30); // set timeout to 30 sec.
	}

	@Override
	public void dropTable(String tableName) throws SQLException {
		mStatement.execute("drop table if exists " + tableName);
	}

	@Override
	public void createTable(String tableName, String attributesDefinition) throws SQLException {
		mStatement.executeUpdate("create table " + tableName + attributesDefinition);
	}

	@Override
	public void insertData(String tableName, String values) throws SQLException {
		mStatement.executeUpdate("insert into " + tableName + " values('" + values + ")");
	}

	@Override
	public ResultSet executeSelectFrom(String tableName) throws SQLException {
		return mStatement.executeQuery("select * from " + tableName);
	}

	@Override
	public ResultSet executeSelectFromWhere(String tableName, String whereClause) throws SQLException {
		return mStatement.executeQuery("select * from " + tableName + " where " + whereClause);
	}

	@Override
	public void deleteDataWhere(String tableName, String whereClause) throws SQLException {
		mStatement.executeUpdate("delete from " + tableName + " where " + whereClause);
	}

	@Override
	public void endService() throws SQLException {
		if (mConnection != null) {
			mConnection.close();
		}
	}
}
