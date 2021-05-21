package Grupo17.G17;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Deque;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;


public class Alertar extends Thread{
	
	private int  Medicao_ID;
	private String Zona_ID;
	private String Sensor_ID;
	private double medicao;
	private String data;
	private int valido;
	private Deque<Double> ultimasMedicoes;
	
	public Alertar(int Medicao_ID, String Zona_ID, String Sensor_ID, double medicao, String data, int valido, Deque<Double>ultimasMedicoes) {
		
		this.Medicao_ID=Medicao_ID;
		this.Zona_ID=Zona_ID;
		this.Sensor_ID=Sensor_ID;
		this.medicao=medicao;
		this.data=data;
		this.valido=valido;
		this.ultimasMedicoes=ultimasMedicoes;
		
	}
	
	public double[] toArray(Object[] arr) {
		
		double[] answer = new double[arr.length];
		
		for(int i = 0; i<arr.length;i++) answer[i] = (double)arr[i];
		
		return answer;
		
	}

	public void run(){
		
		String min = "";
		String max = "";
		double[] medicoes = new double[10];
		
		switch(Sensor_ID){
		
		case "T1":
			min = "parametrocultura.minTemp";
			max = "parametrocultura.maxTemp";
		case "T2":
			min = "parametrocultura.minTemp";
			max = "parametrocultura.maxTemp";
		case "H1":
			min = "parametrocultura.minHumid";
			max = "parametrocultura.maxHumid";
		case "H2":
			min = "parametrocultura.minHumid";
			max = "parametrocultura.maxHumid";
		case "L1":
			min = "parametrocultura.minLuz";
			max = "parametrocultura.maxLuz";
		case "L2":
			min = "parametrocultura.minLuz";
			max = "parametrocultura.maxLuz";
		}
		
		 try
		    {
		      // create our mysql database connection«
		      String myUrl = "jdbc:mysql://localhost:3306/EstufaDB";
		      Connection conn = DriverManager.getConnection(myUrl, "root", "");
		      
		      String query = "SELECT Distinct cultura.Cultura_ID, "+ min +",  "+ max +
		    		  " FROM parametrocultura,cultura WHERE cultura.ParametroCultura_ID = parametrocultura.ParametroCultura_ID AND cultura.Zona_ID='"+Zona_ID+"'";

		      // create the java statement
		      Statement st = conn.createStatement();
		      
		      // execute the query, and get a java resultset
		      ResultSet rs = st.executeQuery(query);
		      
		      
		      // iterate through the java resultset
		      while (rs.next()){
		    	  
		        int id = rs.getInt("Cultura_ID");
		        double minN = rs.getDouble(min);
		        double maxN = rs.getDouble(max);
		        double[] param = new double[] {minN, maxN}; 
		       
		        String[] isAlert = Alerts.Alertar(medicao,toArray(ultimasMedicoes.toArray()), param, valido == 1);
		        
		        if(isAlert != null) {
		        	
		        	Date date = new Date(System.currentTimeMillis());
		        	Timestamp timestamp2 = new Timestamp(date.getTime());
		    		
		    		try {
		    			
		    			System.out.println("herr");
		    			
		    			Dictionary<Integer,Integer> helper2 = Potato.getLastAlert();
		    			int helper = helper2.get((Integer)id);
		    			
		    			System.out.println(helper2.keys().nextElement().hashCode());
		    			System.out.println(((Integer)id).hashCode());
		    			System.out.println();
		    			
		    			System.out.print("plz" + helper);
		    			
		    			String query3 = "SELECT alerta.HoraEscrita"+
		  		    		  " FROM alerta,cultura WHERE cultura.Cultura_ID ='"+id+"' ORDER BY HoraEscrita DESC LIMIT 1";

		    			// create the java statement
		    			Statement st3 = conn.createStatement();
		  		      
		  		      	// execute the query, and get a java resultset
		  		      	ResultSet rs3= st3.executeQuery(query);
		  		      	
		  		      	long t =0 ;
		  		      	
		  		      	if(rs3.next()) {
		  		      		
		  		      		t= rs3.getLong(1);
		  		      		
		  		      	}
		    			
		    			if(timestamp2.getTime() - t > helper) {
		    				
		    				String addMedicao = "INSERT INTO Alerta (Alerta_ID, Hora, Leitura, Tipo, Mensagem, HoraEscrita, Zona_ID, Sensor_ID, Cultura_ID)" +
				    		        "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				    		System.out.println(Medicao_ID+","+Zona_ID+","+Sensor_ID+","+medicao+","+data+","+valido+","+ ultimasMedicoes);
				    		PreparedStatement preparedStatement = conn.prepareStatement(addMedicao);
				    		preparedStatement.setInt(1, (int)Math.pow(Medicao_ID, id+1));
				    		BigDecimal d = new BigDecimal(medicao);
				    		preparedStatement.setString(2, data);
				    		preparedStatement.setBigDecimal(3, d);
				    		preparedStatement.setString(4, isAlert[0]);
				    		preparedStatement.setString(5, isAlert[1]);
				    		preparedStatement.setString(6,timestamp2.toString());
				    		preparedStatement.setString(7, Zona_ID);
				    		preparedStatement.setString(8, Sensor_ID);
				    		preparedStatement.setInt(9, id);
				    		preparedStatement.executeUpdate();
		    				
		    			}
		    			
		    			
		    			if(helper >= 36000000) {
		    				
		    				Potato.getLastAlert().remove(id);
		    				Potato.getLastAlert().put(id, 1);
		    				
		    			}else {
		    				
		    				helper *=2 ;
		    				Potato.getLastAlert().remove(id);
		    				Potato.getLastAlert().put(id, helper);
		    				
		    			}
		    		}catch(Exception e) {
		    			
		    			System.out.println("EXception");
		    			e.printStackTrace();
		    			
		    			Potato.getLastAlert().put(id, 1);
		    			
		    			String addMedicao = "INSERT INTO Alerta (Alerta_ID, Hora, Leitura, Tipo, Mensagem, HoraEscrita, Zona_ID, Sensor_ID, Cultura_ID)" +
			    		        "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			    		System.out.println("2, "+Medicao_ID+","+Zona_ID+","+Sensor_ID+","+medicao+","+data+","+valido+","+ ultimasMedicoes);
			    		PreparedStatement preparedStatement = conn.prepareStatement(addMedicao);
			    		preparedStatement.setInt(1, (int)Math.pow(Medicao_ID, id+1));
			    		BigDecimal d = new BigDecimal(medicao);
			    		preparedStatement.setString(2, data);
			    		preparedStatement.setBigDecimal(3, d);
			    		preparedStatement.setString(4, isAlert[0]);
			    		preparedStatement.setString(5, isAlert[1]);
			    		preparedStatement.setString(6,timestamp2.toString());
			    		preparedStatement.setString(7, Zona_ID);
			    		preparedStatement.setString(8, Sensor_ID);
			    		preparedStatement.setInt(9, id);
			    		preparedStatement.executeUpdate();
	    				
		    			
		    		}
		    		
		        	
		        }
		        
		      }
		      st.close();
		      conn.close();
		    }catch (Exception e){
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		    }
		
    }
	
}
