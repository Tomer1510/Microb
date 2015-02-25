package model;

import java.sql.Timestamp;

public class Messages {
	private Timestamp timestamp;
	private int messageID;
	private String authorNickname, content;
	public Messages(int ID, String nickname, String content, Timestamp timestamp) {
		// TODO Auto-generated constructor stub
		this.messageID = ID;
		this.authorNickname = nickname;
		this.content = content;
		this.timestamp = timestamp;
	}
	
	public String getTimestamp() {
		return this.timestamp.toString();
	}

	public String getAuthor() {
		return this.authorNickname;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public int getID() {
		return this.messageID;
	}
}
