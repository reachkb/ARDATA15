package main;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import connector.SmDataConnector;
import connector.UdDataConnector;
import connector.XrpDataConnector;
import util.EmailUtil;
import xrp.model.Metric;
import xrp.service.MetricService;


public class Main {
	
	final static Logger logger = Logger.getLogger(Main.class);

	public static void main( String[] args ) throws Exception {
		try {
			loadData(args);
			
		} catch( Exception ex ) {
			logger.error("Failed to load data", ex );
			//EmailUtil.sendMail("From Adil", "This is a test message");
		}
	}
	
	public static void loadData( String[] args ) throws Exception {

		logger.info("Initializing data connectors");
		UdDataConnector udDataConnector = UdDataConnector.getInstance();
		SmDataConnector smDataConnector = SmDataConnector.getInstance();
		XrpDataConnector xrpDataConnector = XrpDataConnector.getInstance();

		logger.info("Loading UD data");
		List<Map<String, Object>> udServers = udDataConnector.getUdServers();
		
		logger.info("Loading SM data");
		List<Map<String, Object>> smServers = smDataConnector.getSmServers();
		List<Map<String, Object>> smApplications = smDataConnector.getSmApplications();
		List<Map<String, Object>> smApplicationServers = smDataConnector.getSmApplicationServers();
		List<Map<String, Object>> smDatabase = smDataConnector.getSmDatabases();
		List<Map<String, Object>> smStorages = smDataConnector.getSmStorages();

		logger.info("Saving UD data");
		xrpDataConnector.saveUdServers(udServers);
		
		logger.info("Saving SM data");
		xrpDataConnector.saveSmServers(smServers);
		xrpDataConnector.saveSmApplications(smApplications);
		xrpDataConnector.saveSmApplicationServers(smApplicationServers);
		xrpDataConnector.saveSmDatabases(smDatabase);
		xrpDataConnector.saveSmStorages(smStorages);
		
		logger.info("Reconciling UD and SM data");
		List<Map<String, Object>> xrpServers = xrpDataConnector.getXprServers( udServers, smServers );

		logger.info("Saving XRP data");
		xrpDataConnector.saveXrpServers(xrpServers);

		logger.info("Loading XRP exception data");
		List<Map<String, Object>> xrpServerExceptions = xrpDataConnector.getServerExceptions();
		List<Map<String, Object>> xrpMappingExceptions = xrpDataConnector.getMappingExceptions();

		
		logger.info("Calculating daily metrices");
		MetricService metricService= new MetricService();
		Map<String, Metric> metrices = metricService.generateMetrices(xrpServers, smApplications, smApplicationServers, smDatabase, smStorages, xrpServerExceptions, xrpMappingExceptions);
		
		logger.info("Saving daily metrices");
		metricService.saveMetrices(new ArrayList<Metric>(metrices.values()));
		
		
		logger.info("Successfully finished loading process");
	}

	
}
