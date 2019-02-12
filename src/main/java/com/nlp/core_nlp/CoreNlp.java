package com.nlp.core_nlp;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;

public class CoreNlp {

    public void createTags ( String text ){
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
//        String text = "Leo was born in India";

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                System.out.println(String.format("Print: word: [%s] pos: [%s] ne: [%s]", word, pos, ne));


            }
//            System.out.println("End of Line");
        }
    }
    
	/*
	 * public static void main ( String [] args ){ // creates a StanfordCoreNLP
	 * object, with POS tagging, lemmatization, NER, parsing, and coreference
	 * resolution Properties props = new Properties();
	 * props.setProperty("annotators",
	 * "tokenize, ssplit, pos, lemma, ner, parse, dcoref"); StanfordCoreNLP pipeline
	 * = new StanfordCoreNLP(props);
	 * 
	 * // read some text in the text variable String text = "Obama lives in USA";
	 * 
	 * // create an empty Annotation just with the given text Annotation document =
	 * new Annotation(text);
	 * 
	 * // run all Annotators on this text pipeline.annotate(document);
	 * 
	 * List<CoreMap> sentences =
	 * document.get(CoreAnnotations.SentencesAnnotation.class);
	 * 
	 * for (CoreMap sentence : sentences) { // traversing the words in the current
	 * sentence // a CoreLabel is a CoreMap with additional token-specific methods
	 * for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class))
	 * { // this is the text of the token String word =
	 * token.get(CoreAnnotations.TextAnnotation.class); // this is the POS tag of
	 * the token String pos =
	 * token.get(CoreAnnotations.PartOfSpeechAnnotation.class); // this is the NER
	 * label of the token String ne =
	 * token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
	 * 
	 * System.out.println(String.format("Print: word: [%s] pos: [%s] ne: [%s]",
	 * word, pos, ne));
	 * 
	 * // this is the parse tree of the current sentence Tree tree =
	 * sentence.get(TreeAnnotation.class);
	 * 
	 * // this is the Stanford dependency graph of the current sentence
	 * 
	 * @SuppressWarnings("deprecation") SemanticGraph dependencies =
	 * sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
	 * 
	 * } } }
	 */
    
}
