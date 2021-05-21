package Grupo17.G17;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttException;

//Direct Migration from MongoDB to MYSQL
public class DirectMigration {
	public static void main(String[] args) throws FileNotFoundException, IOException, MqttException {
		Getter getter = new Getter();
		getter.start();
	}
}