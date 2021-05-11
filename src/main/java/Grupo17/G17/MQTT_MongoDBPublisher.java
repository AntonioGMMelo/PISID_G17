package Grupo17.G17;

import java.util.Arrays;

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

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

//MongoDBPublisher for MQTT
public class MQTT_MongoDBPublisher implements MqttCallbackExtended{
	
	private final String serverURI;
	private MqttClient client;
	private final MqttConnectOptions mqttOptions;
	private MongoClient mongoClient;
	private MongoDatabase mongoDataBase;
	private MongoCollection<Document> mongoCollection;
	
	
	
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
	 
	 public void iniciar() {
		 try {
			 System.out.println("A conectar-se ao broker mqtt" + serverURI);
			 client = new MqttClient(serverURI, String.format("cliente_java_%d", System.currentTimeMillis()), new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir")));
			 client.setCallback(this);
			 client.connect(mqttOptions);
		 }catch (MqttException ex) {
			System.out.println("Erro");
		}
	 }
	 
	 public void publicar(String topic, byte[] payload, int qos) throws MqttException {
	        publicar(topic, payload, qos, false);
	    }
	 
	 public synchronized void publicar(String topic, byte[] payload, int qos, boolean retained) throws MqttException {
		 client = new MqttClient("tcp://localhost:27017", MqttClient.generateClientId());
		 try {
			 if(client.isConnected()) {
				 System.out.println("esta ligado");
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
