package org.jlb.tools.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface IDatabaseServices : Services de base de données.
 * 
 * @author JLuc
 *
 */
public interface IDatabaseServices
{

  /**
   * Suppression d'une table.
   * 
   * @param tableName
   *          Nom de la table
   * @throws SQLException
   *           Erreur SQL
   */
  void dropTable(String tableName) throws SQLException;

  /**
   * Création d'une table.
   * 
   * @param tableName
   *          Nom de la table
   * @param attributesDefintion
   *          Liste des définitions des attributs
   * @throws SQLException
   *           Erreur SQL
   */
  void createTable(String tableName, String attributesDefintion) throws SQLException;

  /**
   * Ajout de données dans une table.
   * 
   * @param tableName
   *          Nom de la table
   * @param values
   *          Valeurs
   * @throws SQLException
   *           Erreur SQL
   */
  void insertData(String tableName, String sqlColNames, final String sqlValues, List<String> types, List<Object> values)
      throws SQLException;

  /**
   * Mise à jour de données dans une table.
   * 
   * @param tableName
   *          Nom de la table
   * @param values
   *          Valeurs
   * @param whereClause
   *          Clause de critères
   * @throws SQLException
   *           Erreur SQL
   */
  void updateData(String tableName, String sqlSet, String whereClause, List<String> types, List<Object> values)
      throws SQLException;

  /**
   * Requéte sur une table.
   * 
   * @param tableName
   *          Nom de la table
   * @return Le résultat de la requéte
   * @throws SQLException
   *           Erreur SQL
   */
  ResultSet executeSelectFrom(String tableName) throws SQLException;

  /**
   * Requéte sur une table avec critéres.
   * 
   * @param tabelName
   *          Nom de la table
   * @param whereClause
   *          Clause de critéres
   * @return Le résultat de la requéte
   * @throws SQLException
   *           Erreur SQL
   */
  ResultSet executeSelectFromWhere(String tabelName, String whereClause) throws SQLException;

  /**
   * Suppression de données dans une table avec critéres.
   * 
   * @param tableName
   *          Nom de la table
   * @param whereClause
   *          Clause de critéres
   * @throws SQLException
   *           Erreur SQL
   */
  void deleteDataWhere(String tableName, String whereClause) throws SQLException;

  /**
   * Fin du service de base de données.
   * 
   * @throws SQLException
   *           Erreur SQL
   */
  void endService() throws SQLException;

  /**
   * Démarrage du service de base de données.
   * 
   * @param databasePath
   *          Chemin de la base de données
   * @throws SQLException
   *           Erreur SQL
   */
  void startService(String databasePath) throws SQLException;

  /**
   * Début de transaction. Permet de stocker en memoire les requetes.
   */
  void beginTransaction() throws SQLException;

  /**
   * Fin de transaction. Permet de flusher les requetes en memoire en BDD.
   */
  void endTransaction() throws SQLException;
}
