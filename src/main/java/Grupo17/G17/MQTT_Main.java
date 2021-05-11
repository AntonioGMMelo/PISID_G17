package Grupo17.G17;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.mongodb.client.*;

public class MQTT_Main {

	public static void main(String[] args) throws InterruptedException, MqttException {
		MQTT_MongoDBPublisher clienteMqtt = new MQTT_MongoDBPublisher("tcp://broker.mqtt-dashboard.com:1883");
		
		clienteMqtt.iniciar();
		
		new MQTT_MySQLSubscriber(clienteMqtt, "topico", 0);
		
		while(true) {
			
			Thread.sleep(1000);
			
			String mensagem = ""; // lolinagem
			
			clienteMqtt.publicar("topico", mensagem.getBytes(), 0);
		}

	}

}
