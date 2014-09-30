package com.noursouryia.entity;

import java.util.ArrayList;

public class Author {

	//	a.	tid : author ID.
	//	b.	name : author name.
	//	c.	count : how many articles author dose has .
	//	d.	link : API  to bring all articles for this author, 

	private int tid;
	private String name;
	private int count;
	private String link;
	private ArrayList<Article> articles = new ArrayList<Article>();
	
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	public ArrayList<Article> getArticles() {
		return articles;
	}
	public void setArticles(ArrayList<Article> articles) {
		this.articles = articles;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("tid : " 		+ getTid() + "\n");
		sb.append("name : " 	+ getName() + "\n");
		sb.append("count : " 	+ getCount() + "\n");
		sb.append("link : " 	+ getLink() + "\n");
		sb.append("articles(size) : " 	+ getArticles().size());
		return sb.toString();
	}

}
