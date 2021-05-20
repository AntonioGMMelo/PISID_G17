
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
		String m;
		Properties p = new Properties();
		p.load(new FileInputStream("SimulateSensor.ini"));
		
		String cloudServerName = p.getProperty("cloud_server");
		String cloudTopicName = p.getProperty("cloud_topic");
		
		MQTT_MongoDBPublisher cliente = new MQTT_MongoDBPublisher(cloudServerName);
		
		cliente.connectCloud(p);
		
		MongoClient localMongoClient = new MongoClient(new MongoClientURI("mongodb://127.0.0.1:27017"));
		
		
		Document teste = new Document();
		
		new MQTT_MySQLSubscriber(cliente, cloudTopicName, 0);
		
			
		
		while(true) {
			
		//    MongoCollection<Document> localMongoCollection2 = localMongoDatabase.getCollection("Zona2");
		    
		    
		//	MongoCursor<Document> cursor2 = localMongoCollection1.find().iterator();
//			Document document = cursor.next();

//	    	
		  //  System.out.println(cliente.getMessage());
	//	    MqttMessage message = cliente.getMessage();
		//    System.out.println(cliente.getMessage());
//	    	localMongoCollection1.insertOne(document);
//			Document document = new Document();
		    
			MongoDatabase localMongoDatabase = localMongoClient.getDatabase("EstufaDB");
			 
		    MongoCollection<Document> localMongoCollection1 = localMongoDatabase.getCollection("Zona1");
		  
		   System.out.println(cliente.getMessage());
		   	m = cliente.getMessage().toString();
		    
		    
		    teste = new Document();
		    
		    String[] split = m.split(",");
		   
		    String helperZona = split[0].split(":")[1].trim();
		    String helperSensor = split[1].split(":")[1].trim();
		    String helperData = split[2].split(":", 2)[1].trim();
		    String helperMedicao = split[3].split(":")[1].trim();
		    
		    teste.append("Zona", helperZona);
		    teste.append("Sensor" , helperSensor);
		    teste.append("Data", helperData);
		    teste.append("Medicao", helperMedicao);
		    
//		    if(cliente.getMessage()!=null) {
		   
//		    teste.append("teste", "teste");
		    	
		    	
	    	//teste.append("Zona", "Z1").append("Sensor", "t1").append("data", "19/05").append("Medicao", "10");
	    	

		    
		  //  MongoCursor<Document> cursor = localMongoCollection1.find().iterator();
		    
		    localMongoCollection1.insertOne(teste);
		    
//		    }    
		    
		    
		//    System.out.println(cursor.next().toJson());
		    
		  //  m=cursor.next();
		    
		   
		   
		    
		    
				
//		    System.out.println(cursor.next().toJson());
//			        localMongoCollection1.insertOne(cursor.next());
//			        
//			        localMongoCollection1.insertOne(cursor.next());
			        
			        
//			        m = cursor.next().toJson();
//
//                    String[] split = m.split(",");
//
//                    String helperZona = split[0].split(":")[1].trim();
//                    String helperSensor = split[1].split(":")[1].trim();
//                    String helperData = split[2].split(":", 2)[1].trim();
//                    String helperMedicao = split[3].split(":")[1].trim();
//
//                    document.append("Zona", helperZona);
//                    document.append("Sensor" , helperSensor);
//                    document.append("Data", helperData);
//                    document.append("Medicao", helperMedicao);
//
//                    localMongoCollection1.insertOne(document);
//			        
			Thread.sleep(1000);
		}

	}

}

