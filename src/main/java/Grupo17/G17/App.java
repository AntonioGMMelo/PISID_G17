package Grupo17.G17;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

public class App 
{
    public static void main( String[] args ) {
    	try {
			MqttToMongo.main(args);
			Thread.sleep(1000);
			new App2().start();
		} catch (Exception e) {
			e.printStackTrace();
			App.main(args);
		}
    	
    }
}
