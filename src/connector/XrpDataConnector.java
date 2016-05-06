package connector;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import oracle.sql.RAW;

public class XrpDataConnector extends DataConnector {
	final static Logger logger = Logger.getLogger(XrpDataConnector.class);
	
	private final String CONNECTOR_NAME = "xrp";
	
	private static XrpDataConnector thisInstance;
	
	private XrpDataConnector() {
	}
	
	public static XrpDataConnector getInstance() {
		if( thisInstance == null) {
			thisInstance = new XrpDataConnector();
		}
		return thisInstance;
	}
	

	public void saveUdServers( List<Map<String, Object>> udServers ) throws Exception {
		logger.info("Saving UD Servers");
		saveData(udServers, CONNECTOR_NAME, "ud.server");
	}
	
	public void saveSmServers( List<Map<String, Object>> smServers ) throws Exception {
		logger.info("Saving SM Servers");
		saveData(smServers, CONNECTOR_NAME, "sm.server");
	}

	public void saveSmApplications( List<Map<String, Object>> smApplications ) throws Exception {
		logger.info("Saving SM Applications");
		saveData(smApplications, CONNECTOR_NAME, "sm.application");
	}	

	public void saveSmDatabases( List<Map<String, Object>> smApplications ) throws Exception {
		logger.info("Saving SM Databases");
		saveData(smApplications, CONNECTOR_NAME, "sm.database");
	}	
	
	public void saveSmStorages( List<Map<String, Object>> smApplications ) throws Exception {
		logger.info("Saving SM Storage");
		saveData(smApplications, CONNECTOR_NAME, "sm.storage");
	}		
	
	public void saveSmApplicationServers( List<Map<String, Object>> smApplicationServers ) throws Exception {
		logger.info("Saving SM Mapping");
		saveData(smApplicationServers, CONNECTOR_NAME, "sm.application.server");
	}
	
	public void saveXrpServers( List<Map<String, Object>> xrpServers ) throws Exception {
		logger.info("Saving XRP Servers");
		saveData(xrpServers, CONNECTOR_NAME, "server");
	}
	
	public List<Map<String, Object>> getXrpServers() throws Exception {
		logger.info("Loading XRP Servers");
		String dataName = "server";
		List<Map<String, Object>> servers = new ArrayList<>();
		servers.addAll(getData( CONNECTOR_NAME, String.format("%s.%s.sql", CONNECTOR_NAME, dataName ) ));
		return servers;
	}	

	public List<Map<String, Object>> getServerExceptions() throws Exception {
		logger.info("Loading XRP Server Exceptions");
		String dataName = "server.exception";
		List<Map<String, Object>> servers = new ArrayList<>();
		servers.addAll(getData( CONNECTOR_NAME, String.format("%s.%s.sql", CONNECTOR_NAME, dataName ) ));
		return servers;
	}
	
	public List<Map<String, Object>> getMappingExceptions() throws Exception {
		logger.info("Loading XRP Mapping Exceptions");
		String dataName = "mapping.exception";
		List<Map<String, Object>> servers = new ArrayList<>();
		servers.addAll(getData( CONNECTOR_NAME, String.format("%s.%s.sql", CONNECTOR_NAME, dataName ) ));
		return servers;
	}	
	
	
	public List<Map<String, Object>> getXprServers( List<Map<String, Object>> udServers, List<Map<String, Object>> smServers ) throws Exception {
		logger.info("Reconciling UD and SM Servers");
		
		List<Map<String, Object>> servers = new ArrayList<>();

		Map<String, Map<String, Object>> smMapLogicalName = getHashMap(smServers, "LOGICAL_NAME");
		Map<String, Map<String, Object>> smMapUcmdbId = getHashMap(smServers, "UCMDB_ID");
		
		servers.addAll(smServers);
		for( Map<String, Object> server : servers ) {
			server.put("XRP_SOURCE", 			"SM");
		}
		for( Map<String, Object> server : udServers ) {
			String hostName = (String) server.get("HOST_NAME");
			String globalId =  new String( RAW.newRAW(server.get("GLOBAL_ID")).stringValue() );
			//System.out.println( ">>> " + hostName);
			if( hostName != null ) {
				if( smMapLogicalName.get(hostName.toLowerCase()) != null) {
 					servers.remove(smMapLogicalName.get(hostName.toLowerCase()));
					Map<String, Object> mergedServer = mergeServer( server, smMapLogicalName.get(hostName) );
					servers.add(mergedServer);
				} else {
					if( smMapUcmdbId.get(globalId.toLowerCase()) != null) {
						servers.add(mergeServer( server, smMapUcmdbId.get(globalId) ));
					} else {
						servers.add(appendUdServer( server ));
					}
				}
			}
		}
		//System.out.println("Merged: " + merged);
		//System.out.println("Appended: " + appended);
		//System.out.println( servers );
		return servers;
	}
	
	private Map<String, Map<String, Object>> getHashMap( List<Map<String, Object>> data, String fieldName ) throws SQLException {
		Map<String, Map<String, Object>> map = new HashMap<>();
		for( Map<String, Object> row : data ) {
			String fieldValue = "";
			if( row.get(fieldName) instanceof RAW ) {
				fieldValue =  new String( RAW.newRAW(row.get(fieldName)).stringValue() );
			} else {
				fieldValue = (String) row.get(fieldName);
			}
			if( fieldValue != null ) {
				//System.out.println( "fieldName: " +  fieldName + "  fieldValue: " + fieldValue);
				map.put( fieldValue.toLowerCase(), row );
			}
		}
		return map;
	}

	private Map<String, Object> mergeServer( Map<String, Object> udServer, Map<String, Object> smServer ) {
		Map<String, Object> server = smServer;
		server.put("OPERATING_SYSTEM", udServer.get("OS_NAME") );
		server.put("VENDOR", 				udServer.get("VENDOR"));
		server.put("MODEL", 				udServer.get("DIS_MODEL"));
		server.put("NETWORK_NAME", 			udServer.get("HOST_NAME"));
		server.put("SERIAL_NO_", 			udServer.get("SERIALNUMBER"));
		server.put("PRODUCT_VERSION", 		udServer.get("OS_VERSION"));
		server.put("DOMAIN", 				udServer.get("A_DOMAIN_NAME"));
		server.put("OPERATING_SYSTEM", 		udServer.get("OS_NAME"));
		//server.put("SERVER_ID", 			udServer.get("HOST_NAME"));
		//server.put("MANUFACTURER", 			udServer.get("VENDOR"));
		server.put("MANUFACTURER", 			udServer.get("VENDOR"));
		server.put("XRP_SOURCE", 			"BOTH");
		return server;
	}

	private Map<String, Object> appendUdServer( Map<String, Object> udServer ) throws SQLException {
		Map<String, Object> server = new HashMap<>();
		server.put("ID", 					udServer.get("HOST_NAME"));
		server.put("LOGICAL_NAME", 			udServer.get("HOST_NAME"));
		server.put("ISTATUS", 				"Discovered");
		server.put("TYPE", 					"UNKNOWN");
		server.put("SUBTYPE", 				"UNKNOWN");
		server.put("CG_ENVIRONMENT", 		"UNKNOWN");
		server.put("ASSIGNMENT", 			"UNKNOWN");
		server.put("VENDOR", 				udServer.get("VENDOR"));
		server.put("MODEL", 				udServer.get("DIS_MODEL"));
		server.put("NETWORK_NAME", 			udServer.get("HOST_NAME"));
		server.put("SERIAL_NO_", 			udServer.get("SERIALNUMBER"));
		server.put("LOCATION", 				"UNKNOWN");
		server.put("UPDATED_BY", 			"UD");
		server.put("CONTAINER", 			"UNKNOWN");
		server.put("BUILDING", 				"UNKNOWN");
		server.put("SYSMODTIME", 			null);
		server.put("SYSMODUSER", 			"UD");
		server.put("SITE_CATEGORY", 		"UNKNOWN");
		server.put("OWNER", 				"UNKNOWN");
		server.put("UCMDB_ID", 				new String( RAW.newRAW(udServer.get("GLOBAL_ID")).stringValue() ));
		server.put("CREATED_BY", 			"UD");
		server.put("CREATED_BY_DATE", 		null);
		server.put("PRODUCT_VERSION", 		udServer.get("OS_VERSION"));
		server.put("CG_CREATEDATE", 		null);
		server.put("CG_CREATEDBY", 			"UNKNOWN");
		server.put("DOMAIN", 				udServer.get("A_DOMAIN_NAME"));
		server.put("OPERATING_SYSTEM", 		udServer.get("OS_NAME"));
		server.put("SERVER_ID", 			udServer.get("HOST_NAME"));
		server.put("MANUFACTURER", 			udServer.get("VENDOR"));
		server.put("IP_ADDRESS", 			"UNKNOWN");
		server.put("MAC_ADDRESS", 			"UNKNOWN");
		server.put("SUBNET_MASK", 			"UNKNOWN");
		server.put("DEFAULT_GATEWAY", 		"UNKNOWN");
		server.put("SUPPORT_GROUPS", 		"UNKNOWN");
		server.put("CG_SVC_TIER", 			"UNKNOWN");
		server.put("CG_CREATEDBY_FULLNAME", "UD");
		server.put("CG_SERVICE_TIER", 		"UNKNOWN");
		server.put("CG_SYSTEM_TYPE", 		"UNKNOWN");
		server.put("CG_PRIMARYFUNCTION", 	"UNKNOWN");
		server.put("XRP_SOURCE", 			"UD");
		return server;
	}	
	
}
