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

public class Feature_merged {

	static OpenIE open = new OpenIE();
	static CoreNlp core = new CoreNlp();
	static String text = null;

	public static void listFilesForFolder(final File folder) throws IOException {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				String filename = folder + "\\" + fileEntry.getName();
				BufferedReader read = new BufferedReader(new FileReader(new File(filename)));

				String st;
				while ((st = read.readLine()) != null) {
					text = st;

//					open.computeInfo(st);
//					core.createTags(st);

					// Create the Stanford CoreNLP pipeline
					Properties props = PropertiesUtils.asProperties("annotators",
							"tokenize,ssplit,pos,lemma,depparse,natlog,openie");
					StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

					// Annotate an example document.
					
					Annotation doc = new Annotation(text);
					pipeline.annotate(doc);

					// Loop over sentences in the document
					int sentNo = 0;
					for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
						
						for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
			                // this is the text of the token
			                String word = token.get(CoreAnnotations.TextAnnotation.class);
			                // this is the POS tag of the token
			                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
			                // this is the NER label of the token
			                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

			                System.out.println(String.format("Print: word: [%s] pos: [%s] ne: [%s]", word, pos, ne));

			            }
						System.out.println();
						
						System.out.println("Sentence #" + ++sentNo + ": " + sentence.get(CoreAnnotations.TextAnnotation.class));

						// Print SemanticGraph
						System.out.println(sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class)
								.toString(SemanticGraph.OutputFormat.LIST));

						// Get the OpenIE triples for the sentence
						Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

						// Print the triples
						for (RelationTriple triple : triples) {
							System.out.println(triple.confidence + "\t" + triple.subjectLemmaGloss() + "\t"
									+ triple.relationLemmaGloss() + "\t" + triple.objectLemmaGloss());
						}

						// Alternately, to only run e.g., the clause splitter:
						/*
						 * List<SentenceFragment> clauses = new
						 * OpenIE(props).clausesInSentence(sentence); for (SentenceFragment clause :
						 * clauses) {
						 * System.out.println(clause.parseTree.toString(SemanticGraph.OutputFormat.LIST)
						 * ); }
						 */
						
						
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {

		final File folder = new File("C:\\UTA\\Spring 19\\2192-CSE-5328-001-SFWR ENGR TEAM PROJECT I\\corenlpinputs");
		listFilesForFolder(folder);

	}

}
