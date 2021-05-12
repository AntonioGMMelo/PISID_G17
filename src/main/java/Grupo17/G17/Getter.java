package Grupo17.G17;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.internal.ConnectActionListener;

import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Getter extends Thread{
	public static final int PERIODICIDADE = 1000;
	private long timestamp;
	private Document[] medicoes;
	private long lastTimestamp;
	private boolean initial = true;
	com.mongodb.client.MongoClient mongo = MongoClients.create("mongodb://127.0.0.1:27017");
	public MongoDatabase db = mongo.getDatabase("EstufaDB");

	@Override
	public synchronized void run() {
		try {
			while(true) {
				getStuff();
				while(medicoes[0] == null)
					wait(PERIODICIDADE);
				sendStuff(medicoes);
			}
		} catch (SQLException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private synchronized void getStuff() throws SQLException {
		FindIterable<Document> myCursor1 = db.getCollection("Zona1").find();
		FindIterable<Document> myCursor2 = db.getCollection("Zona2").find();

		int a = 0;
		Document[] aux = new Document[10];
		while(a < 3) {
			Document obj = myCursor1.iterator().next();
			if(initial) {
				aux[0] = obj;
				initial = false;
			}
			else {
				int i = 0;
				while(i < aux.length) { 
					if(aux[i] == null) { 
						aux[i] = obj;
						i = aux.length;
					}
					else
						i++;
				}
			}
			
			medicoes = aux;
			System.out.println(medicoes[0].toString());
			notifyAll();
			a++;
		}
		
		
		while(myCursor2.iterator().hasNext()) {
			Document obj = myCursor2.iterator().next();
			System.out.println("chegou2");
			Document obj2 = myCursor2.iterator().next();
			medicoes[medicoes.length+1] = obj;
			notifyAll();
		}
		
		System.out.println(medicoes[1]);
	}

	public synchronized void sendStuff(Document[] objects) throws SQLException {
		System.out.println("cheguei");
		Connection conn = null;
		Statement stm = null;

		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/EstufaDB", "root", "");
		stm = conn.createStatement();
		
		int indice = 0;
		for(int i = 0; i < medicoes.length; i++)
			if(objects[i]!=null)
				if(objects[i].get("DataHora").equals(lastTimestamp + ""))
					indice = i;
		
		String inserir = null;
		
		for(int i = indice; i < objects.length - 1; i++) {
//			timestamp = Long.parseLong(objects[0].getString("DataHora"));
			inserir = "INSERT INTO Medição (Leitura, Valido, Zona_ID, Sensor_ID)" + "\r\n"
					+ "VALUES (" + "'" + objects[0].getDouble("Medicao") + "'" + "," +  "'1'" + "," 
					+ "'" + objects[0].getString("Zona") + "'" + "," + "'" + objects[0].getString("Sensor")  + "'" + ")";
			System.out.println(inserir);
			stm.executeUpdate(inserir);
		}
		
//		timestamp = Long.parseLong(objects[medicoes.length-1].get("DataHora").toString());
	
		conn.close();
	}
}