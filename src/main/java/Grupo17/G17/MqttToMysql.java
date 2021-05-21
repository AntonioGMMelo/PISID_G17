package Grupo17.G17;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MqttToMysql implements MqttCallback {
	
	static IMqttClient mqttClient;
	static String cloudTopic;
	static MongoClient localMongoClient;
	int valido = 1;

	public static void main(String[] args) throws InterruptedException, MqttSecurityException, MqttException, FileNotFoundException, IOException, SQLException {
		
		Connection conn = null;
		Statement stm = null;

		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/EstufaDB", "root", "");
		stm = conn.createStatement();
		
		String cloudServer = "tcp://broker.mqtt-dashboard.com:1883";
		cloudTopic = "g17";
		
		String clientId = UUID.randomUUID().toString();
		mqttClient = new MqttClient(cloudServer,clientId);
		
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		mqttClient.connect(options);
		
		new MqttToMysql().subscribe();

	}
	
	public void subscribe() throws MqttSecurityException, MqttException {
		mqttClient.setCallback(this);
		mqttClient.subscribe(cloudTopic);
	}

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		System.out.println(arg1.toString());
		
		Connection conn = null;
		Statement stm = null;
		String inserir=null;

		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/EstufaDB", "root", "");
		stm = conn.createStatement();
		
		
//		MongoDatabase localMongoDatabase = localMongoClient.getDatabase("EstufaDB");
//		 
//	    MongoCollection<Document> localMongoCollection1 = localMongoDatabase.getCollection("Zona1");
		
		
	    
	    Document teste = new Document();
	    
	    String rawMsg = arg1.toString();
	    
	    String[] split = rawMsg.split(",");
	   
	    String helperZona = split[0].split(":")[1].trim();
	    String helperSensor = split[1].split(":")[1].trim();
	    String helperData = split[2].split(":", 2)[1].trim();
	    String helperMedicao = split[3].split(":")[1].trim();
	    
	    teste.append("Zona", helperZona);
	    teste.append("Sensor" , helperSensor);
	    teste.append("Data", helperData);
	    teste.append("Medicao", helperMedicao);
	    
	    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	    java.util.Date utilDate = format.parse(rawMsg.split(", ")[3].split("=")[1].split(" ")[0]);
	    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
	    
//	    if(cliente.getMessage()!=null) {
	   
//	    teste.append("teste", "teste"); 	
	    	
    	//teste.append("Zona", "Z1").append("Sensor", "t1").append("data", "19/05").append("Medicao", "10");
 
	  //  MongoCursor<Document> cursor = localMongoCollection1.find().iterator();
	    
	    inserir = "INSERT INTO Medicao (Hora, Leitura, Valido, Zona_ID, Sensor_ID)" + "\r\n"
				+ "VALUES (" + "'" + sqlDate + " " + helperData + "'," 
				+ "'" + helperMedicao + "'" + "," +  valido + "," 
				+ "'" + helperZona + "'" + "," + "'" +
				helperSensor + "'" + ")";
	    
		System.out.println(inserir);
		stm.executeUpdate(inserir);

		
	}

}
