package model;

public class Mentions {
	private String nickname;
	//private int start, end;
	
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
