package Grupo17.G17;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

import com.google.protobuf.Timestamp;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MqttToMysql implements MqttCallback {
	
	static IMqttClient mqttClient;
	static String cloudTopic;
	static MongoClient localMongoClient;
	int valido = 1;
	private Deque<Double> isValid = new LinkedList<Double>();
	private Deque<Double> medicoes = new LinkedList<Double>();
	
	public static void main(String[] args) throws InterruptedException, MqttSecurityException, MqttException, FileNotFoundException, IOException, SQLException {
		
		
		
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
		try {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/EstufaDB", "root", "");
	    String rawMsg = arg1.toString();
	    
	    String[] split = rawMsg.split(",");
	    
	   
	    String helperZona = split[1].split("=")[1].trim();
	    
	    
	    String helperSensor = split[2].split("=")[1].trim();
	    
	    String helperData = split[3].split("=", 2)[1].replace("T", " ").replace("Z", "").trim();
	    
	    
	   // Timestamp t = new Timestamp(DateUtil.provideDateFormat().parse(helperData).getTime());
	    
	    System.out.println(helperData);
	    
	    String helperMedicao = split[4].split("=")[1].trim();
	    double valid = 1;
	    double medicao = Double.parseDouble(helperMedicao.substring(0, 8));
	    
	    
	    if(medicoes.size()>0 && Math.sqrt(Math.pow( medicao - medicoes.getLast(),2)) > 1) {
	    	
	    	valid = 0;
	    	
	    }
	    
	    if(isValid.size()<20) {
	    	
	    	isValid.add(valid);
	    	
	    }else {
	    	
	    	isValid.removeFirst();
	    	isValid.add(valid);
	    	
	    }
	    
	    if(valid == 1) {
	    	if(medicoes.size()<10) {

	    		medicoes.add(medicao);

	    	}else {

	    		medicoes.removeFirst();
	    		medicoes.add(medicao);
	    		
	    	}
	    }
	    
	    
	    
//	    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//	    java.util.Date utilDate = format.parse(rawMsg.split(", ")[3].split("=")[1].split(" ")[0]);
//	    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
	    
//	    if(cliente.getMessage()!=null) {
	   
//	    teste.append("teste", "teste"); 	
	    	
    	//teste.append("Zona", "Z1").append("Sensor", "t1").append("data", "19/05").append("Medicao", "10");
 
	  //  MongoCursor<Document> cursor = localMongoCollection1.find().iterator();
	    
	    String addMedicao = "INSERT INTO Medicao (Medicao_ID, Hora, Leitura, Valido, Zona_ID, Sensor_ID)" +
		        "VALUES ( ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement preparedStatement = conn.prepareStatement(addMedicao);
		preparedStatement.setString(1, null);
		BigDecimal d = new BigDecimal(medicao);
		preparedStatement.setString(2, helperData);
		preparedStatement.setBigDecimal(3, d);
		preparedStatement.setInt(4, (int) valid);
		preparedStatement.setString(5, helperZona);
		preparedStatement.setString(6, helperSensor);
		preparedStatement.executeUpdate(); 
		
//	    PreparedStatement preparedStatement = conn.prepareStatement(addMedicao);
//		preparedStatement.setString(1, null);
//		BigDecimal d = new BigDecimal(100);
//		preparedStatement.setString(2, "2021-05-21 13:53:15");
//		preparedStatement.setBigDecimal(3, d);
//		preparedStatement.setInt(4, 1);
//		preparedStatement.setString(5, "Z1");
//		preparedStatement.setString(6, "H1");
//		preparedStatement.executeUpdate(); 
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("qualquer merda");
		}
	}

}
