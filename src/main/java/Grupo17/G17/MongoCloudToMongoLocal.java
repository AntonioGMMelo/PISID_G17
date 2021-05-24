package Grupo17.G17;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.ConnectionString;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoCloudToMongoLocal {
	private static MongoClient mongoCloud;
	private static MongoDatabase dbCloud;
	private static MongoClient mongoLocal;
	private static MongoDatabase dbLocal;

	private static void connect() {		
		mongoCloud = MongoClients.create(new ConnectionString("mongodb://aluno:aluno@194.210.86.10:27017/?authSource=admin"));
		//		dbCloud = mongoCloud.getDatabase("g17");
		dbCloud = mongoCloud.getDatabase("sid2021");

		//		dbCloud.getCollection("Zona1");
		//		dbCloud.getCollection("Zona2");
		dbCloud.getCollection("sensort1");


		mongoLocal = MongoClients.create("mongodb://127.0.0.1:27017");

		dbLocal = mongoLocal.getDatabase("EstufaDB");

		dbLocal.getCollection("Zona1");
		dbLocal.getCollection("Zona2");

	}

	private static void transferData() {
		//		FindIterable<Document> myCursor1 = dbCloud.getCollection("Zona1").find();
		//		FindIterable<Document> myCursor2 = dbCloud.getCollection("Zona2").find();

		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date date = new Date(System.currentTimeMillis());
		String data = formatter.format(date);
		System.out.println(data);
		BasicDBObject query = new BasicDBObject("Data", new BasicDBObject("$gt", data));
		FindIterable<Document> myCursor1 = dbCloud.getCollection("sensort1").find(query);
		Iterator iterator = myCursor1.iterator();

		while(iterator.hasNext()) {
			Document medicao = (Document) iterator.next();
			System.out.println(medicao);
			dbLocal.getCollection("Zona1").insertOne(medicao);
		}

		//		while(myCursor2.iterator().hasNext())
		//			dbLocal.getCollection("Zona2").insertOne(myCursor2.iterator().next());
	}

	public static void main(String[] args) {
		connect();
		while(true)
			transferData();
	}
}
