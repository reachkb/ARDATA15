package xrp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.RAW;
import util.DateUtil;
import xrp.dao.MetricDao;
import xrp.dao.MetricDefinitionDao;
import xrp.model.Metric;
import xrp.model.MetricDefinition;
import xrp.model.MetricDefinitionGroup;
import xrp.model.ui.AreaChart;
import xrp.model.ui.AreaChartData;


public class MetricService {

	public Map<String, Metric> generateMetrices( List<Map<String, Object>> xrpServers, List<Map<String, Object>> xrpApplications, List<Map<String, Object>> xrpApplicationServers, List<Map<String, Object>> xrpDatabase, List<Map<String, Object>> xrpStorage, List<Map<String, Object>> xrpServerExceptions, List<Map<String, Object>> xrpMappingExceptions ) {
		return generateMetrices( new Date(), xrpServers, xrpApplications, xrpApplicationServers, xrpDatabase, xrpStorage, xrpServerExceptions, xrpMappingExceptions ); 
	}
	
	public Map<String, Metric> generateMetrices( Date metricsDate, List<Map<String, Object>> xrpServers, List<Map<String, Object>> xrpApplications, List<Map<String, Object>> xrpApplicationServers, List<Map<String, Object>> xrpDatabase, List<Map<String, Object>> xrpStorage, List<Map<String, Object>> xrpServerExceptions , List<Map<String, Object>> xrpMappingExceptions) {
		Map<String, Metric> metrices = new HashMap<>();
		metrices.putAll(generateMappingMetrics(xrpServers, xrpApplications, xrpApplicationServers));
		metrices.putAll(generateServerDataQualityMetrics(xrpServers, new String[] {"LOGICAL_NAME", "UCMDB_ID", "OWNER"} ));
		metrices.putAll(generateApplicationDataQualityMetrics(xrpApplications, new String[] {"LOGICAL_NAME", "OWNER"} ));
		metrices.putAll(generateDiscoveryMetrics(xrpServers, xrpServerExceptions ));
		metrices.putAll(generateInfraServerMetrics(xrpServers));
		metrices.putAll(generateInfraApplicationMetrics(xrpApplications));
		metrices.putAll(generateInfraDatabaseMetrics(xrpDatabase) );
		metrices.putAll(generateInfraStorageMetrics(xrpStorage) );
		metrices.putAll(generateVirtualizationMetrics(xrpServers) );
		for( Metric metric: metrices.values() ) {
			metric.setDate(metricsDate);
		}
		return metrices; 
	}	
	
	public Map<String, Metric> generateMappingMetrics( List<Map<String, Object>> xrpServers, List<Map<String, Object>> xrpApplications, List<Map<String, Object>> xrpApplicationServers ) {
		int applicationCount = 0;
		int serverCount = 0;
		int productionServerCount = 0;
		int applicationMappedCount = 0;
		int serverMappedCount = 0;
		int productionServerMappedCount = 0;
		
		applicationCount = xrpApplications.size();
		
		serverCount = xrpServers.size();
		
		Map< String,Map<String, Object>> productionServers = new HashMap<>();
		for( Map<String, Object> row : xrpServers ) {
			if( "production".equalsIgnoreCase( (String )row.get("CG_ENVIRONMENT")) ) {
				productionServers.put((String )row.get("LOGICAL_NAME"), row);
			}
		}
		productionServerCount = productionServers.size();
		
		Map<String, Integer> mapApp = new HashMap<>();
		Map<String, Integer> mapServer = new HashMap<>();
		for( Map<String, Object> row : xrpApplicationServers ) {
			String appName = (String) row.get("UPSTREAMCI_LOGICAL_NAME");
			String serverName = (String) row.get("DOWNSTREAMCI_LOGICAL_NAME");
			Integer a = mapApp.get(appName);
			if( a ==  null ) {
				a = new Integer(1);
			} else {
				a = new Integer(a.intValue()+1);
			}
			mapApp.put(appName, a);
			
			Integer s = mapServer.get(serverName);
			if( s ==  null ) {
				s = new Integer(1);
			} else {
				s = new Integer(s.intValue()+1);
			}
			mapServer.put(serverName, s);
		}

		applicationMappedCount = mapApp.keySet().size();
		
		serverMappedCount = mapServer.keySet().size();
		
		for( String a : mapServer.keySet() ) {
			if( productionServers.get(a) != null ) {
				productionServerMappedCount++;
			}
		}
		
//		System.out.println("applicationCount: " + applicationCount);
//		System.out.println("applicationMappedCount: " + applicationMappedCount );
//		System.out.println("serverCount: " + serverCount );
//		System.out.println("serverMappedCount: " + serverMappedCount );
//		System.out.println("productionServerCount: " + productionServerCount );
//		System.out.println("productionServerMappedCount: " + productionServerMappedCount );

		Map<String, Metric> metrices = new HashMap<>();
		metrices.put( "server.mapped", new Metric(new Date(), "server.mapped", serverMappedCount, serverCount ) );
		metrices.put( "server.production.mapped", new Metric(new Date(), "server.production.mapped", productionServerMappedCount, productionServerCount ));
		metrices.put( "application.mapped", new Metric(new Date(), "application.mapped", applicationMappedCount, applicationCount));
		return metrices;
	}
	
	public Map<String, Metric> generateServerDataQualityMetrics( List<Map<String, Object>> xrpServers, String[] manadatoryFields ) {
		int serverTotal = 0;
		int serverIncomplete = 0;
		int serverProductionTotal = 0;
		int serverProductionIncomplete = 0;
		
		for( Map<String, Object> server : xrpServers ) {
			boolean incomplete = false;
			for( String manadatoryField : manadatoryFields ) {
				if( server.get(manadatoryField) != null ) {
					if("".equalsIgnoreCase((String)server.get(manadatoryField))) {
						incomplete = true;
					}
				} else {
					incomplete = true;
				}
			}
			if( incomplete ) {
				serverIncomplete++;
			}
			if( server.get("CG_ENVIRONMENT") != null ) {
				if("Production".equalsIgnoreCase((String)server.get("CG_ENVIRONMENT"))) {
					serverProductionTotal++;
					if( incomplete ) {
						serverProductionIncomplete++;
					}
				}
			}
			serverTotal++;
		}

//		System.out.println("serverTotal: " + serverTotal );
//		System.out.println("serverIncomplete: " + serverIncomplete );
//		System.out.println("serverProductionTotal: " + serverProductionTotal );
//		System.out.println("serverProductionIncomplete: " + serverProductionIncomplete );
		
//		List<Map<String, Object>> metrices = new ArrayList<>();
//		metrices.add(createMetrics("serverComplete", new Integer(serverTotal-serverIncomplete).toString() ));
//		metrices.add(createMetrics("serverCompleteProd", new Integer(serverProductionTotal-serverProductionIncomplete).toString() ));
		
		Map<String, Metric> metrices = new HashMap<>();
		metrices.put( "server.complete", new Metric(new Date(), "server.complete", (serverTotal-serverIncomplete), serverTotal ) );
		metrices.put( "server.production.complete", new Metric(new Date(), "server.production.complete", (serverProductionTotal-serverProductionIncomplete), serverProductionTotal ));
		return metrices;
	}

	public Map<String, Metric> generateApplicationDataQualityMetrics( List<Map<String, Object>> xrpApplications, String[] manadatoryFields ) {
		int applicationTotal = 0;
		int applicationIncomplete = 0;
		
		for( Map<String, Object> xrpApplication : xrpApplications ) {
			boolean incomplete = false;
			for( String manadatoryField : manadatoryFields ) {
				if( xrpApplication.get(manadatoryField) != null ) {
					if("".equalsIgnoreCase((String)xrpApplication.get(manadatoryField))) {
						incomplete = true;
					}
				} else {
					incomplete = true;
				}
			}
			if( incomplete ) {
				applicationIncomplete++;
			}
			applicationTotal++;
		}
//		System.out.println("applicationTotal: " + applicationTotal );
//		System.out.println("applicationIncomplete: " + applicationIncomplete );
//		
//		List<Map<String, Object>> metrices = new ArrayList<>();
//		metrices.add(createMetrics("appComplete", new Integer(applicationTotal-applicationIncomplete).toString() ));
		
		Map<String, Metric> metrices = new HashMap<>();
		metrices.put( "application.complete", new Metric(new Date(), "application.complete", (applicationTotal-applicationIncomplete), applicationTotal ) );
		return metrices;
	}
	
	public Map<String, Metric> generateDiscoveryMetrics( List<Map<String, Object>> xrpServers, List<Map<String, Object>> xrpServerException ) {
		Map<String, Metric> metrices = new HashMap<>();
		
		Map<String, Map<String, Object>> mapServerException = getHashMap(xrpServerException, "HOSTNAME");
		
		int exceptionCount = 0;
		int udCount = 0;
		int smCount = 0;
		int bothCount = 0;
		for( Map<String, Object> xrpServer : xrpServers ) {
			String hostName = (String) xrpServer.get("LOGICAL_NAME");
			if( hostName != null ) {
				hostName = hostName.toLowerCase();
			}
			if( "BOTH".equalsIgnoreCase((String)xrpServer.get("XRP_SOURCE")) ) {
				bothCount++;
			} else if( "UD".equalsIgnoreCase((String)xrpServer.get("XRP_SOURCE"))  ) {
				udCount++;
			} else if( "SM".equalsIgnoreCase((String)xrpServer.get("XRP_SOURCE"))  ) {
				smCount++;
				if( mapServerException.get(hostName) != null ) {
					exceptionCount++;
				}
			}
		}
		
		System.out.println("udCount: " + udCount );	
		System.out.println("smCount: " + smCount );
		System.out.println("bothCount: " + bothCount );
		System.out.println("exceptionCount: " + exceptionCount );
		
		metrices.put( "server.discovered", new Metric(new Date(), "server.discovered", bothCount+exceptionCount, bothCount+smCount ) );
		metrices.put( "server.new", new Metric(new Date(), "server.new", udCount, bothCount+smCount ) );
		return metrices;
	}
	
	public Map<String, Metric> generateInfraApplicationMetrics( List<Map<String, Object>> xrpApplications ) {
		Map<String, Metric> metrices = new HashMap<>();
		metrices.put( "application", new Metric(new Date(), "application", xrpApplications.size(), xrpApplications.size() ) );
		return metrices;
	}	
	
	public Map<String, Metric> generateInfraServerMetrics( List<Map<String, Object>> xrpServer ) {
		Map<String, Metric> metrices = new HashMap<>();
		metrices.put( "server", new Metric(new Date(), "server", xrpServer.size(), xrpServer.size() ) );
		return metrices;
	}		
	
	public Map<String, Metric> generateInfraDatabaseMetrics( List<Map<String, Object>> xrpDatabase ) {
		Map<String, Metric> metrices = new HashMap<>();
		metrices.put( "database", new Metric(new Date(), "database", xrpDatabase.size(), xrpDatabase.size() ) );
		return metrices;
	}			
	
	public Map<String, Metric> generateInfraStorageMetrics( List<Map<String, Object>> xrpStorage ) {
		Map<String, Metric> metrices = new HashMap<>();
		metrices.put( "storage", new Metric(new Date(), "storage", xrpStorage.size(), xrpStorage.size() ) );
		return metrices;
	}	
	
	public Map<String, Metric> generateVirtualizationMetrics( List<Map<String, Object>> xrpServers ) {
		int virtualCount = 0;
		for( Map<String, Object> xrpServer : xrpServers ) {
			if( xrpServer.get("VIRTUAL") != null ) {
				if("Y".equalsIgnoreCase((String)xrpServer.get("VIRTUAL"))) {
					virtualCount++;
				}
			} else if(xrpServer.get("SERIAL_NO_") != null ) {
				if( (((String)xrpServer.get("SERIAL_NO_")).toLowerCase()).contains("vmware")) {
					virtualCount++;
				}
			}
		}
//		List<Map<String, Object>> metrices = new ArrayList<>();
//		metrices.add(createMetrics("virtual", new Integer(virtualCount).toString() ));
//		metrices.add(createMetrics("physical", new Integer(xrpServers.size()-virtualCount).toString() ));
		Map<String, Metric> metrices = new HashMap<>();
		metrices.put( "server.physical", new Metric(new Date(), "server.physical", (xrpServers.size()-virtualCount), xrpServers.size() ) );
		metrices.put( "server.virtual", new Metric(new Date(), "server.virtual", virtualCount, xrpServers.size() ) );
		return metrices;
	}			


	public void saveMetrices( List<Metric> metrices, Date metricDate ) {
		metricDate = DateUtil.removeTime(metricDate);
		for( Metric metric: metrices) {
			metric.setDate(metricDate);
		}
		saveMetrices(metrices);
	}
	
	public void saveMetrices( List<Metric> metrices ) {
		MetricDao md = new MetricDao();
		if( metrices.size() > 0) {
			md.deleteMetrices(metrices.get(0).getDate());
		}
		md.saveMetrices(metrices);
	}

//	public List<String> getMetricDefinitions() {
//		return (new MetricDao()).getMetricDefinitions();
//	}
	
	public Map<String, Metric> getPreviousMetrices() {
		MetricDao md = new MetricDao();
		return md.getPreviousMetrices();
	}
	
	public Map<String, Metric> getCurrentMetrices() {
		MetricDao md = new MetricDao();
		return md.getCurrentMetrices();
	}
	
	private Map<String, Map<String, Object>> getHashMap( List<Map<String, Object>> data, String fieldName ) {
		Map<String, Map<String, Object>> map = new HashMap<>();
		for( Map<String, Object> row : data ) {
			String fieldValue = "";
			if( row.get(fieldName) instanceof RAW ) {
				try {
					fieldValue =  new String( RAW.newRAW(row.get(fieldName)).stringValue() );
				} catch( Exception ex ) {
					fieldValue = null;
				}
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

	public List<MetricDefinitionGroup> getMetricDefinitionGroups() {
		return (new MetricDefinitionDao()).getMetricDefinitionGroups();
	}

	public List<MetricDefinition> getMetricDefinitions() {
		return (new MetricDefinitionDao()).getMetricDefinitions();
	}

	
	public Map<String, List<Metric>> getMetrices() {
		return (new MetricDao()).getMetrices(10);
	}
	
	public List<AreaChart> getMetricChartData() {
		Map<String, List<Metric>> metricMap = getMetrices();
		
		List<MetricDefinition> mds = getMetricDefinitions();
		HashMap<String, String> mapMetricType = new HashMap<>();
		for(MetricDefinition md: mds ) {
			mapMetricType.put(md.getName(), md.getType());
		}
		
		
		List<AreaChart> chartList = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		for( String metricName : metricMap.keySet() ) {
			List<Metric> metricList = metricMap.get(metricName);
			int counter = 1;
			int currentCount = 0;
			int currentTotal = 0;
			int previousCount = 0;
			int previousTotal = 0;
			
			AreaChart ac = new AreaChart(metricName, 0, 0, 0, null);
			List<AreaChartData> acds = new ArrayList<>();
			for( Metric metric : metricList) {
				if( counter == 1 ) {
					currentCount = metric.getCount();
					currentTotal = metric.getTotal();
				}
				if( counter == 2 ) {
					previousCount = metric.getCount();
					previousTotal = metric.getTotal();
				}
				String type = "count";
				if( mapMetricType.get(metricName) != null ) {
					type = mapMetricType.get(metricName);
				}
				
				if( "percentage".equalsIgnoreCase(type) ) {
					AreaChartData acd = new AreaChartData( sdf.format(metric.getDate()), metric.getPercentage(), counter );
					acds.add(acd);
				} else {
					AreaChartData acd = new AreaChartData( sdf.format(metric.getDate()), metric.getCount(), counter );
					acds.add(acd);
				}
				counter++;
			}
			
		    Collections.sort(acds, new Comparator<AreaChartData>() {
		    	@Override
		        	public int compare( AreaChartData mmd1, AreaChartData mmd2 ) {
		        		return (mmd1.getOrder() < mmd2.getOrder()) ? 1 : -1;
		        	}
				});
			
			ac.setData(acds);
			ac.setCount(currentCount);
			ac.setChange(currentCount-previousCount);
			if( currentCount == 0 ) {
				ac.setPercentage(0);
			} else {
				ac.setPercentage((currentCount-previousCount)*100/currentCount);
			}
			chartList.add(ac);
		}
		
		return chartList;
	}	
	
}
