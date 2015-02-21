package general;

import java.lang.reflect.Type;

import model.Users;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class convertToJSON {

	public static String doConvert(Object o)
	{
		Type gsonType;
		
		if(o instanceof servletResult)
		{
			gsonType = new TypeToken<servletResult>() {}.getType();
		} else if(o instanceof Users)
		{
			gsonType = new TypeToken<Users>() {}.getType();
		}/* else if(o instanceof Messages)
		{
			
		}*/ else
		{
			return "";
		}
		
		Gson gson = new Gson();
    	String jsonString = gson.toJson(o, gsonType);
    	
    	return jsonString;
	}
	
}
