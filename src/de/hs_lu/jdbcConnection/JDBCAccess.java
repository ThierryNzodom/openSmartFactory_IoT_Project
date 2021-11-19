package de.hs_lu.jdbcConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class JDBCAccess {

	Connection dbConn;
	String dbDrivername;
	String dbURL;
	String dbUserid;
	String dbPassword;
	
	JDBCAccess() throws NoConnectionException {
		this.setParms();	
	}
	abstract void setParms();
	
	void createConnection() throws NoConnectionException{
		
		try{			
			System.out.println("mySQL-Treiber wurden erfolgreich geladen");
			System.out.println("DriverName: " + Class.forName(dbDrivername));
			dbConn = DriverManager.getConnection(
													dbURL,
													dbUserid,
													dbPassword
												);			
			System.out.println("Datenbankverbindung erfolgreich hergestellt :)");
			System.out.println("------------------------------");
		}catch(Exception e){
			e.printStackTrace();
			throw new NoConnectionException("Aufbau der Datenbankverbindung fehlgeschlagen");
		}
	}
	
	public Connection getConnection() throws NoConnectionException {		
		this.createConnection();
		return dbConn;
	}	
}