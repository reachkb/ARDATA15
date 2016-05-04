package xrp.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import db.DBConnect;
import db.NamedParameterStatement;
import xrp.model.DashboardChart;
import xrp.model.DashboardChartData;

public class DashboardDao {
	final static Logger logger = Logger.getLogger(DashboardDao.class);
	
	public DashboardChart getServerTrendChartData() {
		DashboardChart chart = new DashboardChart("dashboard.server.trend", "Server Trend", "area", 1, new ArrayList<String>(), new ArrayList<DashboardChartData>(), null);
		String sql = "SELECT METRIC_DATE, T_BUSINESS, T_SUB_BUSINESS, METRIC_NAME, METRIC_VALUE, METRIC_COUNT, METRIC_TOTAL FROM AR_METRICES WHERE METRIC_NAME = 'server' AND METRIC_DATE > (SYSDATE-7) ORDER BY METRIC_DATE DESC ";
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = nps.executeQuery();
			int order = 0;
			while( rs.next() ) {
				Timestamp timestamp = rs.getTimestamp("METRIC_DATE");
				String name = "00-00";
				if (timestamp != null)
					name = sdf.format(new java.util.Date(timestamp.getTime()));
				chart.getMetric().add(new DashboardChartData(name , rs.getInt("METRIC_COUNT"), "", null, ++order));
			}
			rs.close();
			rs = null;
			nps.close();
			nps = null;
		} catch( Exception  ex ) {
			chart.getMetric().add(new DashboardChartData( "00-00" , 0, "", null, 1));
			logger.warn("Unable to get server trend chart data", ex);
		}
		
		if( chart.getMetric() != null ) {
	    Collections.sort(chart.getMetric(), new Comparator<DashboardChartData>() {
	    	@Override
	        	public int compare( DashboardChartData e1, DashboardChartData e2 ) {
	        		return (e1.getOrder() < e2.getOrder()) ? 1 : -1;
	        	} 
			});	
		}
		return chart;
	}
	
	public DashboardChart getServerVirtualizationChartData() {
		DashboardChart chart = new DashboardChart("dashboard.server.virtualization", "Server Virtualization", "pie", 2, new ArrayList<String>(), new ArrayList<DashboardChartData>(), null);
		String sql = "SELECT COUNT(*) AS COUNT, 'V' AS TYPE from AR_XRP_SERVER where XRP_SOURCE <> 'UD' AND LOWER(SERIAL_NO_) like '%vmware%' " +
						"UNION " +
						" SELECT COUNT(*) AS COUNT, 'T' AS TYPE from AR_XRP_SERVER where XRP_SOURCE <> 'UD' ";

		int total = 0;
		int virtual = 0;
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = nps.executeQuery();
			while( rs.next() ) {
				if( "T".equalsIgnoreCase(rs.getString("TYPE")) ) {
					total = rs.getInt("COUNT");
				}
				if( "V".equalsIgnoreCase(rs.getString("TYPE")) ) {
					virtual = rs.getInt("COUNT");
				}
			}
			rs.close();
			rs = null;
			nps.close();
			nps = null;
		} catch( Exception  ex ) {
			logger.warn("Unable to get server virtualization chart data", ex);
		}
		chart.getMetric().add(new DashboardChartData("Physical", total-virtual, "", null, 1));
		chart.getMetric().add(new DashboardChartData("Virtual", virtual, "", null, 2));
		return chart;
	}	

	public DashboardChart getServerDiscoveryChartData() {
		DashboardChart chart = new DashboardChart("dashboard.server.discovery", "Server Discovery", "pie", 3, new ArrayList<String>(), new ArrayList<DashboardChartData>(), null);
		String sql = "SELECT XRP_SOURCE, COUNT(*) AS COUNT FROM AR_XRP_SERVER GROUP BY XRP_SOURCE" +
						" UNION " +
						" SELECT 'EXCEPTION' AS XRP_SOURCE, COUNT(*) AS COUNT FROM AR_DISCOVERY_EXCEPTIONS WHERE HOSTNAME IN (SELECT LOGICAL_NAME FROM AR_XRP_SERVER) ";

		int both = 0;
		int sm = 0;
		int ud = 0;
		int exception = 0;
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = nps.executeQuery();
			while( rs.next() ) {
				if( "BOTH".equalsIgnoreCase(rs.getString("XRP_SOURCE")) ) {
					both = rs.getInt("COUNT");
				}
				if( "UD".equalsIgnoreCase(rs.getString("XRP_SOURCE")) ) {
					ud = rs.getInt("COUNT");
				}
				if( "SM".equalsIgnoreCase(rs.getString("XRP_SOURCE")) ) {
					sm = rs.getInt("COUNT");
				}				
				if( "EXCEPTION".equalsIgnoreCase(rs.getString("XRP_SOURCE")) ) {
					exception = rs.getInt("COUNT");
				}
			}
			rs.close();
			rs = null;
			nps.close();
			nps = null;
		} catch( Exception  ex ) {
			logger.warn("Unable to get server discovery chart data", ex);
		}
		chart.getMetric().add(new DashboardChartData("Discovered", both, "", null, 1));
		chart.getMetric().add(new DashboardChartData("Not Discovered", sm-exception, "", null, 2));
		chart.getMetric().add(new DashboardChartData("Exception", exception, "", null, 3));
		chart.getMetric().add(new DashboardChartData("New Discovery", ud, "", null, 4));
		
		return chart;
	}	
	
	public DashboardChart getServerOSChartData() {
		DashboardChart chart = new DashboardChart("dashboard.server.os", "Server OS", "hBar", 4, new ArrayList<String>(), null, new ArrayList<DashboardChartData>());
		String sql = "SELECT OPERATING_SYSTEM, COUNT(*) AS COUNT FROM AR_XRP_SERVER GROUP BY OPERATING_SYSTEM";

		List<Integer> counts = new ArrayList<>();
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = nps.executeQuery();
			while( rs.next() ) {
				String osName = rs.getString("OPERATING_SYSTEM");
				if( osName == null ) {
					osName = "UNKNOWN";
				}
				chart.getCategory().add(osName);
				int count = rs.getInt("COUNT");
				counts.add(new Integer(count));
			}
			rs.close();
			rs = null;
			nps.close();
			nps = null;
		} catch( Exception  ex ) {
			logger.warn("Unable to get server os chart data", ex);
		}
		
		Integer[] counts1 = new Integer[counts.size()];
		counts1 = counts.toArray(counts1);
		
		chart.getBusinessList().add(new DashboardChartData("serverOS", 0, "", counts1, 1));
		return chart;
	}	
	
	public DashboardChart getServerEnvironmentChartData() {
		DashboardChart chart = new DashboardChart("dashboard.server.environment", "Server Environment", "hBar", 4, new ArrayList<String>(), null, new ArrayList<DashboardChartData>());
		String sql = "SELECT NVL(UPPER(CG_ENVIRONMENT),'UNKNOWN') AS ENV, COUNT(*) AS COUNT FROM AR_XRP_SERVER GROUP BY NVL(UPPER(CG_ENVIRONMENT),'UNKNOWN') ORDER BY COUNT DESC";

		List<Integer> counts = new ArrayList<>();
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = nps.executeQuery();
			while( rs.next() ) {
				String osName = rs.getString("ENV");
				if( osName == null ) {
					osName = "UNKNOWN";
				}
				chart.getCategory().add(osName);
				int count = rs.getInt("COUNT");
				counts.add(new Integer(count));
			}
			rs.close();
			rs = null;
			nps.close();
			nps = null;
		} catch( Exception  ex ) {
			logger.warn("Unable to get server environment chart data", ex);
		}
		
		Integer[] counts1 = new Integer[counts.size()];
		counts1 = counts.toArray(counts1);
		
		chart.getBusinessList().add(new DashboardChartData("ServerEnv", 0, "", counts1, 1));
		return chart;
	}		

	public DashboardChart getDatabaseManufacturerChartData() {
		DashboardChart chart = new DashboardChart("dashboard.database.manufacturer", "Database Manufacturer", "hBar", 4, new ArrayList<String>(), null, new ArrayList<DashboardChartData>());
		
		String sql = "SELECT NVL(UPPER(SUBTYPE),'UNKNOWN') AS ENV, COUNT(*) AS COUNT FROM AR_CMDB_DATABASE GROUP BY NVL(UPPER(SUBTYPE),'UNKNOWN') ORDER BY COUNT DESC";

		List<Integer> counts = new ArrayList<>();
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = nps.executeQuery();
			while( rs.next() ) {
				String osName = rs.getString("ENV");
				if( osName == null ) {
					osName = "UNKNOWN";
				}
				chart.getCategory().add(osName);
				int count = rs.getInt("COUNT");
				counts.add(new Integer(count));
			}
			rs.close();
			rs = null;
			nps.close();
			nps = null;
		} catch( Exception  ex ) {
			logger.warn("Unable to get database manufacturer chart data", ex);
		}
		
		Integer[] counts1 = new Integer[counts.size()];
		counts1 = counts.toArray(counts1);
		
		chart.getBusinessList().add(new DashboardChartData("DatabaseManufacturer", 0, "", counts1, 1));
		return chart;
	}
	
	public DashboardChart getDatabaseEnvironmentChartData() {
		DashboardChart chart = new DashboardChart("dashboard.database.environment", "Database Environment", "hBar", 4, new ArrayList<String>(), null, new ArrayList<DashboardChartData>());
		String sql = "SELECT NVL(UPPER(CG_ENVIRONMENT),'UNKNOWN') AS ENV, COUNT(*) AS COUNT FROM AR_CMDB_DATABASE GROUP BY NVL(UPPER(CG_ENVIRONMENT),'UNKNOWN') ORDER BY COUNT DESC";

		List<Integer> counts = new ArrayList<>();
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = nps.executeQuery();
			while( rs.next() ) {
				String osName = rs.getString("ENV");
				if( osName == null ) {
					osName = "UNKNOWN";
				}
				chart.getCategory().add(osName);
				int count = rs.getInt("COUNT");
				counts.add(new Integer(count));
			}
			rs.close();
			rs = null;
			nps.close();
			nps = null;
		} catch( Exception  ex ) {
			logger.warn("Unable to get database environment chart data", ex);
		}
		
		Integer[] counts1 = new Integer[counts.size()];
		counts1 = counts.toArray(counts1);

		
		
		chart.getBusinessList().add(new DashboardChartData("DatabaseEnv", 0, "", counts1, 1));
		return chart;
	}
	
	
}
