package org.jlb.tools.resources.kvfile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jlb.tools.logging.LogTracer;
import org.jlb.tools.resources.kvfile.impl.GenericKeyValueFile;

/**
 * Crée tous les fichiers .properties par défaut correspondant aux dictionnaires
 * par défaut ainsi qu'aux dictionnaires passés en argument.
 * 
 * @author JLuc
 */
public class DicoBuilder implements Runnable {

	/** Liste de bundles a charger. */
	protected List<GenericKeyValueFile> mBundlesToLoad;
	/** Repertoire de base. */
	protected File mBaseDirectory;

	/**
	 * Constructeur.
	 * 
	 * @param baseDirectory
	 *            repertoire de base
	 */
	public DicoBuilder(File baseDirectory) {
		this(baseDirectory, new ArrayList<GenericKeyValueFile>());
	}

	/**
	 * Constructeur.
	 * 
	 * @param baseDirectory
	 *            repertoire de base
	 * @param bundlesToLoad
	 *            liste des bundles a charger
	 * @param generateFrameworkBundles
	 *            chargement des bundles du framework
	 */
	public DicoBuilder(final File baseDirectory, List<GenericKeyValueFile> bundlesToLoad) {
		mBaseDirectory = baseDirectory;

		List<GenericKeyValueFile> frameworkBundles = new ArrayList<GenericKeyValueFile>();
		frameworkBundles.addAll(bundlesToLoad);
		mBundlesToLoad = frameworkBundles;
	}

	@Override
	public void run() {
		for (GenericKeyValueFile bundle : mBundlesToLoad) {
			String template = bundle.getTemplateFileAsString();
			String bundleName = bundle.getBundleName();
			String fileName = mBaseDirectory.getAbsolutePath() + File.separator + bundleName + ".properties";
			File bundleFile = new File(fileName);
			try {
				LogTracer.getLogger().debug("Ecriture du template : " + bundleFile);
				FileUtils.writeStringToFile(bundleFile, template, "utf-8");
			} catch (IOException e) {
				LogTracer.getLogger().error("Erreur durant l'écriture du template: " + bundleFile, e);
			}
		}
	}

	/**
	 * Permet de produire les fichiers de configuration par defaut.
	 * 
	 * @param args
	 *            arguments de la ligne de commande
	 */
	public static void main(String[] args) {
		File basedir = new File("src");
		DicoBuilder builder = null;

		if (args.length >= 1) {
			basedir = new File(args[0]);
		}

		if (!basedir.exists() || !basedir.canWrite()) {
			LogTracer.getLogger().debug("Repertoire de destination inexistant ou en lecture seule : " + basedir);
			System.exit(1);
		} else {
			LogTracer.getLogger().debug("Repertoire de destination OK : " + basedir);
		}

		if (args.length >= 2) {
			List<GenericKeyValueFile> listOfBundles = new ArrayList<GenericKeyValueFile>();

			// Les noms des classes de dictionnaires sont à partir du second
			// argument
			for (int i = 1; i < args.length; i++) {
				try {
					String dicoClassName = args[i];
					Class<?> classOfDico = Class.forName(dicoClassName);
					Field field = classOfDico.getField("INSTANCE");
					GenericKeyValueFile bundle = (GenericKeyValueFile) field.get(null);
					listOfBundles.add(bundle);
					LogTracer.getLogger().debug("Dictionnaire a regenerer : " + bundle.getBundleName());
				} catch (Exception e) {
					LogTracer.getLogger().error(e.getMessage(), e);
				}
			}
			if (listOfBundles.isEmpty()) {
				LogTracer.getLogger().debug("Aucun dictionnaire valide à regenerer.");
				System.exit(2);
			}
			builder = new DicoBuilder(basedir, listOfBundles);
		} else {
			builder = new DicoBuilder(basedir);
		}
		LogTracer.getLogger().debug("Regeneration des dictionnaires...");
		builder.run();
	}

}
