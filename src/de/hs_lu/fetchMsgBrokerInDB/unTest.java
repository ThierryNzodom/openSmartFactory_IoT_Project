package de.hs_lu.fetchMsgBrokerInDB;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class unTest {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws JSONException {
		
		String descriptionKey = null;
		String message = "{smartfactory.waage:{StopFeed:FALSE}}"; 
		
		int index = message.indexOf(':');
		descriptionKey = message.substring(1, index);
		System.out.println(descriptionKey);
		
		JSONObject json = new JSONObject(message);
		   JSONObject description = json.getJSONObject(descriptionKey);		   
		   
		   Map<String, String> map = new HashMap<String, String>();
		   
		   Iterator iter = description.keys();
		   while (iter.hasNext()) {
			String key = (String) iter.next();
			String value = description.getString(key);
			map.put(key, value);
			System.out.println(key + "----" + value);
		}

	}

}
