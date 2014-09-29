package com.noursouryia.entity;

public class PollChoice {
	
//	a.	chid : choice ID, you will use it later to let me know which choice has been chosen by user.
//	b.	chtext :  choice text.
//	c.	chvotes : The total number of votes this choice has received by all users.

	private int chid;
	private String chtext;
	private int chvotes;
	
	public int getChid() {
		return chid;
	}
	public void setChid(int chid) {
		this.chid = chid;
	}
	public String getChtext() {
		return chtext;
	}
	public void setChtext(String chtext) {
		this.chtext = chtext;
	}
	public int getChvotes() {
		return chvotes;
	}
	public void setChvotes(int chvotes) {
		this.chvotes = chvotes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("chid : " 	+ getChid() + "\n");
		sb.append("chtext : " 	+ getChtext() + "\n");
		sb.append("chvotes : " 	+ getChvotes());
		return sb.toString();
	}
}
