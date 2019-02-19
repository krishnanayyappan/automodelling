package com.nlp.core_nlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.nlp.util.ClassifiedSentence;
import com.nlp.util.FileHelper;
import com.nlp.util.ClassificationCoreLabel;
import com.nlp.util.ClassificationTriple;

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
	static String line = null;
	static FileHelper fileHelper = new FileHelper();
	static Date date = new Date();
		
	public static final String PROPERTIESLIST = "tokenize,ssplit,pos,lemma,depparse,natlog,openie";
	
	ClassLoader classLoader = getClass().getClassLoader();	
	private File folder = null;
	
	public Feature_merged (String folderName) throws IOException{
	
		this.folder = new File(classLoader.getResource(folderName).getFile());
		if(this.folder == null) {
			throw new FileNotFoundException("Folder " + folderName + " does not exist.");
		}
	}	

	public static void listFilesForFolder(final File folder) throws IOException {
		
		ClassifiedSentence classifiedSentence; 
		ClassificationCoreLabel classificationCoreLabel;
		ClassificationTriple classificationTriple;
		ArrayList<ClassifiedSentence> listOfClassifiedSentences = new ArrayList<ClassifiedSentence>();;
		ArrayList<ClassificationCoreLabel> listOfClassificationPerWord;
		ArrayList<ClassificationTriple> listOfClassificationTriple;
		
		
		for (final File file : folder.listFiles()) {
			
			if (file.isDirectory()) {
				listFilesForFolder(file);
			} else {
				
				BufferedReader fileContent = null;
				try {
					
					String filename = getFileWithRelativePath(folder, file);
					
					fileContent = new BufferedReader(new FileReader(new File(filename)));

					String tempLine;
					while ((tempLine = fileContent.readLine()) != null) {
						
						classifiedSentence = new ClassifiedSentence();
						line = tempLine;
						
						classifiedSentence.setUnknown(null);						

						// Create the Stanford CoreNLP pipeline
						Properties properties = PropertiesUtils.asProperties("annotators", PROPERTIESLIST);
						StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);
	
						// Annotate an example document.						
						Annotation annotation = new Annotation(line);
						pipeline.annotate(annotation);
	
						// Loop over sentences in the document
						int sentenceNo = 0;
						for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
							classifiedSentence.setSentence(sentence.toString());
							
							listOfClassificationPerWord = new ArrayList<ClassificationCoreLabel>();
							for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				                String word = token.get(CoreAnnotations.TextAnnotation.class);
				                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
				                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
	
				                System.out.println(String.format("Print: Word: [%s] POS: [%s] NER: [%s]", word, pos, ner));
				                
				                classificationCoreLabel = new ClassificationCoreLabel(word, pos, ner);
				                listOfClassificationPerWord.add(classificationCoreLabel);
				            }
							
							System.out.println("\nSentence #" + ++sentenceNo + ": " + sentence.get(CoreAnnotations.TextAnnotation.class));
	
							// Print SemanticGraph
							System.out.println(sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class)
									.toString(SemanticGraph.OutputFormat.LIST));
	
							// Get the OpenIE triples for the sentence
							Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
	
							listOfClassificationTriple = new ArrayList<ClassificationTriple>();
							
							// Print the triples
							for (RelationTriple triple : triples) {
								System.out.println(triple.confidence + "\t" + triple.subjectLemmaGloss() + "\t"
										+ triple.relationGloss() + "\t" + triple.objectLemmaGloss());
								classificationTriple = new ClassificationTriple(triple.subjectLemmaGloss(), 
										triple.relationGloss(), triple.objectLemmaGloss());
								listOfClassificationTriple.add(classificationTriple);
								
							}	
							classifiedSentence.setListClassificationCoreLabel(listOfClassificationPerWord);
							classifiedSentence.setListClassificationTriple(listOfClassificationTriple);
							listOfClassifiedSentences.add(classifiedSentence);
							writeClassifiedSentencesToFile(listOfClassifiedSentences);
						}
					}
					
				} catch (Exception e){
					System.out.println("Program encountered an error while processing the file : " +e);
				} finally {
					if(fileContent != null)
						fileContent.close();
				}
			}
		}
		
	}

	private static void writeClassifiedSentencesToFile(ArrayList<ClassifiedSentence> listOfClassifiedSentences) {
		String content = "\n";
		for(ClassifiedSentence cs : listOfClassifiedSentences)
			content = content + cs.toString()+ "\n ----------\n";
		
		DateTime dt = new DateTime(date);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd-HH-mm");
		String outputFilename = "\\output"+dt.toString(dtf)+".csv";
		
		fileHelper.saveToFile(content, "result", outputFilename, "UTF-8");
		
	}

	private static String getFileWithRelativePath(final File folder, final File file) {
		return folder + "\\" + file.getName();
	}

	public static void main(String[] args) throws IOException {

		listFilesForFolder(new Feature_merged("test").folder);
	}
}
