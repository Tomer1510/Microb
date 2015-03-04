package model;

import java.sql.Timestamp;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * represent user's message
 * 
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 */

public class Messages {
	private Timestamp timestamp; // date of the creation of the message
	private int messageID; // ID number in the DB
	private int republishCounter; // number of times the message has been republished
	private String authorNickname, content;
	//private List<Mentions> mentions; // lists of mentions in the message
	private List<Topics> topics; // lists of topics in the message
	
	public Messages(int ID, String nickname, String content, String topics, Timestamp timestamp, int republishCounter) {
		// TODO Auto-generated constructor stub
		Gson gson = new Gson();
	 	//this.mentions = gson.fromJson(mentions, new TypeToken<List<Mentions>>(){}.getType());
	 	this.topics = gson.fromJson(topics, new TypeToken<List<Topics>>(){}.getType());
		this.messageID = ID;
		this.authorNickname = nickname;
		this.content = content;
		this.timestamp = timestamp;
		this.republishCounter = republishCounter;
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
	
	/*public List<Mentions> getMentions() {
		return this.mentions;
	}*/

	public int getRepublishCounter() {
		return this.republishCounter;
	}

	public List<Topics> getTopics() {
		return topics;
	}
}
