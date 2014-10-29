package com.noursouryia.entity;

public class Share {

//	a.	title: title of the sharing.
//	b.	writer: writer’s name.
//	c.	email:   visitor email.
//	d.	msg :   message.


	private String title;
	private String writer;
	private String email;
	private String message;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("title : " 	+ getTitle() + "\n");
		sb.append("writer : " 	+ getWriter() + "\n");
		sb.append("email : " 	+ getEmail() + "\n");
		sb.append("message : " 	+ getMessage());
		return sb.toString();
	}
	
}
