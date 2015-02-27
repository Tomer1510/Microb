package model;

import general.AppConstants;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Messages {
	private Timestamp timestamp;
	private int messageID;
	private String authorNickname, content;
	private List<Mentions> mentions;
	public Messages(int ID, String nickname, String content, String mentions, Timestamp timestamp) {
		// TODO Auto-generated constructor stub
		Gson gson = new Gson();
	 	this.mentions = gson.fromJson(mentions, new TypeToken<List<Mentions>>(){}.getType());
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
	
	public List<Mentions> getMentions() {
		return this.mentions;
	}
}
