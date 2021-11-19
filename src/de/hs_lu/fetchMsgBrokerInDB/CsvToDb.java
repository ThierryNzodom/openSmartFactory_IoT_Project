package de.hs_lu.fetchMsgBrokerInDB;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.DatabaseMetaData;

import de.hs_lu.jdbcConnection.MySQLAccess;
import de.hs_lu.jdbcConnection.NoConnectionException;

public class CsvToDb {
	String jsonFromCsv = "[{ FIELD1: \"2017-04-27 12:01:47\", FIELD2: \"SF/SMoProL/Evt\", FIELD3: \"{Robotino:{BatteryVoltage:'21.455'}}\" }, { FIELD1: \"2017-04-27 12:01:47\", FIELD2: \"SF/SMoProL/Evt\", FIELD3: \"{Robotino:{RobState:'IDLE'}}\" }, { FIELD1: \"2017-04-27 12:01:47\", FIELD2: \"SF/SMoProL/Evt\", FIELD3: \"{Robotino:{Position:'0'}}\" }]"; 
	//String jsonFromCsv = "[{FIELD1: \"2017-04-27 08:29:37\",FIELD2: \"SF/SMoProL/Evt/DockingStation/smartfactory.docking2/JSON/OPCUA/TransportStatus\",FIELD3: \"{value:'3'}\" }, {FIELD1: \"2017-04-27 08:29:37\",FIELD2: \"SF/SMoProL/Evt/DockingStation/smartfactory.docking2/JSON/OPCUA/IncomingPriority\",FIELD3: \"{value:'1'}\" }]";
	//{ FIELD1: \"2017-04-27 12:01:47\", FIELD2: \"SF/SMoProL/Evt\", FIELD3: \"{Robotino:{BatteryVoltage:'21.455'}}\" }, { FIELD1: \"2017-04-27 12:01:47\", FIELD2: \"SF/SMoProL/Evt\", FIELD3: \"{Robotino:{RobState:'IDLE'}}\" }, { FIELD1: \"2017-04-27 12:01:47\", FIELD2: \"SF/SMoProL/Evt\", FIELD3: \"{Robotino:{Position:'0'}}\" }
	String kpiName;
	String kpiValue;
	String moduleDescription;
	Timestamp timestamp;
	Connection dbConn;
	String time;
	
	public CsvToDb() throws NoConnectionException{
		//this.dbConn = new MySQLAccess().getConnection();
	}
	
	public void MsgToDb () throws SQLException {
		try {
			JSONArray jsonArray = new JSONArray(jsonFromCsv);

			for (int i=0;i<jsonArray.length();i++){
				//System.out.println(jsonArray.get(i).toString());
				JSONObject json = new JSONObject(jsonArray.get(i).toString());
				
//				Iterator iter = json.keys();
				this.timestamp = Timestamp.valueOf(json.get("FIELD1").toString());
				this.time = json.get("FIELD1").toString();
				System.out.println("timestamp \t\t: "+timestamp.toString());
				//System.out.println("timestamp \t\t: "+time);
				if(!"mqout".equals(json.get("FIELD2").toString())){
					System.out.println(json.get("FIELD2").toString());
					if (!json.get("FIELD3").toString().contains("<")){
						System.out.println(json.get("FIELD3").toString());
						JSONObject messageObj = new JSONObject(json.get("FIELD3").toString());
						
						Iterator<?> msgIter = messageObj.keys();
						String description = msgIter.next().toString();
						if(!"value".equals(description)){
							this.moduleDescription = description;
							JSONObject kpiObj = messageObj.getJSONObject(this.moduleDescription);
							Iterator<?> kpiIter = kpiObj.keys();
							this.kpiName = kpiIter.next().toString();
							this.kpiValue = kpiObj.getString(this.kpiName);
							System.out.println("moduleDescription \t: "+this.moduleDescription.toString());
							System.out.println("kpiName \t\t: "+this.kpiName.toString());
							System.out.println("kpiValue \t\t: "+this.kpiValue.toString());
							this.insertTableTest();
						}
					}
				}else{System.out.println("mqout");}
				System.out.println("====================================================================== "+i+"-"+jsonArray.length()+"%");
				//Iterator
				//Iterator iter = json.keys();
				//String field1 = iter.next().toString();
				//JSONObject msgTimestamp = json.getJSONObject(field1);
				//System.out.println(msgTimestamp.toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public String getKpiValue() {
		return kpiValue;
	}

	public void setKpiValue(String kpiValue) {
		this.kpiValue = kpiValue;
	}

	public String getModuleDescription() {
		return moduleDescription;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
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

	public void insertTableTest() throws SQLException, NoConnectionException{
		this.dbConn = new MySQLAccess().getConnection();
		String sql = "INSERT INTO FACT_TABLE " +
	    		"(moduleDescription, kpiName, kpiValue, timeStamp)" + 
	    		" values (?, ?, ?, ?)";
		/*String sql = "INSERT INTO FACT_TABLE " +
	    		"(moduleDescription, kpiName, kpiValue)" + 
	    		" values (?, ?, ?)";*/
		
		PreparedStatement preparedStmt = dbConn.prepareStatement(sql);
		//preparedStmt.setTimestamp(1, this.getTimestamp());
		
	    preparedStmt.setString (1, this.getModuleDescription());
	    preparedStmt.setString (2, this.getKpiName());
	    preparedStmt.setString (3, this.getKpiValue());
	    preparedStmt.setTimestamp(4, this.getTimestamp());
		//preparedStmt.setString (4, this.time);
	    System.out.println(preparedStmt.toString());	   
	    preparedStmt.execute();
	    System.out.println("Die Modul-Daten wurden erfolgreich in der Datenbank gespeichert");
	    System.out.println("------------------------------");
	    this.dbConn.close();
	}	
	
}
