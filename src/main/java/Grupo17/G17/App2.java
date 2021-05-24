package Grupo17.G17;

import org.eclipse.paho.client.mqttv3.MqttException;

public class App2 extends Thread{

	@Override
	public void run() {
	
    	try {
			MongoToMqtt.main(new String[] {});
			sleep(1000);
			new App3().start();
		} catch (Exception e) {
			e.printStackTrace();
			new App2().start();
			
		}
    	
    	
	}
	

}
