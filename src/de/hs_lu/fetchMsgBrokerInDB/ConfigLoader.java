package de.hs_lu.fetchMsgBrokerInDB;

import java.io.*;
import java.lang.Integer;

public class ConfigLoader {
		
	public String broker;
     String clientId;
     String topic;
     int qos;
     File file = new File("./config.txt");
     
		public ConfigLoader() {
			super();
		}
	    
		public ConfigLoader(String broker, String clientId, String topic,
				int qos, File file) {
			super();
			this.broker = broker;
			this.clientId = clientId;
			this.topic = topic;
			this.qos = qos;
			this.file = file;
		}

		public boolean isFileAvailable() {	    	
	        if (file.exists()) {
	            return true;	          
	        }else{
	            return false;
	        }
	    }
		
		public File getFile() {
			return this.file;
		}
	    public String getBroker() {
			return this.broker;
		}
		public String getClientId() {
			return this.clientId;
		}
		public String getTopic() {
			return this.topic;
		}
		public int getQos() {
			return this.qos;
		}

		public void loadFile() throws FileNotFoundException {
			 FileReader fReader = new FileReader(this.getFile());
			 System.out.println("\nConfigurationsdatei wird ausgelesen: ");
	        try{
	        	BufferedReader bReader = new BufferedReader(fReader);
//	            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("config.txt")));
	            String row = "";

	            while( (row = bReader.readLine()) != null){
	            	
	            	int sepIndex = row.indexOf(":");
	            	String paraNameBroker = row.substring(0, row.indexOf(":"));
	            	String paramValue = row.substring(sepIndex+1).replace(" ", "");

	            	if(paraNameBroker.equals("MqttBroker")){
	            		this.broker = paramValue;
	            	}
	            	else if (paraNameBroker.equals("Topic")){
	            		this.topic = paramValue;
	            	}
	            	else if (paraNameBroker.equals("ClientID")){
	            		this.clientId = paramValue;
	            	}
	            	else if (paraNameBroker.equals("Qos")){
	            		this.qos = Integer.parseInt(paramValue.toString(), 16);
	            	}	            	
	            }
	            	bReader.close();
	            	
	        }catch(IOException e){
	            e.printStackTrace();
	        }
	    }
}
