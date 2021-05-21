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
	private Deque<Double> isValidT1 = new LinkedList<Double>();
	private Deque<Double> medicoesT1 = new LinkedList<Double>();
	private Deque<Double> isValidT2 = new LinkedList<Double>();
	private Deque<Double> medicoesT2 = new LinkedList<Double>();
	private Deque<Double> isValidH1 = new LinkedList<Double>();
	private Deque<Double> medicoesH1 = new LinkedList<Double>();
	private Deque<Double> isValidH2 = new LinkedList<Double>();
	private Deque<Double> medicoesH2 = new LinkedList<Double>();
	private Deque<Double> isValidL1 = new LinkedList<Double>();
	private Deque<Double> medicoesL1 = new LinkedList<Double>();
	private Deque<Double> isValidL2 = new LinkedList<Double>();
	private Deque<Double> medicoesL2 = new LinkedList<Double>();
	private int currentId = 1;
	
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
	    double medicao = 0;
	    if(helperMedicao.length()<8) 
	    	medicao = Double.parseDouble(helperMedicao);
	    else
	    	medicao = Double.parseDouble(helperMedicao.substring(0,8));
	    
	    if(helperSensor.equals("T1")) {
	    	
	    	if(medicoesT1.size()>0 && Math.sqrt(Math.pow( medicao - medicoesT1.getLast(),2)) > 1) {

	    		valid = 0;

	    	}


	    	if(valid == 1) {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, medicoesT1).start();
	    		
	    		if(medicoesT1.size()<10) {

	    			medicoesT1.add(medicao);

	    		}else {

	    			medicoesT1.removeFirst();
	    			medicoesT1.add(medicao);

	    		}
	    	}else {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, isValidT1).start();
	    		
	    		
	    	}
	    	
	    	if(isValidT1.size()<20) {

	    		isValidT1.add(valid);

	    	}else {

	    		isValidT1.removeFirst();
	    		isValidT1.add(valid);

	    	}

	    }else if(helperSensor.equals("T2")) {

	    	if(medicoesT2.size()>0 && Math.sqrt(Math.pow( medicao - medicoesT2.getLast(),2)) > 1) {

	    		valid = 0;

	    	}

	    	if(valid == 1) {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, medicoesT2).start();
	    		
	    		if(medicoesT2.size()<10) {

	    			medicoesT2.add(medicao);

	    		}else {

	    			medicoesT2.removeFirst();
	    			medicoesT2.add(medicao);

	    		}
	    	}else {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, isValidT2).start();
	    		
	    	}
	    	
	    	if(isValidT2.size()<20) {

	    		isValidT2.add(valid);

	    	}else {

	    		isValidT2.removeFirst();
	    		isValidT2.add(valid);

	    	}

	    }else if(helperSensor.equals("H1")) {

	    	if(medicoesH1.size()>0 && Math.sqrt(Math.pow( medicao - medicoesH1.getLast(),2)) > 1) {

	    		valid = 0;

	    	}

	    	if(valid == 1) {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, medicoesH1).start();
	    		
	    		if(medicoesH1.size()<10) {
	    			
	    			System.out.println(medicoesH1);
	    			medicoesH1.add(medicao);
	    			System.out.println(medicoesH1);
	    			
	    		}else {

	    			medicoesH1.removeFirst();
	    			medicoesH1.add(medicao);

	    		}
	    	}else {
	    	
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, isValidH1).start();
	    		
	    	}

	    	if(isValidH1.size()<20) {

	    		isValidH1.add(valid);

	    	}else {

	    		isValidH1.removeFirst();
	    		isValidH1.add(valid);

	    	}
	    	
	    }else if(helperSensor.equals("H2")) {

	    	if(medicoesH2.size()>0 && Math.sqrt(Math.pow( medicao - medicoesH2.getLast(),2)) > 1) {

	    		valid = 0;

	    	}

	    	if(valid == 1) {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, medicoesH2).start();
	    		
	    		if(medicoesH2.size()<10) {

	    			medicoesH2.add(medicao);

	    		}else {

	    			medicoesH2.removeFirst();
	    			medicoesH2.add(medicao);

	    		}
	    	}else {
	    		
	    		new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, isValidH2).start();
	    		
	    	}
	    
	    	if(isValidH2.size()<20) {

	    		isValidH2.add(valid);

	    	}else {

	    		isValidH2.removeFirst();
	    		isValidH2.add(valid);

	    	}
	    	
	    }else if(helperSensor.equals("L1")) {

	    		if(medicoesL1.size()>0 && Math.sqrt(Math.pow( medicao - medicoesL1.getLast(),2)) > 1) {

	    			valid = 0;

	    		}

	    		if(valid == 1) {
	    			
	    			new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, medicoesL1).start();
	    			
	    			if(medicoesL1.size()<10) {

	    				medicoesL1.add(medicao);

	    			}else {

	    				medicoesL1.removeFirst();
	    				medicoesL1.add(medicao);

	    			}
	    		}else {
	    			
	    			new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, isValidL1).start();
	    			
	    		}
	    		
	    		if(isValidL1.size()<20) {

	    			isValidL1.add(valid);

	    		}else {

	    			isValidL1.removeFirst();
	    			isValidL1.add(valid);

	    		}

	    		
	   		}else if(helperSensor.equals("L2")) {

	    			if(medicoesL2.size()>0 && Math.sqrt(Math.pow( medicao - medicoesL2.getLast(),2)) > 1) {

	    				valid = 0;

	    			}

	    			

	    			if(valid == 1) {
	    				
	    				new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, medicoesL2).start();
	    				
	    				if(medicoesL2.size()<10) {

	    					medicoesL2.add(medicao);

	    				}else{

	    					medicoesL2.removeFirst();
	    					medicoesL2.add(medicao);

	    				}
	    				
	    			}else {
	    				
	    				new Alertar(currentId,helperZona, helperSensor, medicao, helperData, (int)valid, isValidL2).start();
	    				
	    			}
	    			
	    			if(isValidL2.size()<20) {

	    				isValidL2.add(valid);

	    			}else {

	    				isValidL2.removeFirst();
	    				isValidL2.add(valid);

	    			}
	    			
	    		}
	    
	    
//	    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//	    java.util.Date utilDate = format.parse(rawMsg.split(", ")[3].split("=")[1].split(" ")[0]);
//	    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
	    
//	    if(cliente.getMessage()!=null) {
	   
//	    teste.append("teste", "teste"); 	
	    	
    	//teste.append("Zona", "Z1").append("Sensor", "t1").append("data", "19/05").append("Medicao", "10");
 
	  //  MongoCursor<Document> cursor = localMongoCollection1.find().iterator();
	    
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
	
    public void addMedicoesH1(double toAdd) {
    	
    	medicoesH1.add(toAdd);
    	
    }
}
