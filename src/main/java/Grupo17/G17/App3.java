package Grupo17.G17;

import java.io.IOException;
import java.sql.SQLException;

import org.eclipse.paho.client.mqttv3.MqttException;

public class App3 extends Thread {

	
	@Override
	public void run() {
		
    	try {
			MqttToMysql.main(new String[] {});
		} catch (InterruptedException | MqttException | IOException | SQLException e) {
			e.printStackTrace();
			new App3().start();
		}
		
	}
	
	
}