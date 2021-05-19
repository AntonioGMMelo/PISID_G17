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


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

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
	ArrayList<Document> medicoes = new ArrayList<Document>();
	private String lastTimestamp_zona1;
	private String lastTimestamp_zona2;
	private boolean initial = true;
	com.mongodb.client.MongoClient mongo = MongoClients.create("mongodb://127.0.0.1:27017");
	public MongoDatabase db = mongo.getDatabase("EstufaDB");

	@Override
	public synchronized void run() {
		try {
			while(true) {
				wait(PERIODICIDADE);
				getStuff();
				sendStuff(medicoes);
				this.medicoes = null;
			}
		} catch (SQLException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized void getStuff() throws SQLException{
		FindIterable<Document> myCursor1 = db.getCollection("Zona1").find();
		FindIterable<Document> myCursor2 = db.getCollection("Zona2").find();

		ArrayList<Document> listaMedicoes_zona1 = getMedicoes(myCursor1, 1);
		ArrayList<Document> listaMedicoes_zona2 = getMedicoes(myCursor2, 2);

		for(Document doc : listaMedicoes_zona1) {
			this.medicoes.add(doc);
		}
		for(Document doc : listaMedicoes_zona2) {
			this.medicoes.add(doc);
		}
	}

	public ArrayList<Document> getMedicoes(FindIterable<Document> myCursor, int zona) {
		ArrayList<Document> listaMedicoes = new ArrayList<Document>();
		Iterator doc = myCursor.iterator();
		String timestamp_zona1 = null;
		String timestamp_zona2 = null;
		Boolean semaforo_zona1 = true;
		Boolean semaforo_zona2 = true;
		while(doc.hasNext()) {

			Document medicao = (Document) doc.next();
			String[] array = medicao.toString().split(",");
			String timestamp = array[3].split("=")[1];
			String ultimo_timestamp;

			if(zona == 1) {ultimo_timestamp = this.lastTimestamp_zona1;}
			else {ultimo_timestamp = this.lastTimestamp_zona2;}


			if(checkTimestamp(timestamp, ultimo_timestamp)) {
				listaMedicoes.add(medicao);
				if(zona == 1 && semaforo_zona1) {
					timestamp_zona1 = array[3].split("=")[1];
					semaforo_zona1 = false;
				}
				if(zona == 2 && semaforo_zona2){
					timestamp_zona2 = array[3].split("=")[1];
					semaforo_zona2 = false;
				}
			}
			else {
				System.out.println("Esta medição já foi enviada");
				break;
			}
		}
		if(timestamp_zona1 != null) {
			this.lastTimestamp_zona1 = timestamp_zona1;
		}
		else if(timestamp_zona2 != null) {
			this.lastTimestamp_zona2 = timestamp_zona2;
		}
		return listaMedicoes;
	}

	public Boolean checkTimestamp(String timestamp, String ultimo_timestamp) {
		String[] t = timestamp.split(" at ");
		String[] data = t[0].split(":");
		String[] hora = t[1].split("/");

		if(ultimo_timestamp == null) {
			return true;
		}
		String[] ultimo_t = ultimo_timestamp.split(" at ");
		String[] ultima_data = ultimo_t[0].split(":");
		String[] ultima_hora = ultimo_t[1].split("/");

		if(ultimo_timestamp.equals(ultimo_timestamp)) {
			return false;
		}
		else {
			if(Integer.parseInt(data[2]) <= Integer.parseInt(ultima_data[2])) {
				if(Integer.parseInt(data[1]) <= Integer.parseInt(ultima_data[1])) {
					if(Integer.parseInt(data[0]) <= Integer.parseInt(ultima_data[0])) {
						if(Integer.parseInt(hora[0]) <= Integer.parseInt(ultima_hora[0])) {
							if(Integer.parseInt(hora[1]) <= Integer.parseInt(ultima_hora[1])) {
								if(Integer.parseInt(hora[2]) < Integer.parseInt(ultima_hora[1])) {return false;}
								else {return true;}
							}
							else {return true;}
						}
						else {return true;}
					}
					else {return true;}
				}
				else {return true;}
			}
			else {return true;}
		}
	}

	public synchronized void sendStuff(ArrayList<Document> medicoes) throws SQLException { //falta guardar o lastTimestamp e colocar o timestamp na tabela
		Connection conn = null;
		Statement stm = null;

		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/EstufaDB", "root", "");
		stm = conn.createStatement();

		String inserir;

		if(this.medicoes != null) {
			for(Document m : this.medicoes) {
				inserir = "INSERT INTO Medicao (Leitura, Valido, Zona_ID, Sensor_ID)" + "\r\n"
						+ "VALUES (" + "'" + Double.parseDouble(m.toString().split(", ")[4].split("=")[1].split("}")[0]) + "'" + "," +  "'1'" + "," 
						+ "'" + m.toString().split(", ")[1].split("=")[1] + "'" + "," + "'" + m.toString().split(", ")[2].split("=")[1]  + "'" + ")";
				System.out.println(inserir);
				stm.executeUpdate(inserir);
			}
		}
		else {
			System.out.println("Não existem novas medições em ambas as zonas");
		}

		conn.close();
	}
}