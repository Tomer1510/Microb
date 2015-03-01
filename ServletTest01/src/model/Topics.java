package model;

public class Topics {
	private String topic;
	
	public Topics(String topic) {
		this.topic = topic;
	}
	
	public String getTopic() {
		return this.topic;
	}
	
	
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
