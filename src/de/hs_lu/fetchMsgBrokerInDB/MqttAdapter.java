package de.hs_lu.fetchMsgBrokerInDB;

import java.io.FileNotFoundException;
import java.sql.Connection;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import de.hs_lu.jdbcConnection.NoConnectionException;

public class MqttAdapter {

Connection dbConn;
	
	public MqttAdapter() throws NoConnectionException {
		
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("==========================================================");
		System.out.println("Mqtt-Client mit einer Anbindung zu einer MySQl Datenbank");
		System.out.println("==========================================================");
	    ConfigLoader configFile = new ConfigLoader();
	    configFile.loadFile();
	   // CsvToDb csfToDb = new CsvToDb();
	    /*try {
			csfToDb.MsgToDb();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    
	    int qos             = configFile.getQos();
	    String broker	 	= configFile.getBroker();
	    String clientId     = configFile.getClientId();
	    String topic		= configFile.getTopic();
	    MemoryPersistence persistence = new MemoryPersistence();		     
	    
	    System.out.println("Die MQTT Broker-Adresse lautet: " + broker);

	    try {
	    	// Konfiguration der Verbindung zum MQTT Broker
        MqttClient client = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        
 
        connOpts.setCleanSession(true);
        connOpts.setAutomaticReconnect(false);
       
        System.out.println("Verbindung zum MQTT Broker wird aufgebaut...");		                
        
        client.setCallback(new MqttCallback() {
        	
        	public void connectionLost(Throwable cause) { 
        		System.out.println("Verbindung zum MQTT Broker wurde unterbrochen: " + cause);
        		//Called when the client lost the connection to the broker 
            }
            public void messageArrived(String topic, MqttMessage msg) throws Exception {
            	System.out.println("Verbunden:");
            	System.out.println("");
            	System.out.println(msg.toString());
		    	MsgMqttBroker testObj = new MsgMqttBroker(msg.toString());
		    	
		    	String moduleDescription = testObj.getModuleDescription();    	
		    	String kpiName = testObj.getKpiName();
		    	String kpiValue = testObj.getKpiValue();    	
		    	
		    	System.out.println("Aus der Nachricht gelesene Daten:");
		    	System.out.println("moduleDescription \t: " + moduleDescription);
		    	System.out.println("kpiName \t\t: " + kpiName);
		    	System.out.println("kpiValue \t\t: " + kpiValue);
		    	System.out.println();
		    	    	
		//    	testObj.dropTableTest();
		//		testObj.createTableTest();		   
		    	testObj.insertTableTest();
		//    	testObj.readTableTest();
            }
        
        public void deliveryComplete(IMqttDeliveryToken token) {	//Called when a outgoing publish is complete 
          System.out.println("Delivery Complete");
         }
         
        });
                
        client.connect(connOpts);
        client.subscribe(topic, qos);
            
            } catch(MqttException me) {
                System.out.println("Reason " + me.getReasonCode());
                System.out.println("Msg " + me.getMessage());
                System.out.println("Loc " + me.getLocalizedMessage());
                System.out.println("Cause " + me.getCause());
                System.out.println("Exception " + me);
                me.printStackTrace();
            }
        	 //System.exit(0);     
	}

}
