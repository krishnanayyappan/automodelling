package com.nlp.util;

import java.util.ArrayList;

public class ClassifiedSentence {
	
	private String sentence;	
	private String unknown;	
	private ArrayList<ClassificationCoreLabel> listOfClassificationCoreLabel;
	private ArrayList<ClassificationTriple> listOfClassificationTriple;
	
	public ClassifiedSentence() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClassifiedSentence(String sentence, String unknown,
			ArrayList<ClassificationCoreLabel> listOfClassificationCoreLabel,
			ArrayList<ClassificationTriple> listOfClassificationTriple) {
		super();
		this.sentence = sentence;
		this.unknown = unknown;
		this.listOfClassificationCoreLabel = listOfClassificationCoreLabel;
		this.listOfClassificationTriple = listOfClassificationTriple;
	}

	@Override
	public String toString() {
		
		String result = null;
		result = "SENTENCE|WORD|POS|NER|SUBJECT|PREDICATE|OBJECT \n";
		for(ClassificationCoreLabel corelabel : listOfClassificationCoreLabel) {
			for(ClassificationTriple triple : listOfClassificationTriple) {
				result = result + "" + sentence + "|"
						+ corelabel.getWord() + "|" + corelabel.getPos() + "|" + corelabel.getNer() + "|"
								+ "" + triple.getSubject() + "|" + triple.getPredicate() + "|" + triple.getObject() + "\n";
			}
		}
		return result;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getUnknown() {
		return unknown;
	}

	public void setUnknown(String unknown) {
		this.unknown = unknown;
	}

	public ArrayList<ClassificationCoreLabel> getListClassificationCoreLabel() {
		return listOfClassificationCoreLabel;
	}

	public void setListClassificationCoreLabel(ArrayList<ClassificationCoreLabel> listOfClassificationCoreLabel) {
		this.listOfClassificationCoreLabel = listOfClassificationCoreLabel;
	}

	public ArrayList<ClassificationTriple> getListClassificationTriple() {
		return listOfClassificationTriple;
	}

	public void setListClassificationTriple(ArrayList<ClassificationTriple> listOfClassificationTriple) {
		this.listOfClassificationTriple = listOfClassificationTriple;
	}
	
}