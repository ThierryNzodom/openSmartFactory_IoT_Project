package de.hs_lu.fetchMsgBrokerInDB;

import java.sql.Timestamp;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import de.hs_lu.jdbcConnection.NoConnectionException;

public class MsgMqttBroker extends AppInstallTestTable {
	Timestamp timeStampID;
	String descriptionKey;
	String cut = "";
//	String moduleDescriptiotn;
//	String kpiName;
//	String kpiValue;	
	
	public MsgMqttBroker() throws NoConnectionException {
		super();
	}
	@SuppressWarnings("rawtypes")
	public MsgMqttBroker(String jsonString) throws NoConnectionException {
		super();
		try {
			
			//jsonString = "{pilz.lager:{PortLeftId[6]:65}}";
			if(jsonString.contains("]")){				
				int vorKlammer = jsonString.indexOf("[");
				int nachklammer = jsonString.indexOf("]");
				cut = jsonString.substring(vorKlammer, nachklammer+1);
				
				jsonString = jsonString.replace(cut, "");
			}
			
		   JSONObject json = new JSONObject(jsonString.toString());		
		    int index = jsonString.indexOf(':');
		    descriptionKey = jsonString.substring(1, index);
		   
			descriptionKey = descriptionKey.replace("\"","");
			//set the Moduledescription
			this.setModuleDescription(descriptionKey);
			
		//	JSONObject json = new JSONObject(jsonString.toString());
			
			/*Iterator iter = json.keys();
	        this.moduleDescription = iter.next().toString();
	        
	        Iterator iter2 = json.getJSONObject(this.moduleDescription).keys();
	        this.kpiName = iter2.next().toString();
	        
	        JSONObject description = json.getJSONObject(this.moduleDescription);
	        this.kpiValue = description.get(this.kpiName).toString();
	         */  
			
			JSONObject description = json.getJSONObject(descriptionKey);
			   
			   Iterator iter = description.keys();
			   while (iter.hasNext()) {
				String key = (String) iter.next();
			
				// Set the KpiName
		        this.setKpiName(key + cut);
		           
				String value = description.get(key).toString();				
				
				// Set the kpiValue
		        this.setKpiValue(value);
			}
            
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		
		}	
	}
	
}
