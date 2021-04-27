package Grupo17.G17;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBStart {

	//Creates EstufaDB and both collections
	private static void connect() {		

		MongoClient mongo = MongoClients.create("mongodb://127.0.0.1:27017");
		
		MongoDatabase db = mongo.getDatabase("EstufaDB");
		
		db.getCollection("Zona1");
		db.getCollection("Zona2");
	
	}
	
	public static void main(String[] args) {
		
		connect();
		
	}
}
