package general;

import general.convertToJSON;

public class servletResult {
	
	private String result;
	
	public servletResult(String result)
	{
		this.result = result;
	}
	
	public String getResult()
	{
		return result;
	}
	
	public String getJSONResult()
	{
		return convertToJSON.doConvert(this);
	}
	
	public void setResult(String result)
	{
		this.result = result;
	}

}
