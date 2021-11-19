package de.hs_lu.fetchMsgBrokerInDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.mysql.jdbc.DatabaseMetaData;

import de.hs_lu.jdbcConnection.MySQLAccess;
import de.hs_lu.jdbcConnection.NoConnectionException;

public class AppInstallTestTable {
	
	Connection dbConn;	
	Timestamp timeStamp;
	String moduleDescription;	
	String kpiName;
	String kpiValue;
	
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public String getModuleDescription() {
		return moduleDescription;
	}
	public String getKpiValue() {
		return kpiValue;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiValue(String kpiValue) {
		this.kpiValue = kpiValue;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
	}
	public AppInstallTestTable() throws NoConnectionException {
		dbConn = new MySQLAccess().getConnection();
	}		
	
	public void doSomething() throws SQLException{
		//do something
	}	
	public boolean tableExists() throws SQLException{
		DatabaseMetaData metadata = (DatabaseMetaData) dbConn.getMetaData();
		ResultSet rSet;
		rSet = metadata.getTables(null, null, "FACT_TABLE", null);
		if(rSet.next()){
			return true;			
		} else return false;
	}
	public void createTableTest() throws SQLException{
		if (tableExists()) {
			System.out.println("Die Tabelle FACT_TABLE existiert bereits");
			System.out.println("------------------------------");
			System.out.println();
		} else {
		String sql = "CREATE TABLE FACT_TABLE (" + 
				" moduleDescription VARCHAR(255) " + 				
				" kpiName VARCHAR(50), " +
				" kpiValue VARCHAR(255), " +
			    ");" ;
		System.out.println(sql);
		this.dbConn.createStatement().executeUpdate(sql);
		System.out.println("Tabelle FACT_TABLE erfolgreich erstellt");
		System.out.println("------------------------------");
		}
	}
	public void insertTableTest() throws SQLException{
		String sql = "INSERT INTO FACT_TABLE " +
	    		"(timeStamp, moduleDescription, kpiName, kpiValue)" + 
	    		" values (?, ?, ?, ?)";
		//System.out.println(sql);
		
		PreparedStatement preparedStmt = dbConn.prepareStatement(sql);
		preparedStmt.setTimestamp(1, this.getTimeStamp());
	    preparedStmt.setString(2, this.getModuleDescription());
	    preparedStmt.setString (3, this.getKpiName());
	    preparedStmt.setString (4, this.getKpiValue());
	    System.out.println(preparedStmt.toString());

	    preparedStmt.execute();
	    System.out.println("Die Modul-Daten wurden erfolgreich in der Datenbank gespeichert");
	    System.out.println("------------------------------");
	    this.dbConn.close();
	}	
	public void readTableTest() throws SQLException{
		String sql = "SELECT * FROM FACT_TABLE";
		System.out.println(sql);
		this.dbConn.createStatement().executeQuery(sql);
		System.out.println("Tabelle FACT_TABLE erfolgreich gelesen");
		System.out.println("------------------------------");
	}
	public void dropTableTest() throws SQLException{
		String sql = "Drop TABLE FACT_TABLE";
		System.out.println(sql);
		this.dbConn.createStatement().executeUpdate(sql);
		System.out.println("Tabelle FACT_TABLE erfolgreich gelöscht");
		System.out.println("------------------------------");
	}
}
