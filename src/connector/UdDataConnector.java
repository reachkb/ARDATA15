package connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class UdDataConnector extends DataConnector {
	final static Logger logger = Logger.getLogger(UdDataConnector.class);
	
	private final String CONNECTOR_NAME = "ud";
	
	private static UdDataConnector thisInstance;
	
	private UdDataConnector() {
	}
	
	public static UdDataConnector getInstance() {
		if( thisInstance == null) {
			thisInstance = new UdDataConnector();
		}
		return thisInstance;
	}	
	
	public List<Map<String, Object>> getUdServers() throws Exception {
		List<Map<String, Object>> servers = new ArrayList<>();

		logger.info("Loading UD Windows Servers");
		servers.addAll(getData("nt" ));
		
		logger.info("Loading UD Unix Servers");
		servers.addAll(getData("unix" ));
		
		logger.info("Loading UD ESX Servers");
		servers.addAll(getData("esx" ));
		
		logger.info("Loading UD AS/400 Servers");
		servers.addAll(getData("as400" ));
		
		logger.info("Loading UD zOS Servers");
		servers.addAll(getData("zos" ));
		
		return servers;
	}

	private List<Map<String, Object>> getData( String dataName ) throws Exception {
		List<Map<String, Object>> servers = new ArrayList<>();
		servers.addAll(getData( CONNECTOR_NAME, String.format("%s.%s.sql", CONNECTOR_NAME, dataName) ));
		return servers;
	} 
}
