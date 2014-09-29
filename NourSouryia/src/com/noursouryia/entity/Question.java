package com.noursouryia.entity;

import java.util.ArrayList;

public class Question {

//	a.	qid : question ID.
//	b.	question : question text.
//	c.	poll_choices : data about choices of this poll:

	private int qid;
	private String question;
	private ArrayList<PollChoice> pollChoices = new ArrayList<PollChoice>();
	
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
	public ArrayList<PollChoice> getPollChoices() {
		return pollChoices;
	}
	public void setPollChoices(ArrayList<PollChoice> pollChoices) {
		this.pollChoices = pollChoices;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("qid : " 	+ getQid() + "\n");
		sb.append("question : "	+ getQuestion() + "\n");
		sb.append("pollChoices(size) : " 	+ getPollChoices().size());
		return sb.toString();
	}
}
