package com.noursouryia.entity;

public class Category {

//	There are some information under this key:
//		a.	tid : category’s  ID.
//		b.	name : category’s  title.
//		c.	link : this link will let visitor display articles under this category.
		
	private int tid;
	private String name;
	private String link;
	private String parent;
	
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
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("tid : " + getTid() + "\n");
		sb.append("name : " + getName() + "\n");
		sb.append("link : " + getLink());
		return sb.toString();
	}
	
}
