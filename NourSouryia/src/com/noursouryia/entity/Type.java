package com.noursouryia.entity;

import java.util.ArrayList;

public class Type {
	
//	1-	name_en: the name of section in English.
//	2-	name_ar : the name of section in Arabic, you can use it in the menu.
//	3-	link : this link will let visitor to display articles of the section ,we have two type of links.
//	a.	link=# : when the section has categories.
//	b.	full link : when the section doesn’t has categories.
//	4-	cats : this key will be returned only when the section has some categories. 

	private String nameEn;
	private String nameAr;
	private String link; // "#" if it has categories
	private ArrayList<Category> categories = new ArrayList<Category>();
	
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public String getNameAr() {
		return nameAr;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public ArrayList<Category> getCategories() {
		return categories;
	}
	public void setCategories(ArrayList<Category> categories) {
		this.categories = categories;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("nameEn : " 	+ getNameEn() + "\n");
		sb.append("nameAr : "	+ getNameAr() + "\n");
		sb.append("link : " 	+ getLink() + "\n");
		sb.append("categories(size) : " 	+ getCategories().size());
		return sb.toString();
	}
	
}
