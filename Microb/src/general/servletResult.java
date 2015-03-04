package general;

import general.convertToJSON;

/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 * represent any generic result of the servlets
 * in the form of <result: xxx> or <result:xxx, value: yyy>
 * can return the result in JSON format
 */

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
	
	/**
	 * Return the JSON representation of result object.
	 *
	 * @return string of JSON representation of the result.
	 */	
	public String getJSONResult()
	{
		return convertToJSON.doConvert(this);
	}
	
	public void setResult(String result)
	{
		this.result = result;
	}

	public String getValue() {
		return value;
	}

}
