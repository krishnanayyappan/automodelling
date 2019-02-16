package com.nlp.core_nlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

public class Feature {

	static OpenIE open = new OpenIE();
	static CoreNlp core = new CoreNlp();
	static String text = null;
	
	ClassLoader classLoader = getClass().getClassLoader();	
	final File folder = new File(classLoader.getResource("test").getFile());
	

	public static void listFilesForFolder(final File folder) throws IOException {
		
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				String filename = folder + "\\" + fileEntry.getName();
				BufferedReader read = new BufferedReader(new FileReader(new File(filename)));

				String st;
				while ((st = read.readLine()) != null) {

					open.computeInfo(st);
					core.createTags(st);
					
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {

		
		listFilesForFolder(new Feature().folder);
	}

}
