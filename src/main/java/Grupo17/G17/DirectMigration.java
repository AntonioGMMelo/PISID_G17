package Grupo17.G17;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

//Direct Migration from MongoDB to MYSQL
public class DirectMigration {
	
	public static void main(String[] args) {
		Getter getter = new Getter();
		getter.start();
	}


}
