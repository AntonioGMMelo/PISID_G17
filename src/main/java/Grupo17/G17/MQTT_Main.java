package Grupo17.G17;

public class MQTT_Main {

	public static void main(String[] args) throws InterruptedException {
		MQTT_MongoDBPublisher cliente = new MQTT_MongoDBPublisher("tcp://broker.mqtt-dashboard.com:1883");
		
		cliente.iniciar();
		
		new MQTT_MySQLSubscriber(cliente, "topico", 0);
		
		while(true) {
			
			Thread.sleep(1000);
			
			String mensagem = "lolinagem";
			
			cliente.publicar("topico", mensagem.getBytes(), 0);
		}

	}

}
