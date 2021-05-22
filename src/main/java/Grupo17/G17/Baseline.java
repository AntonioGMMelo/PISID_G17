package Grupo17.G17;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.util.Arrays;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;

public class Baseline {

    public static void main(String[] args) throws MqttException {
    	
        String cloudServer = "tcp://broker.mqtt-dashboard.com:1883";
        String cloudTopic = "g17";

        MongoClient localMongoClient = new MongoClient(new MongoClientURI("mongodb://127.0.0.1:27017"));

        MongoDatabase localMongoDatabase = localMongoClient.getDatabase("EstufaDB");

        MongoCollection<Document> localMongoCollection1 = localMongoDatabase.getCollection("Zona1");
		
		String clientId = UUID.randomUUID().toString();
		IMqttClient mqttClient = new MqttClient(cloudServer,clientId);
		mqttClient.connect();
		
		long batata= System.currentTimeMillis();
		
    	for(int i= 60; i>=0; i--) {
    		
    		Date date2 = new Date( batata- i*1000);
        	Timestamp startTime = new Timestamp(date2.getTime());
        	String formated= startTime.toString().replace(" ","T").substring(0,19).concat("Z");
        	System.out.println(formated);
        	
        	try {
        	
        	Document pastel = localMongoCollection1.find(eq("Data", formated)).first();
        	
        	System.out.println(pastel);
        	
        	byte[] payload = pastel.toString().getBytes();
		    
    		MqttMessage msg = new MqttMessage(payload);
    		msg.setQos(2);
    		msg.setRetained(false);
    		mqttClient.publish(cloudTopic,msg);
    		
    		System.out.println(msg);
        	
        	}catch(Exception e) {
        
        		e.printStackTrace();
        		
        	}
        }
        	

    }

}
