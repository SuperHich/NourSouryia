package com.noursouryia.entity;

public class Author {

	//	a.	tid : author ID.
	//	b.	name : author name.
	//	c.	count : how many articles author dose has .
	//	d.	link : API  to bring all articles for this author, 

	private int tid;
	private String name;
	private int count;
	private String link;
	
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("tid : " 		+ getTid() + "\n");
		sb.append("name : " 	+ getName() + "\n");
		sb.append("count : " 	+ getCount() + "\n");
		sb.append("link : " 	+ getLink());
		return sb.toString();
	}

}
