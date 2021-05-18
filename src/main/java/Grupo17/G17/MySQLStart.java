
package Grupo17.G17;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLStart {

	private static void connect() {
		
		Connection connection = null;
	
		String url1 = "jdbc:mysql://localhost:3306/";
		String user = "root";
    	String password = "";
    
    	//Try Connect
		try {
		
			connection = DriverManager.getConnection(url1, user, password);
    	
			if (connection != null) {
    			System.out.println("Connected to the database test1");
    		}
    	
			//Creating EstufaDB
			Statement statement = connection.createStatement();
		    String sqlCreate = "CREATE DATABASE IF NOT EXISTS EstufaDB";
		    statement.executeUpdate(sqlCreate);
		    
		    //Selecting EstufaDB
		    connection = DriverManager.getConnection(url1 + "EstufaDB", user, password);
		    
		    //Creating Tables
		    
		    //Creating Zona Table
		    statement = connection.createStatement();
			String createZona = "CREATE TABLE IF NOT EXISTS Zona " +
								"(Zona_ID VARCHAR(2) not NULL, " +
								"Temperatura decimal(5,2) not NULL, " +
								"Humidade decimal(5,2) not NULL, " +
								"Luz decimal(5,2) not NULL, " +
								"PRIMARY KEY (Zona_ID) )";
			statement.executeUpdate(createZona);
			
			//Works until here
			
			//Creating Sensor Table
			statement = connection.createStatement();
			String createSensor = "CREATE TABLE IF NOT EXISTS Sensor " +
								  "(Sensor_ID VARCHAR(2) not NULL, " +
								  "LimiteInferior decimal(5,2) not NULL, " +	
								  "LimiteSuperior decimal(5,2) not NULL, " +
								  "Zona_ID VARCHAR(2) not NULL, " +
								  "PRIMARY KEY (Sensor_ID) )";
			statement.executeUpdate(createSensor);
			
			//Creating Utilizador Table
			statement = connection.createStatement();
			String createUtilizador = "CREATE TABLE IF NOT EXISTS Utilizador" +
								  	  "(Utilizador_ID INTEGER not NULL AUTO_INCREMENT, " +
								      "NomeUtilizador VARCHAR(50) not NULL, " +	
								      "EmailUtilizador VARCHAR(50) not NULL, " +
								      "TipoUtilizador VARCHAR(50) not NULL, " +
								      "PRIMARY KEY (Utilizador_ID) )";
			statement.executeUpdate(createUtilizador);
			
			//Creating Cultura Table
			statement = connection.createStatement();
			String createCultura = "CREATE TABLE IF NOT EXISTS Cultura " +
								  	  "(Cultura_ID INTEGER not NULL AUTO_INCREMENT, " +
								      "NomeCultura VARCHAR(50) not NULL, " +	
								      "Estado TINYINT not NULL, " +
								      "Utilizador_ID INTEGER, " +
								      "Zona_ID VARCHAR(2) not NULL, " +
								      "ParametroCultura_ID INTEGER NOT NULL, " +
								      "PRIMARY KEY (Cultura_ID) )";
			statement.executeUpdate(createCultura);
			
			//Creating Medição Table
			statement = connection.createStatement();
			String createMedicao = "CREATE TABLE IF NOT EXISTS Medicao" +
								  	  "(Medicao_ID INTEGER not NULL AUTO_INCREMENT, " +
								      "Hora timestamp not NULL, " +	
								      "Leitura decimal(5,2) not NULL, " +
								      "Valido TINYINT not NULL, " +
								      "Zona_ID VARCHAR(2) not NULL, " +
								      "Sensor_ID VARCHAR(2) not NULL, " +
								      "PRIMARY KEY (Medicao_ID) )";
			statement.executeUpdate(createMedicao);
			
			//Creating Alerta Table
			statement = connection.createStatement();
			String createAlerta = "CREATE TABLE IF NOT EXISTS Alerta " +
								  	  "(Alerta_ID INTEGER not NULL, " +
								      "Hora timestamp not NULL DEFAULT CURRENT_TIMESTAMP, " +	
								      "Leitura decimal(5,2) not NULL, " +
								      "Tipo VARCHAR(1) not NULL, " +
								      "Mensagem VARCHAR(150) not NULL, " +
								      "HoraEscrita timestamp not NULL DEFAULT CURRENT_TIMESTAMP, " +
								      "Zona_ID VARCHAR(2) not NULL, " +
								      "Sensor_ID VARCHAR(2) not NULL, " +
								      "Cultura_ID INTEGER not NULL,  " +
								      "Utilizador_ID INTEGER not NULL,  " +
								      "Cultura VARCHAR(50) not NULL,  " +
								      "PRIMARY KEY (Alerta_ID) )";
			statement.executeUpdate(createAlerta);
			
			//Creating ParametroCultura Table
			statement = connection.createStatement();
			String createParametroCultura = "CREATE TABLE IF NOT EXISTS ParametroCultura " +
								  	  "(ParametroCultura_ID INTEGER not NULL AUTO_INCREMENT, " +
								  	  "MinTemp decimal(5,2) not NULL, " +
								  	  "MaxTemp decimal(5,2) not NULL, " +
								  	  "MinHumid decimal(5,2) not NULL, " +
								  	  "MaxHumid decimal(5,2) not NULL, " +
								  	  "MinLuz decimal(5,2) not NULL, " +
								  	  "MaxLuz decimal(5,2) not NULL, " +
								      "PRIMARY KEY (ParametroCultura_ID) )";
			statement.executeUpdate(createParametroCultura);
			
			//Create table Relations
			
			//Create Cultura Relations
			statement = connection.createStatement();
			String createCulturaFKs = "ALTER TABLE Cultura " +
									  "ADD CONSTRAINT FK_Cultura_ParametroCultura FOREIGN KEY (ParametroCultura_ID) REFERENCES ParametroCultura(ParametroCultura_ID) ON UPDATE CASCADE ON DELETE CASCADE," +
									  "ADD CONSTRAINT FK_Cultura_Utilizador FOREIGN KEY (Utilizador_ID) REFERENCES Utilizador (Utilizador_ID) ON UPDATE CASCADE ON DELETE SET NULL," +
									  "ADD CONSTRAINT FK_Cultura_Zona FOREIGN KEY (Zona_ID) REFERENCES Zona (Zona_ID) ON UPDATE CASCADE ON DELETE CASCADE;";
			statement.executeUpdate(createCulturaFKs);
			
			//Create Alerta Relations
			statement = connection.createStatement();
			String createAlertaFK = "ALTER TABLE Alerta " +
									"ADD CONSTRAINT FK_Alerta_Cultura FOREIGN KEY (Cultura_ID) REFERENCES Cultura (Cultura_ID)";
			statement.executeUpdate(createAlertaFK);
			
			//Create Medição Relations
			statement = connection.createStatement();
			String createMedicaoSensor = "ALTER TABLE Medicao " +
										 "ADD CONSTRAINT FK_Medição_Sensor FOREIGN KEY (Sensor_ID) REFERENCES Sensor (Sensor_ID)";
			statement.executeUpdate(createMedicaoSensor);
			
			//Create Sensor Relations
			statement = connection.createStatement();
			String createSensorZona = "ALTER TABLE Sensor " +
									  "ADD CONSTRAINT FK_Sensor_Zona FOREIGN KEY (Zona_ID) REFERENCES Zona(Zona_ID)";
			statement.executeUpdate(createSensorZona);
			
			//Creating Both Zonas and all six Sensores
			
			//Creating Zona 1
			String addZ1 = "INSERT INTO Zona (Zona_ID, Temperatura, Humidade, Luz)" +
			        "VALUES (?, ?, ?, ?)";
			
			PreparedStatement preparedStatement = connection.prepareStatement(addZ1);
			preparedStatement.setString(1, "Z1");
			BigDecimal d = new BigDecimal(0.0);
			preparedStatement.setBigDecimal(2, d);
			preparedStatement.setBigDecimal(3, d);
			preparedStatement.setBigDecimal(4, d);
			preparedStatement.executeUpdate(); 
			
			//Creating Zona 2
			String addZ2 = "INSERT INTO Zona (Zona_ID, Temperatura, Humidade, Luz)" +
			        "VALUES (?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(addZ2);
			preparedStatement.setString(1, "Z2");
			preparedStatement.setBigDecimal(2, d);
			preparedStatement.setBigDecimal(3, d);
			preparedStatement.setBigDecimal(4, d);
			preparedStatement.executeUpdate(); 
			
			//Creating H1
			String addH1 = "INSERT INTO Sensor (Sensor_ID, LimiteInferior, LimiteSuperior, Zona_ID)" +
						   "VALUES (?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(addH1);
			preparedStatement.setString(1, "H1");
			preparedStatement.setBigDecimal(2, d);                     //Trocar para limite NO 194.210.86.10
			preparedStatement.setBigDecimal(3, new BigDecimal(200.0)); //Trocar para limite NO 194.210.86.10
			preparedStatement.setString(4, "Z1");
			preparedStatement.executeUpdate(); 
			
			//Creating H2
			String addH2 = "INSERT INTO Sensor (Sensor_ID, LimiteInferior, LimiteSuperior, Zona_ID)" +
						   "VALUES (?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(addH2);
			preparedStatement.setString(1, "H2");
			preparedStatement.setBigDecimal(2, d); //Trocar para limite NO 194.210.86.10
			preparedStatement.setBigDecimal(3, new BigDecimal(200.0)); //Trocar para limite NO 194.210.86.10
			preparedStatement.setString(4, "Z2");
			preparedStatement.executeUpdate(); 
			
			//Creating T1
			String addT1 = "INSERT INTO Sensor (Sensor_ID, LimiteInferior, LimiteSuperior, Zona_ID)" +
						   "VALUES (?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(addT1);
			preparedStatement.setString(1, "T1");
			preparedStatement.setBigDecimal(2, d); //Trocar para limite NO 194.210.86.10
			preparedStatement.setBigDecimal(3, new BigDecimal(100.0)); //Trocar para limite NO 194.210.86.10
			preparedStatement.setString(4, "Z1");
			preparedStatement.executeUpdate(); 
			
			//Creating T2
			String addT2 = "INSERT INTO Sensor (Sensor_ID, LimiteInferior, LimiteSuperior, Zona_ID)" +
						   "VALUES (?, ?, ?, ?)";	 
			
			preparedStatement = connection.prepareStatement(addT2);
			preparedStatement.setString(1, "T2");
			preparedStatement.setBigDecimal(2, d); //Trocar para limite NO 194.210.86.10
			preparedStatement.setBigDecimal(3, new BigDecimal(100.0)); //Trocar para limite NO 194.210.86.10
			preparedStatement.setString(4, "Z2");
			preparedStatement.executeUpdate();
			
			//Creating L1
			String addL1 = "INSERT INTO Sensor (Sensor_ID, LimiteInferior, LimiteSuperior, Zona_ID)" +
						   "VALUES (?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(addL1);
			preparedStatement.setString(1, "L1");
			preparedStatement.setBigDecimal(2, d); //Trocar para limite NO 194.210.86.10
			preparedStatement.setBigDecimal(3, new BigDecimal(100.0)); //Trocar para limite NO 194.210.86.10
			preparedStatement.setString(4, "Z1");
			preparedStatement.executeUpdate();
			
			//Creating L2
			String addL2 = "INSERT INTO Sensor (Sensor_ID, LimiteInferior, LimiteSuperior, Zona_ID)" +
						   "VALUES (?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(addL2);
			preparedStatement.setString(1, "L2");
			preparedStatement.setBigDecimal(2, d); //Trocar para limite NO 194.210.86.10
			preparedStatement.setBigDecimal(3, new BigDecimal(100.0)); //Trocar para limite NO 194.210.86.10
			preparedStatement.setString(4, "Z1");
			preparedStatement.executeUpdate(); 
			
			System.out.println("Boot Succesfull");
			
		}catch(Exception e){
			
			e.printStackTrace();
		
		}finally {
			
            if (connection != null) {
            
            	try {
                
            		connection.close();
               
            	} catch (SQLException ex) {
                
            		ex.printStackTrace();
                
            	}
            	
            }
            
		}
	
	}
	
	public static void main(String[] args) {
		
		connect();
		
	}
	

}