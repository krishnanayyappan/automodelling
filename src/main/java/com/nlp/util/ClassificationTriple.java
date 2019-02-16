package com.nlp.util;

public class ClassificationTriple {

	private String subject;
	private String object;
	private String predicate;
	
	public ClassificationTriple() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ClassificationTriple(String subject, String predicate, String object) {
		super();
		this.subject = subject;
		this.object = object;
		this.predicate = predicate;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	
}
