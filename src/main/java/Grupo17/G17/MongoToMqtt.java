package Grupo17.G17;

import java.util.Random;
import java.util.UUID;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoToMqtt {

	public static void main(String[] args) throws MqttException, InterruptedException {
		
		
		String cloudServer = "tcp://broker.mqtt-dashboard.com:1883";
		String cloudTopic = "g17";
		
		String clientId = UUID.randomUUID().toString();
		IMqttClient mqttClient = new MqttClient(cloudServer,clientId);
		
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		mqttClient.connect(options);
		
		MongoClient localMongoClient = new MongoClient(new MongoClientURI("mongodb://127.0.0.1:27017"));
		MongoDatabase localMongoDatabase = localMongoClient.getDatabase("EstufaDB");
	    MongoCollection<Document> localMongoCollection1 = localMongoDatabase.getCollection("Zona1");
		
	    long dataAnterior = 0;
	    while (true) {
	    	
	    	Document doc = localMongoCollection1.find().sort(new Document("_id", -1)).first();
	    	String rawMsg = doc.toString();
	    	
	    	System.out.println(rawMsg);
	    	
	    	String[] split = rawMsg.split(",");
	    	
	    	String[] helperData1 = split[3].split("=");
	    
	    	String data = helperData1[1];
	    	
	    	
	    	
	    	long dataAtual = Long.parseLong(data.replace("-", "").replace("T", "").replace(":", "").replace("Z", ""));
	    	
	    	//tirar a data como um long, se for maior que dataAnterior
	    	
			if(dataAnterior<dataAtual) {
	    		byte[] payload = rawMsg.getBytes();
		    
	    		MqttMessage msg = new MqttMessage(payload);
	    		msg.setQos(0);
	    		msg.setRetained(false);
	    		mqttClient.publish(cloudTopic,msg);
	    		
	    		System.out.println(msg);
	    	}
		    
		    Thread.sleep(1000);
	    	
	    }

		
	}

}
