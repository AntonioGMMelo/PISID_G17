
package Grupo17.G17;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MQTT_Main {
	
	public static void main(String[] args) throws InterruptedException, MqttException, FileNotFoundException, IOException {
		String m=null;
		Properties p = new Properties();
		p.load(new FileInputStream("SimulateSensor.ini"));
		
		String cloudServerName = p.getProperty("cloud_server");
		String cloudTopicName = p.getProperty("cloud_topic");
		
		MQTT_MongoDBPublisher cliente = new MQTT_MongoDBPublisher(cloudServerName);
		
		cliente.connectCloud(p);
		
		//MongoClient client1 = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		
	//	MongoClient localMongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017,localhost:25017,localhost:23017/?replicaSet=replicademo"));
//		MongoClient localMongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
//		//MongoDatabase database = localMongoClient.getDatabase("estufaDB");
//		
//		MongoDatabase localMongoDatabase = localMongoClient.getDatabase("hotel");
//		
//		
//		 
//
//	    Document document = new Document();
//	    document.append("name", "Manu");
//	    document.append("age", 22);
//	    document.append("city", "ElvasCity");
//	    MongoCollection<Document> localMongoCollection1 = localMongoDatabase.getCollection("cliente");	    localMongoCollection1.insertOne(document);
//	    
//		MongoCursor<Document> cursor = localMongoCollection1.find().iterator();
//		try {
//		    while (cursor.hasNext()) {
//		        System.out.println(cursor.next().toJson());
//		        m = cursor.next().toJson().toString();
//		    }
//		} finally {
//		    cursor.close();
//		}
		
		MongoClient localMongoClient = new MongoClient(new MongoClientURI("mongodb://127.0.0.1:27017"));
		
		MongoDatabase localMongoDatabase = localMongoClient.getDatabase("EstufaDB");
		
		new MQTT_MySQLSubscriber(cliente, cloudTopicName, 0);
		
		while(true) {
			 
		    MongoCollection<Document> localMongoCollection1 = localMongoDatabase.getCollection("Zona1");	
		//    MongoCollection<Document> localMongoCollection2 = localMongoDatabase.getCollection("Zona2");
		    
			MongoCursor<Document> cursor = localMongoCollection1.find().iterator();
		//	MongoCursor<Document> cursor2 = localMongoCollection1.find().iterator();
			

	    	
			try {
			    while (cursor.hasNext()) {
			    	
			        System.out.println(cursor.next().toJson());
			        
			        m = cursor.next().toJson().toString();
			        
			        String[] split = m.split(",");
			       
			        
			        Document document = new Document();
				    document.append("Zona", split[0]);
				    document.append("Sensor" , split[1]);
				    document.append("Data", split[2]);
				    document.append("Medicao", split[3]);
			        
			        localMongoCollection1.insertOne(document);
			    }
//			    while(cursor2.hasNext()) {
//			    	
//			    }
			} finally {
			    cursor.close();
			}
			
			
			Thread.sleep(1000);
		}

	}

}

