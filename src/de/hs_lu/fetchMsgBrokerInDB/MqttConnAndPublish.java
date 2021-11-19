package de.hs_lu.fetchMsgBrokerInDB;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttConnAndPublish {

	        public static void main(String[] args) {

	            String topic        = "tinkerforge/bricklet/temperature/t6d/temperature";
	            String broker       = "tcp://IWILR3-3.campus.fh-ludwigshafen.de:1883";
	            String clientId     = "Gruppe3";
	            int qos             = 0;
	            String content      = "Message from MqttPublishSample";
	           	          
	            MemoryPersistence persistence = new MemoryPersistence();

	            try {
	                MqttClient client = new MqttClient(broker, clientId, persistence);
	                MqttConnectOptions connOpts = new MqttConnectOptions();
	                
	                connOpts.setCleanSession(true);
	                System.out.println("Connecting to broker: " + broker);
	                
	                client.connect(connOpts);
	                System.out.println("Connected");
	                System.out.println("Publishing message: " + content);
	                MqttMessage message = new MqttMessage(content.getBytes());
	                message.setQos(qos);
	                
	                client.publish(topic, message);
	                System.out.println("Message published");
	                
	                client.disconnect();
	                System.out.println("Disconnected");
	                System.exit(0);
	                
	            } catch(MqttException me) {
	                System.out.println("reason " + me.getReasonCode());
	                System.out.println("msg " + me.getMessage());
	                System.out.println("loc "+ me.getLocalizedMessage());
	                System.out.println("cause " + me.getCause());
	                System.out.println("excep "+ me);
	                me.printStackTrace();
	            }
	        }	    
}
