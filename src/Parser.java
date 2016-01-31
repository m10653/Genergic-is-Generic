import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
//import org.json.JSONObject;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
/**
 * 
 * @author Michael Combs
 *
 */
public class Parser {
	public static Map<String, String> parseBody(String body){
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType(); //TODO:Learn what the crap is going on here.
		Map<String,String> parsed = gson.fromJson(body, type);
		return parsed;
	}

//	public static Map<String, String> parseBody(String body) { // Returns a map TODO: del old code
//																// of the JSON
//																// encoded post
//																// body and
//																// returns a map
//		Map<String, String> values = new HashMap<String, String>();
//		JSONObject obj = new JSONObject(body);
//		String[] names = JSONObject.getNames(obj);
//		for (String str : names) {
//			values.put(str, obj.getString(str));
//		}
//		return values;
//	}

	public static String parseUrl(String query, String name) { 
		
		if(query.length() <= 2){ //Makes sure that Input if valid ( can not parse any value with length of 2 or less)
			 throw new IllegalArgumentException("Query length must be greater than 2");
		}

		String value = "";
		int i = query.indexOf(name + "=");
		if(i == -1){
			throw new IllegalArgumentException("Could not Find name: "+ name); // Checks is name is in query string or not
		}
		i += name.length() + 1;
		while( i < query.length()) {
			if (query.charAt(i) == '&') {
				break;
			}
			value += query.charAt(i);
			i++;
		}
		return value;
	}

	public static Map<String, String> parseUrl(String query) { 

		 if(query.length() <= 2){ //Makes sure that Input if valid ( can not parse any value with length of 2 or less)
		 throw new IllegalArgumentException("Query length must be greater than 2");
		 }

		Map<String, String> parsed = new HashMap<String, String>();
		for (int i = 0; i < query.length(); i++) {
			String name = "";
			String value = "";

			while (query.charAt(i) != '=') { // Get name
				name += query.charAt(i);
				i++;
				
				if (i >= query.length()){
					throw new IllegalArgumentException("Invalid Query, Name with no value");
				}
			}
			i++;

			while (query.charAt(i) != '&' && query.length() > i) {  //Get value
				if (query.charAt(i) == '+') { // replace "+" with a space
					value += " ";
				} else {
					value += query.charAt(i);
				}
				i++;
				
				if (i >= query.length()) { // checks for end of string
					break;
				}
			}
			
			if(name.equals("")){
				throw new IllegalArgumentException("Name Cannot be Null");
			}
			parsed.put(name, value);

		}
		return parsed;
	}

	public static Long parseTimeStamp(String timeStamp) { // TODO add TimeStamp
															// Parser
		return null;
	}

}
