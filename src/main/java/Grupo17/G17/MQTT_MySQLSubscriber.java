
package Grupo17.G17;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

//MySQLSubscriber for MQTT
public class MQTT_MySQLSubscriber implements IMqttMessageListener {

	
	
	public MQTT_MySQLSubscriber(MQTT_MongoDBPublisher cliente, String topico, int qos) {
		cliente.subscribe(qos,this, topico);
		
	}
	
	
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Mensagem recebida: ");
		System.out.println("\tTópico: " + topic);
		System.out.println("\tMensagem: " + new String(message.getPayload()));
		
	}
	
	
	
}
