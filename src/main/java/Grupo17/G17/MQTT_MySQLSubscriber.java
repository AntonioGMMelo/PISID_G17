
package Grupo17.G17;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

//MySQLSubscriber for MQTT
public class MQTT_MySQLSubscriber implements IMqttMessageListener {

	MqttMessage m;
	
	public MQTT_MySQLSubscriber(MQTT_MongoDBPublisher cliente, String topico, int qos) {
		cliente.subscribe(qos,this, topico);
		
	}
	
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Mensagem recebida: ");
		System.out.println("\tTópico: " + topic);
		System.out.println("\tMensagem: " + new String(message.getPayload()));
		System.out.println("MandeiSubscriber");

		m=message;
		
		
//		Connection conn = null;
//		Statement stm = null;
//		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/EstufaDB", "root", "");
//		stm = conn.createStatement();
//		
//		String paraInserir = message.toString();
//		
//		String[] split = paraInserir.split(",");
//		   
//	    String helperZona = split[0].split(":")[1].trim();
//	    String helperSensor = split[1].split(":")[1].trim();
//	    String helperData = split[2].split(":", 2)[1].trim();
//	    String helperMedicao = split[3].split(":")[1].trim();
//	    
//		
//		String inserir = null;
//		
//		inserir = "INSERT INTO Medição (Leitura, Valido, Zona_ID, Sensor_ID)" + "\r\n"
//				+ "VALUES (" + "'" + helperMedicao + "'" + "," +  "'1'" + "," 
//				+ "'" + helperZona + "'" + "," + "'" + helperSensor  + "'" + ")";
//		
//		stm.executeUpdate(inserir);
	}
	
	public MqttMessage getMessage() {
		return m;
	}
	
}
