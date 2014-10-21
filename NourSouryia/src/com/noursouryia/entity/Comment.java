package com.noursouryia.entity;

public class Comment {

//	a.	name : visitor’s name.
//	b.	country : visitor’s country.
//	c.	date : date of adding.
//	d.	body : content of comment.

	private String name;
	private String country;
	private String email;
	private String date;
	private String body;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("name : " 	+ getName() + "\n");
		sb.append("country : " 	+ getCountry() + "\n");
		sb.append("date : " 	+ getDate() + "\n");
		sb.append("body : " 	+ getBody());
		return sb.toString();
	}
	
}
