package Grupo17.G17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
	private String finalTimestamp;

	@Override
	public synchronized void run() {
		try {
			while(true) {
				wait(PERIODICIDADE);
				getStuff();
				sendStuff(medicoes);
				this.medicoes = null;
			}
		} catch (SQLException | InterruptedException | ParseException | IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void getStuff() throws SQLException, IOException{
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

	public ArrayList<Document> getMedicoes(FindIterable<Document> myCursor, int zona) throws IOException {
		ArrayList<Document> listaMedicoes = new ArrayList<Document>();
		Iterator doc = myCursor.iterator();
		String timestamp_zona1 = null;
		String timestamp_zona2 = null;
		Boolean semaforo_zona1 = true;
		Boolean semaforo_zona2 = true;

		File f = new File("timestamp.txt");
		BufferedReader br;
		if(f.exists()) {
			br = new BufferedReader(new FileReader("timestamp.txt"));
			timestamp_zona1 = br.readLine();
			timestamp_zona2 = timestamp_zona1;
		}

		while(doc.hasNext()) {
			Document medicao = (Document) doc.next();
			String[] array = medicao.toString().split(",");
			String timestamp = array[3].split("=")[1];
			String ultimo_timestamp;

			if(zona == 1) {ultimo_timestamp = lastTimestamp_zona1;}
			else {ultimo_timestamp = lastTimestamp_zona2;}


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

	public synchronized void sendStuff(ArrayList<Document> medicoes) throws SQLException, ParseException, IOException { //falta guardar o lastTimestamp num ficheiro
		Connection conn = null;
		Statement stm = null;

		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/EstufaDB", "root", "");
		stm = conn.createStatement();

		String inserir;

		File f = new File("timestamp.txt");
		BufferedReader br;
		if(f.exists()) {
			br = new BufferedReader(new FileReader("timestamp.txt"));
			finalTimestamp = br.readLine();
		}
		System.out.println("TIMESTAMP - " + finalTimestamp);

		if(this.medicoes != null) {
			for(Document m : this.medicoes) {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				java.util.Date utilDate = format.parse(m.toString().split(", ")[3].split("=")[1].split(" ")[0]);
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				if(finalTimestamp == null){
					inserir = "INSERT INTO Medicao (Hora, Leitura, Valido, Zona_ID, Sensor_ID)" + "\r\n"
							+ "VALUES (" + "'" + sqlDate + " " + m.toString().split(", ")[3].split("=")[1].split(" ")[2] + "'," 
							+ "'" + Double.parseDouble(m.toString().split(", ")[4].split("=")[1].split("}")[0]) + "'" + "," +  "'1'" + "," 
							+ "'" + m.toString().split(", ")[1].split("=")[1] + "'" + "," + "'" + m.toString().split(", ")[2].split("=")[1]  + "'" + ")";
					System.out.println(inserir);
					stm.executeUpdate(inserir);
					finalTimestamp = sqlDate + " at " + m.toString().split(", ")[3].split("=")[1].split(" ")[2];
				}
				else if(!checkTimestamp(finalTimestamp, sqlDate + " at " + m.toString().split(", ")[3].split("=")[1].split(" ")[2])) {
					inserir = "INSERT INTO Medicao (Hora, Leitura, Valido, Zona_ID, Sensor_ID)" + "\r\n"
							+ "VALUES (" + "'" + sqlDate + " " + m.toString().split(", ")[3].split("=")[1].split(" ")[2] + "'," 
							+ "'" + Double.parseDouble(m.toString().split(", ")[4].split("=")[1].split("}")[0]) + "'" + "," +  "'1'" + "," 
							+ "'" + m.toString().split(", ")[1].split("=")[1] + "'" + "," + "'" + m.toString().split(", ")[2].split("=")[1]  + "'" + ")";
					System.out.println(inserir);
					stm.executeUpdate(inserir);
					finalTimestamp = sqlDate + " at " + m.toString().split(", ")[3].split("=")[1].split(" ")[2];
				}
			}
		}
		else {
			System.out.println("Não existem novas medições em ambas as zonas");
		}

		conn.close();
		System.out.println("Last date sent: " + finalTimestamp);
		File myObj = new File("timestamp.txt");
		try (PrintWriter out = new PrintWriter("timestamp.txt")) {
			System.out.println("chegou");
			out.println(finalTimestamp);
		}
	}
}