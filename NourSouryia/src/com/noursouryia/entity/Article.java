package com.noursouryia.entity;

import java.util.ArrayList;

import com.noursouryia.externals.NSManager;

public class Article {
	
//	1-	nid : material ID.
//	2-	title : material’s title.
//	3-	body : material’s content.
//	4-	type : material’s  section.
//	4'-	type_ar : material’s  section in ARABIC.
//	5-	visits : number of people who have visited this material.
//	6-	created : material’s date.
//	7-	name : author’s name.
//	8-	tid : author’s  ID.
//	9-	filepath : image’s link, it has array of link. The first key of index is 0.
//	10-	youtube_link: youtube link for material if it is exist.   
//	11-	mp4_link: video link for material if it is exist.   
//	12-	mp3_link: audio link for material if it is exist.
//	13-	pdf_link: book link for material if it is exist.

	private int nid = NSManager.DEFAULT_VALUE;
	private String title;
	private String body;
	private String type;
	private String typeAr;
	private int visits = 0;
	private String created;
	private String name;
	private int tid = NSManager.DEFAULT_VALUE;
	private ArrayList<String> filePath = new ArrayList<String>();
	private String youtubeLink;
	private String mp4Link;
	private String mp3Link;
	private String pdfLink;
	
	public int getNid() {
		return nid;
	}
	public void setNid(int nid) {
		this.nid = nid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeAr() {
		return typeAr;
	}
	public void setTypeAr(String typeAr) {
		this.typeAr = typeAr;
	}
	public int getVisits() {
		return visits;
	}
	public void setVisits(int visits) {
		this.visits = visits;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public ArrayList<String> getFilePath() {
		return filePath;
	}
	public void setFilePath(ArrayList<String> filePath) {
		this.filePath = filePath;
	}
	public String getYoutubeLink() {
		return youtubeLink;
	}
	public void setYoutubeLink(String youtubeLink) {
		this.youtubeLink = youtubeLink;
	}
	public String getMp4Link() {
		return mp4Link;
	}
	public void setMp4Link(String mp4Link) {
		this.mp4Link = mp4Link;
	}
	public String getMp3Link() {
		return mp3Link;
	}
	public void setMp3Link(String mp3Link) {
		this.mp3Link = mp3Link;
	}
	public String getPdfLink() {
		return pdfLink;
	}
	public void setPdfLink(String pdfLink) {
		this.pdfLink = pdfLink;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("nid : " 		+ getNid() + "\n");
		sb.append("title : " 	+ getTitle() + "\n");
		sb.append("body : " 	+ getBody() + "\n");
		sb.append("type : " 	+ getType() + "\n");
		sb.append("typeAr : " 	+ getTypeAr() + "\n");
		sb.append("visits : " 	+ getVisits() + "\n");
		sb.append("created : " 	+ getCreated() + "\n");
		sb.append("name : "	 	+ getName() + "\n");
		sb.append("tid : " 		+ getTid() + "\n");
		sb.append("filePath(size) : " 	+ getFilePath().size() + "\n");
		sb.append("youtubeLink : " 		+ getYoutubeLink() + "\n");
		sb.append("mp4Link : " 	+ getMp4Link() + "\n");
		sb.append("mp3Link : " 	+ getMp3Link() + "\n");
		sb.append("pdfLink : " 	+ getPdfLink());
		
		return sb.toString();
	}
	
}
