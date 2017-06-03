package com.jlb.tools.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe CSVReader : classe utilitaire de lecture d'un fichier CSV.
 * 
 * @author JLuc
 *
 */
public final class CSVReader {

    /**
     * Caractère de séparation d'éléments.
     */
    private static String mSEPARATOR = ";";

    /**
     * Constructeur par défaut.
     */
    private CSVReader() {

    }

    /**
     * Lecture du fichier CSV.
     * 
     * @param file
     *            Fichier à lire
     * @return la liste des éléments du fichier
     */
    public static List<String[]> readFile(final File file) {

        List<String[]> result = new ArrayList<String[]>();

        FileReader fr;
        BufferedReader br;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] readedLine = line.split(mSEPARATOR);
                final int size = readedLine.length;
                if (size == 0) {
                    continue;
                }

                String debut = readedLine[0].trim();
                if (debut.length() == 0 && size == 1) {
                    continue;
                }
                if (debut.startsWith("#")) {
                    continue;
                }
                result.add(readedLine);
            }

            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            // TODO : Voir la gestion des exceptions
            e.printStackTrace();
        } catch (IOException e) {
            // TODO : Voir la gestion des exceptions
            e.printStackTrace();
        }

        return result;
    }

}
