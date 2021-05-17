package Grupo17.G17;

import java.awt.List;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import javax.swing.text.Document;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

//MongoDBPublisher for MQTT
public class MQTT_MongoDBPublisher implements MqttCallbackExtended{
	
	private final String serverURI;
	private MqttClient client;
	private final MqttConnectOptions mqttOptions;
	private boolean initial = true;
	private Document[] medicoes;
	com.mongodb.client.MongoClient mongo = MongoClients.create("mongodb://127.0.0.1:27017");
	public MongoDatabase db = mongo.getDatabase("EstufaDB");
	
	
	
	public MQTT_MongoDBPublisher(String serverURI) {
		
		this.serverURI=serverURI;
		mqttOptions = new MqttConnectOptions();
		mqttOptions.setConnectionTimeout(3);
		mqttOptions.setKeepAliveInterval(10);
		mqttOptions.setAutomaticReconnect(true);
		mqttOptions.setCleanSession(false);
		
	}
	
	 public IMqttToken subscribe(int qos, IMqttMessageListener gestorMensagemMQTT, String... topicos) {
	        if (client == null || topicos.length == 0) {
	            return null;
	        }
	        int tamanho = topicos.length;
	        int[] qoss = new int[tamanho];
	        IMqttMessageListener[] listners = new IMqttMessageListener[tamanho];

	        for (int i = 0; i < tamanho; i++) {
	            qoss[i] = qos;
	            listners[i] = gestorMensagemMQTT;
	        }
	        try {
	            return client.subscribeWithResponse(topicos, qoss, listners);
	        } catch (MqttException ex) {
	            System.out.println(String.format("Erro ao inscrever-se nos topicos %s - %s", Arrays.asList(topicos), ex));
	            return null;
	        }
	    }
	 
	 
	 public void publicar(String topic, byte[] payload, int qos) throws MqttException {
	        publicar(topic, payload, qos, false);
	 }
	 
//	 public void connectMongo() {
//		 MongoClient client = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
//		 MongoDatabase database = client.getDatabase("estufaDB");
//		 database.getCollection("zona1");
//		 database.getCollection("zona2");
//		 
//		 System.out.println("connected to mongo");
//		 
//	 }
	 
	 public void connectCloud(Properties p) throws MqttException {
		 String cloudServerName = p.getProperty("cloud_server");
		 String cloudTopicName = p.getProperty("cloud_topic");
		 
		 int i= new Random().nextInt(1000);
		 try {
			 client = new MqttClient(cloudServerName, "CloudToMongo_" + String.valueOf(i) +"_" + cloudTopicName);
			 client.connect();
			 client.setCallback(this);
			 client.subscribe(cloudTopicName);
			 
		 }catch (MqttException e) {
			e.printStackTrace();
		}
		  
	 }
	 
//	public synchronized void getMensagem() {
//		
//		FindIterable<Document> mycursor1 = (FindIterable<Document>) db.getCollection("Zona1");
//		FindIterable<Document> mycursor2 = (FindIterable<Document>) db.getCollection("Zona2");
//		
//		int a = 0;
//		
//		while(a<3) {
//			Document[] aux = new Document[1000];
//			Document obj = mycursor1.iterator().next();
//			if(initial) {
//				aux[0] = obj;
//				initial = false;
//			}
//			else	
//				for(int i = 0; i < aux.length; i++) {
//					if(aux[i] == null) {
//						System.out.println("devo");
//						aux[i] = obj;
//						i = aux.length;
//					}
//				}
//			
//			medicoes = aux;
//			System.out.println(medicoes[0].toString());
//			a++;
//		}
//		
//		
//		while(mycursor2.iterator().hasNext()) {
//			System.out.println("chegou2");
//			Document obj = mycursor2.iterator().next();
//			medicoes[medicoes.length+1] = obj;
//		}
//		
//		System.out.println(medicoes[1]);
//			
//	}
	 

	public synchronized void publicar(String topic, byte[] payload, int qos, boolean retained) throws MqttException {
		
		 try {
			 if(client.isConnected()) {
				 client.publish(topic, payload, qos, retained);
				 System.out.println("Topico publicado");
			 }else {
				 System.out.println("Nao foi possivel publicar o topico");
			 }
		 }catch (MqttException ex) {
			System.out.println("Erro ao publicar");
		}
	 }
	 

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Conexão com o broker perdida" + cause);
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {		
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		System.out.println("Client MQTT" + (reconnect ? "reconectado" : "conectado") + "com o broker" +serverURI);
	}

}
