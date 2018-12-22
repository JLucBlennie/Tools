package org.jlb.tools.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jlb.tools.logging.LogTracer;

/**
 * Classe CSVWriter : classe utilitaire d'écriture d'un fichier CSV.
 * 
 * @author JLuc
 *
 */
public final class CSVWriter {

	/**
	 * Caractère de séparation d'éléments.
	 */
	private static String mSEPARATOR = ";";

	/**
	 * Constructeur par défaut.
	 */
	private CSVWriter() {

	}

	/**
	 * Ecriture du fichier CSV.
	 * 
	 * @param file
	 *            Fichier à écrire
	 * @param datas
	 *            les données à écrire
	 */
	public static void writeFile(final File file, final List<String[]> datas) {

		FileWriter fr;
		BufferedWriter br;
		try {
			fr = new FileWriter(file);
			br = new BufferedWriter(fr);

			for (String[] dataLine : datas) {
				for (int i = 0; i < dataLine.length; i++) {
					String data = dataLine[i];
					br.write(data);
					if (i < dataLine.length - 1) br.write(mSEPARATOR);
				}
				br.newLine();
			}
			br.flush();
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			LogTracer.getLogger().error("Fichier non trouvé : " + file.getAbsolutePath(), e);
		} catch (IOException e) {
			LogTracer.getLogger().error("Erreur durant l'écriture du fichier : " + file.getAbsolutePath(), e);
		}
	}

}
