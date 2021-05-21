package Grupo17.G17;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MysqlConnection {

	public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
		String db = "estufaDB";
		String DBuser = "root";
		String DBpass = "";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connectionSQL = DriverManager.getConnection("jdbc:mysql://localhost/" + db + "?useTimezone=true&serverTimezone=UTC", DBuser, DBpass);
		
		
		DateFormat dateFormatSQL = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		int id = 1;
		String sensor = "T1";
		String zona = "Z1";
		int valido = 1;
		for (;;) {
			Date now = new Date();
			String hora = dateFormatSQL.format(now);
			Random r = new Random();
			double leitura = r.nextDouble()*5.0 + 20.0;
			String query = "INSERT INTO Medicao VALUES (NULL, '" + hora + "','" + leitura + "','" + valido + "','" + zona + "','" + sensor + "');";
			System.out.println(query);
			connectionSQL.createStatement().executeUpdate(query);
			id++;
			Thread.sleep(3000);
		}
	}

}
