package com.jlb.tools.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface IDatabaseServices : Services de base de donn�es.
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
     * Cr�ation d'une table.
     * 
     * @param tableName
     *            Nom de la table
     * @param attributesDefintion
     *            Liste des d�finitions des attributs
     * @throws SQLException
     *             Erreur SQL
     */
    void createTable(String tableName, String attributesDefintion) throws SQLException;

    /**
     * Ajout de donn�es dans une table.
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
     * Requ�te sur une table.
     * 
     * @param tableName
     *            Nom de la table
     * @return Le r�sultat de la requ�te
     * @throws SQLException
     *             Erreur SQL
     */
    ResultSet executeSelectFrom(String tableName) throws SQLException;

    /**
     * Requ�te sur une table avec crit�res.
     * 
     * @param tabelName
     *            Nom de la table
     * @param whereClause
     *            Clause de crit�res
     * @return Le r�sultat de la requ�te
     * @throws SQLException
     *             Erreur SQL
     */
    ResultSet executeSelectFromWhere(String tabelName, String whereClause) throws SQLException;

    /**
     * Suppression de donn�es dans une table avec crit�res.
     * 
     * @param tableName
     *            Nom de la table
     * @param whereClause
     *            Clause de crit�res
     * @throws SQLException
     *             Erreur SQL
     */
    void deleteDataWhere(String tableName, String whereClause) throws SQLException;

    /**
     * Fin du service de base de donn�es.
     * 
     * @throws SQLException
     *             Erreur SQL
     */
    void endService() throws SQLException;
}
