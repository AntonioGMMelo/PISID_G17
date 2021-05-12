package Grupo17.G17;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MQTT_Main {
	
	

	public static void main(String[] args) throws InterruptedException, MqttException, FileNotFoundException, IOException {
		
		Properties p = new Properties();
		p.load(new FileInputStream("SimulateSensor.ini"));
		
		String cloudServerName = p.getProperty("cloud_server");
		String cloudTopicName = p.getProperty("cloud_topic");
		
		MQTT_MongoDBPublisher cliente = new MQTT_MongoDBPublisher(cloudServerName);
		
		//cliente.iniciar();
		cliente.connectCloud(p);
		
		new MQTT_MySQLSubscriber(cliente, cloudTopicName, 0);
		
		while(true) {
			
			Thread.sleep(1000);
			
		//	String mensagem = p.getProperty("Zona ," + "Sensor ," + "LimiteInferior ," + "LimiteSuperior ," + "ValorInicial");
			
			String mensagem = "exemplo";
			cliente.publicar(cloudTopicName, mensagem.getBytes(), 0);
		}

	}

}
