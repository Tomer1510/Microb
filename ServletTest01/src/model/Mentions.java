package model;

/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 * represent a mention on user's message
 */

public class Mentions {
	private String nickname;
	private int start, end; // start and end index of the mention inside the raw message content
	
	public Mentions(String nickname) {
		this.nickname = nickname;
		//this.start = start;
		//this.end = end;
	}
	
	public String getNickname() {
		return this.nickname;
	}
	
	/*public int getStart() {
		return this.start;
	}
	
	public int getEnd() {
		return this.end;
	}*/
	
	
	/*public String toString() {
		String ret = "[";
		for (int i = 0; i < this.mentions.size(); i++) {
			ret += '"'+this.mentions.get(i)+"\",";
	     }
		ret = (ret.length()>1?ret.substring(0, ret.length()-1):ret)+"]";
		System.out.println(ret);
		return ret;
	}*/

}
