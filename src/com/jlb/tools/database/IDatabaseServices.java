package com.jlb.tools.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface IDatabaseServices : Services de base de données.
 * 
 * @author JLuc
 *
 */
public interface IDatabaseServices {

    /**
     * Suppression d'une table.
     * 
     * @param tableName
     *            Nom de la table
     * @throws SQLException
     *             Erreur SQL
     */
    void dropTable(String tableName) throws SQLException;

    /**
     * Création d'une table.
     * 
     * @param tableName
     *            Nom de la table
     * @param attributesDefintion
     *            Liste des définitions des attributs
     * @throws SQLException
     *             Erreur SQL
     */
    void createTable(String tableName, String attributesDefintion) throws SQLException;

    /**
     * Ajout de données dans une table.
     * 
     * @param tableName
     *            Nom de la table
     * @param values
     *            Valeurs
     * @throws SQLException
     *             Erreur SQL
     */
    void insertData(String tableName, String values) throws SQLException;

    /**
     * Requète sur une table.
     * 
     * @param tableName
     *            Nom de la table
     * @return Le résultat de la requète
     * @throws SQLException
     *             Erreur SQL
     */
    ResultSet executeSelectFrom(String tableName) throws SQLException;

    /**
     * Requète sur une table avec critères.
     * 
     * @param tabelName
     *            Nom de la table
     * @param whereClause
     *            Clause de critères
     * @return Le résultat de la requète
     * @throws SQLException
     *             Erreur SQL
     */
    ResultSet executeSelectFromWhere(String tabelName, String whereClause) throws SQLException;

    /**
     * Suppression de données dans une table avec critères.
     * 
     * @param tableName
     *            Nom de la table
     * @param whereClause
     *            Clause de critères
     * @throws SQLException
     *             Erreur SQL
     */
    void deleteDataWhere(String tableName, String whereClause) throws SQLException;

    /**
     * Fin du service de base de données.
     * 
     * @throws SQLException
     *             Erreur SQL
     */
    void endService() throws SQLException;
}
