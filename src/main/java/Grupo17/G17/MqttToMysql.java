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
import java.sql.ResultSet;
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
	private int currentId = 1;
	double lastMedicao = 0;
	
	
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
	    
	    System.out.println(helperData);
	    
	    String helperMedicao = split[4].split("=")[1].trim().replace("}","");
	    double valid = 1;
	    double medicao = 0;
	    if(helperMedicao.length()<7) 
	    	medicao = Double.parseDouble(helperMedicao);
	    else
	    	medicao = Double.parseDouble(helperMedicao.substring(0,7));
	    
	      String query2="SELECT Medicao.Leitura, Medicao.Medicao_ID"+
	    		  " FROM Medicao WHERE medicao.Zona_ID ='"+helperZona+"'ORDER BY Medicao_ID DESC LIMIT 1";
	     
	      Statement st2 = conn.createStatement();
	      
	      // execute the query, and get a java resultset
	      ResultSet rs2 = st2.executeQuery(query2);
	      
	      
	      int index = 0;
	      
	      if(rs2.next()) {
	    	  lastMedicao = rs2.getDouble("Leitura");
	    	  currentId=rs2.getInt("Medicao_ID");
	      }
	      
	      st2.close();
	    
	    
	    
	    if(helperSensor.equals("T1")) {
	    	
	    	if(Potato.getMedicoesT1().size()>0 && Math.sqrt(Math.pow( medicao - lastMedicao,2)) > 1.5) {

	    		valid = 0;

	    	}


	    	if(valid == 1) {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, Potato.getMedicoesT1()).start();
	    		
	    		if(Potato.getMedicoesT1().size()<10) {

	    			Potato.getMedicoesT1().add(medicao);

	    		}else {

	    			Potato.getMedicoesT1().removeFirst();
	    			Potato.getMedicoesT1().add(medicao);

	    		}
	    	}else {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, Potato.getIsValidT1()).start();
	    		
	    		
	    	}
	    	
	    	if(Potato.getIsValidT1().size()<20) {

	    		Potato.getIsValidT1().add(valid);

	    	}else {

	    		Potato.getIsValidT1().removeFirst();
	    		Potato.getIsValidT1().add(valid);

	    	}

	    }else if(helperSensor.equals("T2")) {

	    	if(Potato.getMedicoesT2().size()>0 && Math.sqrt(Math.pow( medicao - lastMedicao,2)) > 1) {

	    		valid = 0;

	    	}

	    	if(valid == 1) {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, Potato.getMedicoesT2()).start();
	    		
	    		if(Potato.getMedicoesT2().size()<10) {

	    			Potato.getMedicoesT2().add(medicao);

	    		}else {

	    			Potato.getMedicoesT2().removeFirst();
	    			Potato.getMedicoesT2().add(medicao);

	    		}
	    	}else {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, Potato.getIsValidT2()).start();
	    		
	    	}
	    	
	    	if(Potato.getIsValidT2().size()<20) {

	    		Potato.getIsValidT2().add(valid);

	    	}else {

	    		Potato.getIsValidT2().removeFirst();
	    		Potato.getIsValidT2().add(valid);

	    	}

	    }else if(helperSensor.equals("H1")) {

	    	if(Potato.getMedicoesH1().size()>0 && Math.sqrt(Math.pow(medicao - lastMedicao,2)) > 1) {
	    		System.out.println(lastMedicao+"|"+ medicao);
	    		
	    		valid = 0;

	    	}

	    	if(valid == 1) {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, Potato.getMedicoesH1()).start();
	    		
	    		if(Potato.getMedicoesH1().size()<10) {
	    			
	    			Potato.getMedicoesH1().add(medicao);
	    			
	    		}else {

	    			Potato.getMedicoesH1().removeFirst();
	    			Potato.getMedicoesH1().add(medicao);

	    		}
	    	}else {
	    	
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, Potato.getIsValidH1()).start();
	    		
	    	}

	    	if(Potato.getIsValidH1().size()<20) {

	    		Potato.getIsValidH1().add(valid);

	    	}else {

	    		Potato.getIsValidH1().removeFirst();
	    		Potato.getIsValidH1().add(valid);

	    	}
	    	
	    }else if(helperSensor.equals("H2")) {

	    	if(Potato.getMedicoesH2().size()>0 && Math.sqrt(Math.pow( medicao - lastMedicao,2)) > 1) {

	    		valid = 0;

	    	}

	    	if(valid == 1) {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, Potato.getMedicoesH2()).start();
	    		
	    		if(Potato.getMedicoesH2().size()<10) {

	    			Potato.getMedicoesH2().add(medicao);

	    		}else {

	    			Potato.getMedicoesH2().removeFirst();
	    			Potato.getMedicoesH2().add(medicao);

	    		}
	    	}else {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, Potato.getIsValidH2()).start();
	    		
	    	}
	    
	    	if(Potato.getIsValidH2().size()<20) {

	    		Potato.getIsValidH2().add(valid);

	    	}else {

	    		Potato.getIsValidH2().removeFirst();
	    		Potato.getIsValidH2().add(valid);

	    	}
	    	
	    }else if(helperSensor.equals("L1")) {

	    		if(Potato.getMedicoesL1().size()>0 && Math.sqrt(Math.pow( medicao - lastMedicao,2)) > 1) {

	    			valid = 0;

	    		}

	    		if(valid == 1) {
	    			
	    			new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, Potato.getMedicoesL1()).start();
	    			
	    			if(Potato.getMedicoesL1().size()<10) {

	    				Potato.getMedicoesL1().add(medicao);

	    			}else {

	    				Potato.getMedicoesL1().removeFirst();
	    				Potato.getMedicoesL1().add(medicao);

	    			}
	    		}else {
	    			
	    			new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, Potato.getIsValidL1()).start();
	    			
	    		}
	    		
	    		if(Potato.getIsValidL1().size()<20) {

	    			Potato.getIsValidL1().add(valid);

	    		}else {

	    			Potato.getIsValidL1().removeFirst();
	    			Potato.getIsValidL1().add(valid);

	    		}

	    		
	   		}else if(helperSensor.equals("L2")) {

	    			if(Potato.getMedicoesL2().size()>0 && Math.sqrt(Math.pow( medicao - lastMedicao,2)) > 1) {

	    				valid = 0;

	    			}

	    			

	    			if(valid == 1) {
	    				
	    				new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, Potato.getMedicoesL2()).start();
	    				
	    				if(Potato.getMedicoesL2().size()<10) {

	    					Potato.getMedicoesL2().add(medicao);

	    				}else{

	    					Potato.getMedicoesL2().removeFirst();
	    					Potato.getMedicoesL2().add(medicao);

	    				}
	    				
	    			}else {
	    				
	    				new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, Potato.getIsValidL2()).start();
	    				
	    			}
	    			
	    			if(Potato.getIsValidL2().size()<20) {

	    				Potato.getIsValidL2().add(valid);

	    			}else {

	    				Potato.getIsValidL2().removeFirst();
	    				Potato.getIsValidL2().add(valid);

	    			}
	    			
	    		}
	    
	    

	    String addMedicao = "INSERT INTO Medicao (Hora, Leitura, Valido, Zona_ID, Sensor_ID)" +
		        "VALUES (?, ?, ?, ?, ?)";
	    
		PreparedStatement preparedStatement = conn.prepareStatement(addMedicao);
		BigDecimal d = new BigDecimal(medicao);
		preparedStatement.setString(1, helperData);
		preparedStatement.setBigDecimal(2, d);
		preparedStatement.setInt(3, (int) valid);
		preparedStatement.setString(4, helperZona);
		preparedStatement.setString(5, helperSensor);
		preparedStatement.executeUpdate(); 
		currentId++;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("qualquer merda");
		}
	}
}
