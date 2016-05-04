package connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class SmDataConnector extends DataConnector {
	final static Logger logger = Logger.getLogger(SmDataConnector.class);
	
	private final String CONNECTOR_NAME = "sm";
	
	private static SmDataConnector thisInstance;
	
	private SmDataConnector() {
	}
	
	public static SmDataConnector getInstance() {
		if( thisInstance == null) {
			thisInstance = new SmDataConnector();
		}
		return thisInstance;
	}	
	
	public List<Map<String, Object>> getSmApplicationServers() throws Exception {
		logger.info("Loading SM Mappings");
		return getData( "application.server" );
	}
	
	public List<Map<String, Object>> getSmApplications() throws Exception {
		logger.info("Loading SM Applciations");
		return getData( "application" );
	}
	
	public List<Map<String, Object>> getSmServers() throws Exception {
		logger.info("Loading SM Servers");
		return getData( "server" );
	}

	public List<Map<String, Object>> getSmDatabases() throws Exception {
		logger.info("Loading SM Databases");
		return getData( "database" );
	}
	
	public List<Map<String, Object>> getSmStorages() throws Exception {
		logger.info("Loading SM Storage");
		return getData( "storage" );
	}	
	
	private List<Map<String, Object>> getData( String dataName ) throws Exception {
		List<Map<String, Object>> servers = new ArrayList<>();
		servers.addAll(getData( CONNECTOR_NAME, String.format("%s.%s.sql", CONNECTOR_NAME, dataName ) ));
		return servers;
	} 
	
}
