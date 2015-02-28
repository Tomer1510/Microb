package general;

import general.convertToJSON;

public class servletResult {
	
	private String result;
	private String value;
	
	public servletResult(String result)
	{
		this.result = result;
	}
	
	public servletResult(String result, String value)
	{
		this.result = result;
		this.value = value;
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
