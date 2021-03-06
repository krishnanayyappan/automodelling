package com.nlp.core_nlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import edu.stanford.nlp.pipeline.*;
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
		
	public static final String PROPERTIESLIST = "tokenize,ssplit,pos,lemma,depparse,natlog,openie,ner";
	
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
		ClassifiedSentence classifiedSentenceAfterAnalysis;
		ClassificationCoreLabel classificationCoreLabel;
		ClassificationTriple classificationTriple;
		ClassificationTriple classificationTripleAfterAnalysis;
		ArrayList<ClassifiedSentence> listOfClassifiedSentences = new ArrayList<ClassifiedSentence>();
		ArrayList<ClassifiedSentence> listOfClassifiedSentencesAfterAnalysis = new ArrayList<ClassifiedSentence>();
		ArrayList<ClassificationCoreLabel> listOfClassificationPerWord;
		ArrayList<ClassificationTriple> listOfClassificationTriple;
		ArrayList<ClassificationTriple> listOfClassificationTripleAfterAnalysis;
		
		Map<String, String> nlpMap = new HashMap<String, String>();
		
		String[] oieSubjectArray;
		String[] oieObjectArray;
		String[] oieRelationArray;
		
		boolean isSubject;
		boolean isObject;
		boolean isRelation;
		
		String subjectAfterAnalysis;
		String objectAfterAnalysis;
		String relationAfterAnalysis;
		
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
						classifiedSentenceAfterAnalysis = new ClassifiedSentence();
						line = tempLine;
						
						classifiedSentence.setUnknown(null);
						classifiedSentenceAfterAnalysis.setUnknown(null);

						// Create the Stanford CoreNLP pipeline
						Properties properties = PropertiesUtils.asProperties("annotators", PROPERTIESLIST);
						StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);
	
						// Annotate an example document.						
						Annotation annotation = new Annotation(line);
						pipeline.annotate(annotation);
						
						

	
						// Loop over sentences in the document
						int sentenceNo = 0;
						for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
							System.out.println("\n############################## NEXT SENTENCE #########################\n");
							System.out.println("\nSentence is #" + ++sentenceNo + ": " + sentence.get(CoreAnnotations.TextAnnotation.class)+"\n");
							
							classifiedSentence.setSentence(sentence.toString());
							classifiedSentenceAfterAnalysis.setSentence(sentence.toString());
							
							listOfClassificationPerWord = new ArrayList<ClassificationCoreLabel>();
							
							for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				                String word = token.get(CoreAnnotations.TextAnnotation.class);
				                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
				                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
	
				                System.out.println(String.format("Print Word, its parts of speech and named entity: [%s] POS: [%s] NER: [%s]", word, pos, ner));
				                
				                nlpMap.put(word, pos);
				                
				                classificationCoreLabel = new ClassificationCoreLabel(word, pos, ner);
				                listOfClassificationPerWord.add(classificationCoreLabel);
				                
				            }
							
							
	
							// Print SemanticGraph
							System.out.println("\n"+sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class)
									.toString(SemanticGraph.OutputFormat.LIST));
	
							// Get the OpenIE triples for the sentence
							Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
	
							listOfClassificationTriple = new ArrayList<ClassificationTriple>();
							listOfClassificationTripleAfterAnalysis = new ArrayList<ClassificationTriple>();
							
							// Print the triples
							for (RelationTriple triple : triples) {
								System.out.println(triple.confidence + "\t" + triple.subjectLemmaGloss() + "\t"
										+ triple.relationGloss() + "\t" + triple.objectLemmaGloss());
								classificationTriple = new ClassificationTriple(triple.subjectLemmaGloss(), 
										triple.relationGloss(), triple.objectLemmaGloss());
								listOfClassificationTriple.add(classificationTriple);
								
								oieSubjectArray = triple.subjectGloss().split(" ");
								oieObjectArray = triple.objectGloss().split(" ");
								oieRelationArray = triple.relationGloss().split(" ");
								isObject = true;
								isSubject = true;
								isRelation = true;
								//Removing the part of code which decides which sentence to be in final result.
								/*for(String subject : oieSubjectArray) {
									if (! nlpMap.get(subject).contains("NN")) {
										System.out.println("This is line is not included as a subject : "+triple.subjectGloss());
										isSubject = false;
										break;
									}									
								}
								
								for(String object : oieObjectArray) {
									if (! nlpMap.get(object).contains("NN")) {
										System.out.println("This is line is not included as a object : "+triple.objectGloss());
										isObject = false;
										break;
									}									
								}
								
								for(String relation : oieRelationArray) {
									if (! nlpMap.get(relation).contains("VB")) {
										System.out.println("This is line is not included as a relation : "+triple.relationGloss());
										isRelation = false;
										break;
									}									
								}*/
								subjectAfterAnalysis="";
								objectAfterAnalysis="";
								relationAfterAnalysis="";
								if(isSubject) {
									subjectAfterAnalysis = triple.subjectGloss();									
								}
								if(isObject) {
									objectAfterAnalysis = triple.objectGloss();
								}
								if(isRelation) {
									relationAfterAnalysis = triple.relationGloss();
								}
								
								if(!subjectAfterAnalysis.equals("") && !objectAfterAnalysis.equals("") && !relationAfterAnalysis.equals("")) {
									classificationTripleAfterAnalysis = new ClassificationTriple(subjectAfterAnalysis, 
										relationAfterAnalysis, objectAfterAnalysis);
									listOfClassificationTripleAfterAnalysis.add(classificationTripleAfterAnalysis);
								}
								
							}	
							classifiedSentence.setListClassificationCoreLabel(listOfClassificationPerWord);
							classifiedSentence.setListClassificationTriple(listOfClassificationTriple);
							
							//Commenting code that writes results to a csv file
							/*listOfClassifiedSentences.add(classifiedSentence);
							writeClassifiedSentencesToFile(listOfClassifiedSentences, "output1");
							
							System.out.println("Finished writing to output1");
							
							if(!listOfClassificationTripleAfterAnalysis.isEmpty()) {
								classifiedSentenceAfterAnalysis.setListClassificationCoreLabel(listOfClassificationPerWord);
								classifiedSentenceAfterAnalysis.setListClassificationTriple(listOfClassificationTripleAfterAnalysis);
								listOfClassifiedSentencesAfterAnalysis.add(classifiedSentenceAfterAnalysis);
								writeClassifiedSentencesToFile(listOfClassifiedSentencesAfterAnalysis, "outputAfterAnalysis2");
								System.out.println("Finished writing to outputAfterAnalysis2");
							}*/
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

	//commenting code that writes classified sentences to file
	/*private static void writeClassifiedSentencesToFile(ArrayList<ClassifiedSentence> listOfClassifiedSentences, String fileName) {
		String content = "\n";
		for(ClassifiedSentence cs : listOfClassifiedSentences)
			content = content + cs.toString()+ "\n ----------\n";
		
		DateTime dt = new DateTime(date);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd-HH-mm");
		String outputFilename = "\\"+fileName+""+dt.toString(dtf)+".csv";
		
		fileHelper.saveToFile(content, "result", outputFilename, "UTF-8");
		
	}*/
	
	private static String getFileWithRelativePath(final File folder, final File file) {
		return folder + "\\" + file.getName();
	}

	public static void main(String[] args) throws IOException {

		listFilesForFolder(new Feature_merged("test").folder);
	}
}
