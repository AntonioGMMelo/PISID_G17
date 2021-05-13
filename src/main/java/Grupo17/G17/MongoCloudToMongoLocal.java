package Grupo17.G17;

import org.bson.Document;

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
		dbCloud = mongoCloud.getDatabase("g17");

		dbCloud.getCollection("Zona1");
		dbCloud.getCollection("Zona2");


		mongoLocal = MongoClients.create("mongodb://127.0.0.1:27017");

		dbLocal = mongoLocal.getDatabase("EstufaDB");

		dbLocal.getCollection("Zona1");
		dbLocal.getCollection("Zona2");

	}

	private static void transferData() {
		FindIterable<Document> myCursor1 = dbCloud.getCollection("Zona1").find();
		FindIterable<Document> myCursor2 = dbCloud.getCollection("Zona2").find();
		
		while(myCursor1.iterator().hasNext())
			dbLocal.getCollection("Zona1").insertOne(myCursor1.iterator().next());
		
		while(myCursor2.iterator().hasNext())
			dbLocal.getCollection("Zona2").insertOne(myCursor1.iterator().next());
	}


	public static void main(String[] args) {
		connect();
		transferData();
	}
}
