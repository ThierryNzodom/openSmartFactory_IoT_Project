package de.hs_lu.jdbcConnection;

@SuppressWarnings("serial")
public class NoConnectionException extends Exception {
	
	public NoConnectionException(String msg){
		super(msg);
	}
	
	public NoConnectionException(){
		super("Datenbankverbindung nicht verfügbar");
	}
	
}
