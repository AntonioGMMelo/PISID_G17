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
import java.util.Iterator;

public class Getter extends Thread{
	public static final int PERIODICIDADE = 1000;
	ArrayList<Document> medicoes = new ArrayList<Document>();
	private String lastTimestamp_zona1;
	private String lastTimestamp_zona2;
	com.mongodb.client.MongoClient mongo = MongoClients.create("mongodb://127.0.0.1:27017");
	public MongoDatabase db = mongo.getDatabase("EstufaDB");
	private String finalTimestamp;

	@Override
	public synchronized void run() {
		try {
			File f = new File("timestamp.txt");
			BufferedReader br;
			if(f.exists()) {
				br = new BufferedReader(new FileReader("timestamp.txt"));
				lastTimestamp_zona1 = br.readLine();
				lastTimestamp_zona2 = lastTimestamp_zona1;
				finalTimestamp = lastTimestamp_zona1;
				br.close();
			}
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
		System.out.println("comecei o getStuff");
		FindIterable<Document> myCursor1 = db.getCollection("Zona1").find();
		FindIterable<Document> myCursor2 = db.getCollection("Zona2").find();

		ArrayList<Document> listaMedicoes_zona1 = getMedicoes(myCursor1, 1);
		ArrayList<Document> listaMedicoes_zona2 = getMedicoes(myCursor2, 2);

		if(this.medicoes != null) {
			for(Document doc : listaMedicoes_zona1)
				this.medicoes.add(doc);
			for(Document doc : listaMedicoes_zona2) 
				this.medicoes.add(doc);
		}
	}

	public ArrayList<Document> getMedicoes(FindIterable<Document> myCursor, int zona) throws IOException{
		ArrayList<Document> listaMedicoes = new ArrayList<Document>();
		Iterator doc = myCursor.iterator();
		String timestamp_zona1 = null;
		String timestamp_zona2 = null;

		while(doc.hasNext()) {
			boolean last = false;
			Document medicao = (Document) doc.next();
			System.out.println(medicao);
			
			String[] array = medicao.toString().split(",");
			String timestamp = array[3].split("=")[1];
			String lastTimestamp; 

			if(zona == 1)
				lastTimestamp = lastTimestamp_zona1;
			else
				lastTimestamp = lastTimestamp_zona2;

			if(checkTimestamp(timestamp, lastTimestamp)) {
				listaMedicoes.add(medicao);
				if(zona == 1)
					timestamp_zona1 = array[3].split("=")[1];
				if(zona == 2)
					timestamp_zona2 = array[3].split("=")[1];
			}
			else {
				System.out.println("Esta medição já foi enviada");
			}
		}
		
		if(timestamp_zona1 != null)
			this.lastTimestamp_zona1 = timestamp_zona1;
		else if(timestamp_zona2 != null)
			this.lastTimestamp_zona2 = timestamp_zona2;
		
		return listaMedicoes;
	}

	public synchronized Boolean checkTimestamp(String timestamp, String ultimo_timestamp) {		
		System.out.println(timestamp);
		System.out.println(ultimo_timestamp);
		
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

	public synchronized void sendStuff(ArrayList<Document> medicoes) throws SQLException, ParseException, IOException { //falta guardar o lastTimestamp num ficheiro
		Connection conn = null;
		Statement stm = null;

		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/EstufaDB", "root", "");
		stm = conn.createStatement();

		String inserir;	

		if(this.medicoes != null)
			for(Document m : this.medicoes) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date utilDate = format.parse(m.toString().split(", ")[3].split("=")[1].split(" ")[0]);
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				inserir = "INSERT INTO Medicao (Hora, Leitura, Valido, Zona_ID, Sensor_ID)" + "\r\n"
						+ "VALUES (" + "'" + sqlDate + " " + m.toString().split("=")[4].split("T")[1].split("Z")[0] + "'," 
						+ "'" + Double.parseDouble(m.toString().split(", ")[4].split("=")[1].split("}")[0]) + "'" + "," +  "'1'" + "," 
						+ "'" + m.toString().split(", ")[1].split("=")[1] + "'" + "," + "'" + m.toString().split(", ")[2].split("=")[1] + "'" + ")";

				finalTimestamp = sqlDate + "T" + m.toString().split("=")[4].split("T")[1].split(",")[0];
				System.out.println(inserir);
				stm.executeUpdate(inserir);
			}
		else
			System.out.println("Não existem novas medições em ambas as zonas");

		conn.close();

		if(finalTimestamp != null) {
			System.out.println("Last date sent: " + finalTimestamp);
			try (PrintWriter out = new PrintWriter("timestamp.txt")) {
				out.println(finalTimestamp);
			}
		}
	}
}