package com.noursouryia.entity;

import java.util.ArrayList;

public class SearchResult {
	
	private int total_items = 0;
	private ArrayList<Article> articles = new ArrayList<Article>();
	
	public int getTotal_items() {
		return total_items;
	}
	public void setTotal_items(int total_items) {
		this.total_items = total_items;
	}
	public ArrayList<Article> getArticles() {
		return articles;
	}
	public void setArticles(ArrayList<Article> articles) {
		this.articles = articles;
	}

}
