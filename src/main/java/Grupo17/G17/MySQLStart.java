package Grupo17.G17;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLStart {

	public void connect() {
		
		Connection connection = null;
	
		String url1 = "jdbc:mysql://localhost/";
		String user = "root";
    	String password = "secret";
    
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
			
			//Creating Sensor Table
			statement = connection.createStatement();
			String createSensor = "CREATE TABLE IF NOT EXISTS Utilizador, " +
								  "(Sensor_ID VARCHAR(2) not NULL, " +
								  "LimiteInferior decimal(5,2), " +	
								  "LimiteSuperior decimal(5,2), " +
								  "ID_Zona INTEGER, " +
								  "PRIMARY KEY (Sensor_ID) )";
			statement.executeUpdate(createSensor);
			
			//Creating Utilizador Table
			statement = connection.createStatement();
			String createUtilizador = "CREATE TABLE IF NOT EXISTS Sensor, " +
								  	  "(Utilizador_ID INTEGER not NULL, " +
								      "NomeInvestigador VARCHAR(50) not NULL, " +	
								      "EmailUtilizador VARCHAR(50) not NULL, " +
								      "TipoUtilizador VARCHAR(1) not NULL, " +
								      "PRIMARY KEY (Utilizador_ID) )";
			statement.executeUpdate(createUtilizador);
			
			//Creating Cultura Table
			statement = connection.createStatement();
			String createCultura = "CREATE TABLE IF NOT EXISTS Cultura, " +
								  	  "(Cultura_ID INTEGER not NULL, " +
								      "NomeCultura VARCHAR(50) not NULL, " +	
								      "Estador TINYINT not NULL, " +
								      "Utilizador_ID INTEGER, " +
								      "Zona_ID VARCHAR(2) not NULL, " +
								      "ParamaetroCultura_ID INTEGER NOT NULL, " +
								      "PRIMARY KEY (Cultura_ID) )";
			statement.executeUpdate(createCultura);
			
			//Creating Medição Table
			statement = connection.createStatement();
			String createMedicao = "CREATE TABLE IF NOT EXISTS Medição, " +
								  	  "(Medição_ID INTEGER not NULL, " +
								      "Hora timestamp not NULL, " +	
								      "Leitura decimal(5,2) not NULL, " +
								      "Valido TINYINT not NULL, " +
								      "Zona_ID VARCHAR(2) not NULL, " +
								      "Sensor_ID VARCHAR(2) not NULL, " +
								      "PRIMARY KEY (Medição_ID) )";
			statement.executeUpdate(createMedicao);
			
			//Creating Alerta Table
			statement = connection.createStatement();
			String createAlerta = "CREATE TABLE IF NOT EXISTS Medição, " +
								  	  "(Alerta_ID INTEGER not NULL, " +
								      "Hora timestamp not NULL, " +	
								      "Leitura decimal(5,2) not NULL, " +
								      "Tipo VARCHAR(1) not NULL, " +
								      "Mensagem VARCHAR(150) not NULL, " +
								      "HoraEscrita timestamp not NULL, " +
								      "Zona_ID VARCHAR(2) not NULL, " +
								      "Sensor_ID VARCHAR(2) not NULL, " +
								      "Cultura_ID, INTEGER not NULL,  " +
								      "Utilizador_ID, INTEGER not NULL,  " +
								      "Cultura, VARCHAR(50) not NULL,  " +
								      "PRIMARY KEY (Alerta_ID) )";
			statement.executeUpdate(createAlerta);
			
			//Creating ParametroCultura Table
			statement = connection.createStatement();
			String createParametroCultura = "CREATE TABLE IF NOT EXISTS Medição, " +
								  	  "(ParametroCultura_ID INTEGER not NULL, " +
								  	  "MinTemp decimal(5,2) not NULL, " +
								  	  "MaxTemp decimal(5,2) not NULL, " +
								  	  "MinHumid decimal(5,2) not NULL, " +
								  	  "MaxHumid decimal(5,2) not NULL, " +
								  	  "MinLuz decimal(5,2) not NULL, " +
								  	  "MaxLuz decimal(5,2) not NULL, " +
								      "PRIMARY KEY (ParametroCultura_ID) )";
			statement.executeUpdate(createParametroCultura);
			
			//Create table Relations and autoIncrements
			
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
	
}
