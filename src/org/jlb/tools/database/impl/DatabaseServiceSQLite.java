package org.jlb.tools.database.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jlb.tools.database.IDatabaseServices;
import org.jlb.tools.logging.LogTracer;

/**
 * Classe DatabaseServiceSQLite : Implémentation des services de base de données pour SQLite.
 * 
 * @author JLuc
 *
 */
public class DatabaseServiceSQLite implements IDatabaseServices
{

  /**
   * Connexion à la base.
   */
  private Connection mConnection;

  /**
   * Constructeur.
   * 
   * @param databasePath
   *          Chemin du fichier de base de données
   * @throws ClassNotFoundException
   *           Erreur de chargement de la library sqlite
   * @throws SQLException
   *           Erreur SQL
   * @throws IOException
   *           Erreur d'accés au fichier
   */
  public DatabaseServiceSQLite(final String databasePath) throws ClassNotFoundException, SQLException, IOException
  {
    Class.forName(org.sqlite.JDBC.class.getName());
    File dbFile = new File(databasePath);
    if (!dbFile.exists())
    {
      if (!dbFile.getParentFile().exists())
      {
        if (dbFile.getParentFile().mkdirs())
        {
          LogTracer.getLogger()
              .debug("Chemin de la base de données créé ==> " + dbFile.getParentFile().getAbsolutePath());
          dbFile.createNewFile();
        }
        else
        {
          LogTracer.getLogger()
              .error("Chemin de la base de données NON créé ==> " + dbFile.getParentFile().getAbsolutePath());
        }
      }
    }
    startService(databasePath);
  }

  @Override
  public final void dropTable(final String tableName) throws SQLException
  {
    LogTracer.getLogger().debug("dropTable : " + "drop table if exists " + tableName);
    mConnection.createStatement().execute("drop table if exists " + tableName);
  }

  @Override
  public final void createTable(final String tableName, final String attributesDefinition) throws SQLException
  {
    LogTracer.getLogger().debug("createTable : " + "create table " + tableName + attributesDefinition);
    mConnection.createStatement().executeUpdate("create table " + tableName + attributesDefinition);
  }

  @Override
  public final void insertData(final String tableName, final String sqlColNames, final String sqlValues,
      List<String> types, List<Object> values) throws SQLException
  {
    String sql = "insert into " + tableName + "(" + sqlColNames + ") values(" + sqlValues + ")";

    try
    {
      PreparedStatement pstmt = mConnection.prepareStatement(sql);

      // set the corresponding param
      for (int i = 0; i < values.size(); i++)
      {
        String type = types.get(i);
        Object value = values.get(i);
        switch (type)
        {
          case "string":
            if (value != null)
              // pstmt.setString(i + 1, ((String) value).startsWith("'") ? (String) value : "'" + (String) value + "'");
              pstmt.setString(i + 1, (String) value);
            else
              pstmt.setString(i + 1, null);
            break;
          case "double":
            pstmt.setDouble(i + 1, (double) value);
            break;
          case "integer":
            pstmt.setInt(i + 1, (int) value);
            break;
          default:
            LogTracer.getLogger().error("Type non pris en charge ==> " + type);
        }
      }
      // update
      pstmt.executeUpdate();
    }
    catch (SQLException e)
    {
      LogTracer.getLogger().error("Erreur durant l'insert" + tableName + "... ==> " + sql, e);
    }
  }

  @Override
  public final void updateData(final String tableName, final String sqlSet, String whereClause, List<String> types,
      List<Object> values) throws SQLException
  {
    String sql = "UPDATE " + tableName + " SET " + sqlSet + " WHERE " + whereClause;

    try
    {
      PreparedStatement pstmt = mConnection.prepareStatement(sql);

      // set the corresponding param
      for (int i = 0; i < values.size(); i++)
      {
        String type = types.get(i);
        Object value = values.get(i);
        switch (type)
        {
          case "string":
            if (value != null)
              // pstmt.setString(i + 1, ((String) value).startsWith("'") ? (String) value : "'" + (String) value + "'");
              pstmt.setString(i + 1, (String) value);
            else
              pstmt.setString(i + 1, null);
            break;
          case "double":
            pstmt.setDouble(i + 1, (double) value);
            break;
          case "integer":
            pstmt.setInt(i + 1, (int) value);
            break;
          default:
            LogTracer.getLogger().error("Type non pris en charge ==> " + type);
        }
      }
      // update
      pstmt.executeUpdate();
    }
    catch (SQLException e)
    {
      LogTracer.getLogger().error("Erreur durant l'update" + tableName + "... ==> " + sql, e);
    }
  }

  @Override
  public final ResultSet executeSelectFrom(final String tableName) throws SQLException
  {
    LogTracer.getLogger().debug("executeSelectFrom : " + "select * from " + tableName);
    return mConnection.createStatement().executeQuery("select * from " + tableName);
  }

  @Override
  public final ResultSet executeSelectFromWhere(final String tableName, final String whereClause) throws SQLException
  {
    LogTracer.getLogger().debug("executeSelectFromWhere : " + "select * from " + tableName + " where " + whereClause);
    return mConnection.createStatement().executeQuery("select * from " + tableName + " where " + whereClause);
  }

  @Override
  public final void deleteDataWhere(final String tableName, final String whereClause) throws SQLException
  {
    LogTracer.getLogger().debug("deleteDataWhere : " + "delete from " + tableName + " where " + whereClause);
    mConnection.createStatement().executeUpdate("delete from " + tableName + " where " + whereClause);
  }

  @Override
  public final void endService() throws SQLException
  {
    if (mConnection != null)
    {
      mConnection.close();
    }
  }

  @Override
  public final void startService(String databasePath) throws SQLException
  { // create a database connection
    mConnection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
  }

  @Override
  public void beginTransaction() throws SQLException
  {
    mConnection.setAutoCommit(false);

  }

  @Override
  public void endTransaction() throws SQLException
  {
    mConnection.commit();
  }
}
