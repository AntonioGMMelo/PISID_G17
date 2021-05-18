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
//	private long timestamp;
	
	private String timestamp = " ";
	
	private Document[] medicoes;
	private Document[] medicoes1;
	private Document[] medicoes2;
	private long lastTimestamp;
	private boolean initial = true;
	
	private String lastTimestamp1;
    private String lastTimestamp2;
	
    com.mongodb.client.MongoClient mongo = MongoClients.create("mongodb://127.0.0.1:27017");
	public MongoDatabase db = mongo.getDatabase("EstufaDB");

	@Override
	public synchronized void run() {
		try {
			//			while(true) {
//			getStuff();
			while(true) {
				get();
				wait(PERIODICIDADE);
			}
//			while(medicoes1[0] == null && medicoes2[0] == null)
//				wait(PERIODICIDADE);
//			sendStuff(medicoes);
			//			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void get() {
		FindIterable<Document> myCursor1 = db.getCollection("Zona1").find();
		FindIterable<Document> myCursor2 = db.getCollection("Zona2").find();

		ArrayList<Document> listaMedicoes_zona1 = getMedicoes(myCursor1, 1);
		ArrayList<Document> listaMedicoes_zona2 = getMedicoes(myCursor2, 2);
		ArrayList<Document> both = new ArrayList<Document>();

		for(Document doc : listaMedicoes_zona1) {
			both.add(doc);
		}
		for(Document doc : listaMedicoes_zona2) {
			both.add(doc);
		}
		for(Document doc : both) {
			System.out.println("new: " + doc.toString());
		}
	}

	public ArrayList<Document> getMedicoes(FindIterable<Document> myCursor, int zona) {
		ArrayList<Document> listaMedicoes = new ArrayList<Document>();
		Iterator doc = myCursor.iterator();

		while(doc.hasNext()) {
			Document medicao = (Document) doc.next();
			String[] array = medicao.toString().split(",");
			if(zona == 1) {
				timestamp = lastTimestamp1;
			}
			else {
				timestamp = lastTimestamp2;
			}
			
			if(comparesTo(array[3], timestamp) < 0) {
				System.out.println("Esta medição já foi enviada");
				if(zona == 1) {
					timestamp = lastTimestamp1;
				}
				else {
					timestamp = lastTimestamp2;
				}
				break;
			}
			else{
				listaMedicoes.add(medicao);
				if(zona == 1) {
					lastTimestamp1 = array[3];
				}
				else {
					lastTimestamp2 = array[3];
				}
			}
		}
		
		if(zona == 1) {
			timestamp = lastTimestamp1;
		}
		else {
			timestamp = lastTimestamp2;
		}
		
		return listaMedicoes;
	}
	
	private int comparesTo(String s1, String s2) {
		if(s2 == null)
			return 1;
		
		if(s2.equals(" "))
			return 1;
		
		String[] string1 = s1.split(" ");
		String[] string2 = s2.split(" ");
		
		String Data1[] = string1[1].split("/");
		String Data2[] = string2[1].split("/");
		String Hora1[] = string1[3].split(":");
		String Hora2[] = string1[3].split(":");
		
		if(Data1 == Data2 && Hora1 == Hora2)
			return 0;
		
		if(Integer.parseInt(Data1[2]) < Integer.parseInt(Data2[2]))
			return -1;
		if(Integer.parseInt(Data1[1]) < Integer.parseInt(Data2[1]))
			return -1;
		if(Integer.parseInt(Data1[0].split("=")[1]) < Integer.parseInt(Data2[0].split("=")[1])) {
			System.out.println("chegueiiii");
			return -1;
		}
		
		if(Integer.parseInt(Hora1[0]) < Integer.parseInt(Hora2[0]))
			return -1;
		if(Integer.parseInt(Hora1[1]) < Integer.parseInt(Hora2[1]))
			return -1;
		if(Integer.parseInt(Hora1[2].split(",")[0]) < Integer.parseInt(Hora2[0].split(",")[0]))
			return -1;
		
		return 1;
	}

	private synchronized void getStuff() throws SQLException { //falta ir so buscar se o timestamp for mais recente que o lastTimestamp
		FindIterable<Document> myCursor1 = db.getCollection("Zona1").find();
		FindIterable<Document> myCursor2 = db.getCollection("Zona2").find();

		Document obj;
		Document[] aux = new Document[10];
		Iterator it = myCursor1.iterator();
		int i = 0;
		while(it.hasNext()) {
			obj = (Document) it.next();
			if(initial) {
				aux[0] = obj;
				initial = false;
				i++;
			}
			else {
				while(i < aux.length && i>0) { 
					if(aux[i] == null && aux[i-1] != obj) { 
						aux[i] = obj;
						break;
					}
					else
						i++;
				}
			}
		}

		int toRemove = 0;
		for(int j = 0; j<aux.length; j++) {
			if(aux[j] == null)
				toRemove++;

			System.out.println("Aux " + j + " - " + aux[j]);
		}
		medicoes1 = new Document[aux.length - toRemove];
		for(int j = 0; j < medicoes1.length; j++)
			medicoes1[j] = aux[j];
		notifyAll();

		Arrays.fill(aux, null);
		initial = true;
		it = myCursor2.iterator();
		i = 0;
		while(it.hasNext()) {
			obj = (Document) it.next();
			if(initial) {
				aux[0] = obj;
				initial = false;
				i++;
			}
			else {
				while(i < aux.length && i>0) {
					if(aux[i] == null && aux[i-1] != obj) { 
						aux[i] = obj;
						break;
					}
					else
						i++;
				}
			}
		}

		toRemove = 0;
		for(int j = 0; j<aux.length; j++) {
			if(aux[j] == null)
				toRemove++;
			System.out.println("Aux " + j + " - " + aux[j]);
		}

		medicoes2 = new Document[aux.length - toRemove];
		for(int j = 0; j < medicoes2.length; j++)
			medicoes2[j] = aux[j];
		notifyAll();

		medicoes = new Document[medicoes1.length + medicoes2.length];
		System.arraycopy(medicoes1, 0, medicoes, 0, medicoes1.length);
		System.arraycopy(medicoes2, 0, medicoes, medicoes1.length, medicoes2.length);
		for(int j = 0; j<medicoes.length; j++)
			System.out.println("Both" + j + " - " + medicoes[j]);
	}

	public synchronized void sendStuff(Document[] objects) throws SQLException { //falta guardar o lastTimestamp e colocar o timestamp na tabela
		System.out.println("cheguei");
		Connection conn = null;
		Statement stm = null;

		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/EstufaDB", "root", "");
		stm = conn.createStatement();

		int indice = 0;
		for(int i = 0; i < objects.length; i++)
			if(objects[i]!=null)
				if(objects[i].get("DataHora").equals(lastTimestamp + ""))
					indice = i;

		String inserir = null;

		for(int i = indice; i < objects.length; i++) {
			//			timestamp = Long.parseLong(objects[0].getString("DataHora"));
			inserir = "INSERT INTO Medição (Leitura, Valido, Zona_ID, Sensor_ID)" + "\r\n"
					+ "VALUES (" + "'" + objects[i].getDouble("Medicao") + "'" + "," +  "'1'" + "," 
					+ "'" + objects[i].getString("Zona") + "'" + "," + "'" + objects[i].getString("Sensor")  + "'" + ")";
			System.out.println(inserir);
			stm.executeUpdate(inserir);
		}

		//		timestamp = Long.parseLong(objects[medicoes.length-1].get("DataHora").toString());

		conn.close();
	}
}