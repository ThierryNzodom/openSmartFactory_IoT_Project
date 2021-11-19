package de.hs_lu.jdbcConnection;

public class MySQLAccess extends JDBCAccess {

	
	void setParms(){
		dbDrivername = "com.mysql.jdbc.Driver";		
		dbURL        = "jdbc:mysql://143.93.203.198/smartfactory";
		dbUserid 	 = "smartfactory";
		dbPassword   = "pw_smartfactory";
	}
	
	public MySQLAccess() throws NoConnectionException {
		super();
	}
	
	public static void main(String[] args) throws NoConnectionException {
		System.out.println("Herstellung einer Datenbankverbindung...");
		new MySQLAccess().getConnection();
	}

}
