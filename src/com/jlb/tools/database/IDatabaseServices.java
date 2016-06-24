package com.jlb.tools.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDatabaseServices {

	public void dropTable(String tableName) throws SQLException;

	public void createTable(String tableName, String attributesDefintion) throws SQLException;

	public void insertData(String tableName, String values) throws SQLException;

	public ResultSet executeSelectFrom(String tableName) throws SQLException;

	public ResultSet executeSelectFromWhere(String tabelName, String whereClause) throws SQLException;

	public void deleteDataWhere(String tableName, String whereClause) throws SQLException;

	public void endService() throws SQLException;
}
