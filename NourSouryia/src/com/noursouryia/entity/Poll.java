package com.noursouryia.entity;

public class Poll {

//	a.	qid : question ID.
//	b.	question : question text.
//	c.	date : poll date.
//	d.	link : API which use to bring details poll when the visitor will click on any question or poll, 

	private int qid;
	private String question;
	private String date;
	private String link;
	
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("qid : " 		+ getQid() + "\n");
		sb.append("question : " + getQuestion() + "\n");
		sb.append("date : " 	+ getDate() + "\n");
		sb.append("link : " 	+ getLink());
		return sb.toString();
	}
	
}
