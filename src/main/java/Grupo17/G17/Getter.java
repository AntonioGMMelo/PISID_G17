package Grupo17.G17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Getter extends Thread{
	public static final int PERIODICIDADE = 1000;
	ArrayList<Document> medicoes = new ArrayList<Document>();
	private String lastTimestamp;
	com.mongodb.client.MongoClient mongo = MongoClients.create("mongodb://127.0.0.1:27017");
	public MongoDatabase db = mongo.getDatabase("EstufaDB");
	private String finalTimestamp;

	@Override
	public synchronized void run() {
		try {
			while(true) {
				File f = new File("timestamp.txt");
				BufferedReader br;
				if(f.exists()) {
					br = new BufferedReader(new FileReader("timestamp.txt"));
					lastTimestamp = br.readLine();
					finalTimestamp = lastTimestamp;
					br.close();
				}
				wait(PERIODICIDADE);
				getStuff();
				sendStuff(medicoes);
			}
		} catch (SQLException | InterruptedException | ParseException | IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void getStuff() throws SQLException, IOException{
		FindIterable<Document> myCursor1 = db.getCollection("Zona1").find();
		FindIterable<Document> myCursor2 = db.getCollection("Zona2").find();

		ArrayList<Document> listaMedicoes = new ArrayList<Document>();
		ArrayList<Document> listaMedicoes_zona1 = getMedicoes(myCursor1);
		ArrayList<Document> listaMedicoes_zona2 = getMedicoes(myCursor2);

		for(Document doc : listaMedicoes_zona1)
			listaMedicoes.add(doc);
		for(Document doc : listaMedicoes_zona2) 
			listaMedicoes.add(doc);

		this.medicoes = listaMedicoes;
	}

	public ArrayList<Document> getMedicoes(FindIterable<Document> myCursor) throws IOException{
		ArrayList<Document> listaAux = new ArrayList<Document>();
		ArrayList<Document> novasMedicoes = new ArrayList<Document>();
		Iterator d = myCursor.iterator();
		String newLastTimestamp = null;
		boolean semaforo = true;

		while(d.hasNext()) {
			Document medicao = (Document) d.next();
			listaAux.add(medicao);
		}
		Collections.reverse(listaAux);

		for(Document doc : listaAux) {
			String[] array = doc.toString().split(",");
			String timestamp = array[3].split("=")[1];
			if(semaforo) {
				newLastTimestamp = timestamp;
				semaforo = false;
			}
			if(checkTimestamp(timestamp, lastTimestamp)) {
				novasMedicoes.add(doc);
			}
			else{
				System.out.println("Esta medi????o j?? foi enviada");
				break;
			}
		}
		if(newLastTimestamp != null) {
			this.lastTimestamp = newLastTimestamp;
		}

		return novasMedicoes;
	}

	public synchronized Boolean checkTimestamp(String timestamp, String ultimo_timestamp) {	
		if(timestamp == null)
			return true;

		if(ultimo_timestamp == null)
			return true;

		String[] t = timestamp.split("T");
		String[] data = t[0].split("-");
		String[] hora = t[1].split(":");

		String[] ultimo_t = ultimo_timestamp.split("T");
		String[] ultima_data = ultimo_t[0].split("-");
		String[] ultima_hora = ultimo_t[1].split(":");

		if(ultimo_timestamp == timestamp)
			return false;
		else
			if(Integer.parseInt(data[2]) <= Integer.parseInt(ultima_data[2]))
				if(Integer.parseInt(data[1]) <= Integer.parseInt(ultima_data[1]))
					if(Integer.parseInt(data[0]) <= Integer.parseInt(ultima_data[0]))
						if(Integer.parseInt(hora[0]) <= Integer.parseInt(ultima_hora[0]))
							if(Integer.parseInt(hora[1]) <= Integer.parseInt(ultima_hora[1]))
								if(Integer.parseInt(hora[2].split("Z")[0]) <= Integer.parseInt(ultima_hora[2].split("Z")[0]))
									return false;
								else
									return true;
							else
								return true;
						else
							return true;
					else
						return true;
				else
					return true;
			else
				return true;
	}

	public synchronized void sendStuff(ArrayList<Document> medicoes) throws SQLException, ParseException, IOException {
		Connection conn = null;
		Statement stm = null;

		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/EstufaDB", "root", "");
		stm = conn.createStatement();

		if(this.medicoes != null) {
			for(Document m : this.medicoes) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date utilDate = format.parse(m.toString().split(", ")[3].split("=")[1].split(" ")[0]);
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				String inserir = "INSERT INTO Medicao (Hora, Leitura, Valido, Zona_ID, Sensor_ID)" + "\r\n"
						+ "VALUES (" + "'" + sqlDate + " " + m.toString().split("=")[4].split("T")[1].split("Z")[0] + "'," 
						+ "'" + Double.parseDouble(m.toString().split(", ")[4].split("=")[1].split("}")[0]) + "'" + "," +  "'1'" + "," 
						+ "'" + m.toString().split(", ")[1].split("=")[1] + "'" + "," + "'" + m.toString().split(", ")[2].split("=")[1] + "'" + ")";

				if(finalTimestamp != null) {
					if(!checkTimestamp(finalTimestamp, sqlDate + "T" + m.toString().split("=")[4].split("T")[1].split(",")[0])) {
						finalTimestamp = sqlDate + "T" + m.toString().split("=")[4].split("T")[1].split(",")[0];
						System.out.println(inserir);
						stm.executeUpdate(inserir);
					}
					else {
						System.out.println(inserir);
						stm.executeUpdate(inserir);
					}
				}
				else {
					finalTimestamp = sqlDate + "T" + m.toString().split("=")[4].split("T")[1].split(",")[0];
					System.out.println(inserir);
					stm.executeUpdate(inserir);
				}
			}
		}
		else
			System.out.println("N??o existem novas medi????es em ambas as zonas");

		conn.close();

		if(finalTimestamp != null) {
			System.out.println("Last date sent: " + finalTimestamp);
			try (PrintWriter out = new PrintWriter("timestamp.txt")) {
				out.println(finalTimestamp);
			}
		}
	}
}