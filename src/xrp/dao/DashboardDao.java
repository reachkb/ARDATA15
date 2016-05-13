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
	
	/*
	 * @ Added by Amrendra Mandal 
	 * for Server Dashboard
	 * on 5/11/2016 
	 * 
	 * */
	public DashboardChart getServerEnvironmentData() {
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
	/*
	 * End
	 * */
	
	
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
		String sql = "SELECT NVL(UPPER(SUBTYPE),'UNKNOWN') AS SUBTYPE, COUNT(*) AS COUNT FROM AR_XRP_SERVER GROUP BY NVL(UPPER(SUBTYPE),'UNKNOWN') ORDER BY COUNT DESC";

		List<Integer> counts = new ArrayList<>();
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = nps.executeQuery();
			while( rs.next() ) {
				String osName = rs.getString("SUBTYPE");
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
/*
 * Added By Amrendra Mandal
 * For CMDB Server Dashboard
 * Charts
 * */	
	public DashboardChart getCMDBServerEnvironmentChart() {
		DashboardChart chart = new DashboardChart("cmdb.server.environment", "Environment", "pie", 3, new ArrayList<String>(), new ArrayList<DashboardChartData>(), null);
		String sql = ""
					+ " SELECT business, Total,  Dev, Test,   "
					+ " PVS,    PreProd,  DR,   Training , Prod ,  "
					+ " Other FROM   (SELECT 'Total' AS business,SUM(CASE  WHEN lower(t1.CG_ENVIRONMENT) = 'development'  THEN 1  ELSE 1END) AS Total, "
					+ " SUM(CASE  WHEN lower(t1.CG_ENVIRONMENT) = 'production'  THEN 1  ELSE 0END) AS prod,   "
					+ " SUM(CASE  WHEN lower(t1.CG_ENVIRONMENT) = 'development'  THEN 1  ELSE 0END) AS Dev,    "
					+ " SUM(CASE  WHEN lower(t1.CG_ENVIRONMENT) IN ( 'destructive', 'test', 'system testing', 'integration testing' )  THEN 1  ELSE 0END) AS Test,"
					+ " SUM(CASE  WHEN lower(t1.CG_ENVIRONMENT) IN ( 'performance volume stress (pvs)' )  THEN 1  ELSE 0END) AS pvs,"
					+ " SUM(CASE  WHEN lower(t1.CG_ENVIRONMENT) in ('pre-production', 'prodfix', 'release management')  THEN 1  ELSE 0END) AS preprod,   "
					+ " SUM(CASE  WHEN lower(t1.CG_ENVIRONMENT) = 'disaster recovery'  THEN 1  ELSE 0END) AS DR,   "
					+ " SUM(CASE  WHEN lower(t1.CG_ENVIRONMENT) = 'training'  THEN 1  ELSE 0END) AS training,    "
					+ " SUM(CASE  WHEN lower(t1.CG_ENVIRONMENT) in( 'unknown', null)  THEN 1  ELSE 0END) AS Other   FROM AR_XRP_SERVER t1   )  "; 

		int total = 0;
		int dev = 0;
		int test = 0;
		int PVS = 0;
		int preProd = 0;
		int DR = 0;
		int training = 0;
		int prod = 0;
		int others = 0;
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = nps.executeQuery();
			while( rs.next() ) {
				total = Integer.parseInt((null != rs.getString("Total") && !"".equals(rs.getString("Total")))? rs.getString("Total") :"0" );
				dev = Integer.parseInt((null != rs.getString("Dev") && !"".equals(rs.getString("Dev")))? rs.getString("Dev") :"0" );
				test = Integer.parseInt((null != rs.getString("Test") && !"".equals(rs.getString("Test")))? rs.getString("Test") :"0" );
				PVS = Integer.parseInt((null != rs.getString("PVS") && !"".equals(rs.getString("PVS")))? rs.getString("PVS") :"0" );
				preProd = Integer.parseInt((null != rs.getString("PreProd") && !"".equals(rs.getString("PreProd")))? rs.getString("PreProd") :"0" );
				DR = Integer.parseInt((null != rs.getString("DR") && !"".equals(rs.getString("DR")))? rs.getString("DR") :"0" );
				training =Integer.parseInt((null != rs.getString("Training") && !"".equals(rs.getString("Training")))? rs.getString("Training") :"0" );
				prod = Integer.parseInt((null != rs.getString("Prod") && !"".equals(rs.getString("Prod")))? rs.getString("Prod") :"0" );
				others =Integer.parseInt((null != rs.getString("Other") && !"".equals(rs.getString("Other")))? rs.getString("Other") :"0" );
			}
			rs.close();
			rs = null;
			nps.close();
			nps = null;
		} catch( Exception  ex ) {
			logger.warn("Unable to get cmdb server enviornment chart data", ex);
		}
		//chart.getMetric().add(new DashboardChartData("Total", total, "", null, 1));
		chart.getMetric().add(new DashboardChartData("Dev", dev, "", null, 1));
		chart.getMetric().add(new DashboardChartData("Test", test, "", null, 2));
		chart.getMetric().add(new DashboardChartData("PVS", PVS, "", null, 3));
		chart.getMetric().add(new DashboardChartData("Pre-Prod", preProd, "", null, 4));
		chart.getMetric().add(new DashboardChartData("DR", DR, "", null, 5));
		chart.getMetric().add(new DashboardChartData("Training", training, "", null, 6));
		chart.getMetric().add(new DashboardChartData("Prod", prod, "", null, 7));
		chart.getMetric().add(new DashboardChartData("Others", others, "", null, 8));
		
		return chart;
	}
	public DashboardChart getCMDBServerOprSysChartData() {
		DashboardChart chart = new DashboardChart("cmdb.server.operating.system", "Operating System", "pie", 3, new ArrayList<String>(), new ArrayList<DashboardChartData>(), null);
		String sql = ""
					+ " SELECT business,   Total, Win, Linux,zvm,zos, "
					+ " vios,iSeries,Appliance, Solaris,  "
					+ " ESX,AIX,Other FROM   (SELECT 'Total' AS Business ,    "
					+ " SUM(CASE  WHEN lower(t1.subtype) = 'windows'  THEN 1  ELSE 1END) AS Total ,"
					+ " SUM(CASE  WHEN lower(t1.subtype) = 'windows'  THEN 1  ELSE 0END) AS Win ,"
					+ " SUM(CASE  WHEN lower(t1.subtype) = 'linux'  THEN 1  ELSE 0END) AS Linux,    "
					+ " SUM(CASE  WHEN lower(t1.subtype) = 'z/vm'  THEN 1  ELSE 0END) AS zvm, 	"
					+ " SUM(CASE  WHEN lower(t1.subtype) = 'z/os'  THEN 1  ELSE 0END) AS zos,    "
					+ " sum(CASE  WHEN lower(t1.subtype) = 'vios'  THEN 1  ELSE 0END) AS vios,    "
					+ " SUM(CASE  WHEN lower(t1.subtype) = 'iseries'  THEN 1  ELSE 0END) AS iseries,"
					+ " sum(CASE  WHEN lower(t1.subtype) = 'appliance'  THEN 1  ELSE 0END) AS appliance, "
					+ " SUM(CASE  WHEN lower(t1.subtype) = 'solaris'  THEN 1  ELSE 0END) AS solaris, "
					+ "	sum(CASE  WHEN lower(t1.subtype) = 'esx'  THEN 1  ELSE 0END) AS esx,    "
					+ " SUM(CASE  WHEN lower(t1.subtype) = 'aix'  THEN 1  ELSE 0END) AS aix,  "
					+ " SUM(CASE  WHEN lower(t1.subtype) in ( 'unknown', null)  THEN 1  ELSE 0END) AS Other   FROM AR_XRP_SERVER t1   )  ";

		int total = 0;
		int Win = 0;
		int linux = 0;
		int zvm = 0;
		int zos = 0;
		int vios = 0;
		int iSeries = 0;
		int appliance = 0;
		int solaris = 0;
		int ESX = 0;
		int AIX = 0;
		int Other = 0;
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = nps.executeQuery();
			while( rs.next() ) {
				total = Integer.parseInt((null != rs.getString("Total") && !"".equals(rs.getString("Total")))? rs.getString("Total") :"0" );
				Win = Integer.parseInt((null != rs.getString("Win") && !"".equals(rs.getString("Win")))? rs.getString("Win") :"0" );
				linux = Integer.parseInt((null != rs.getString("linux") && !"".equals(rs.getString("linux")))? rs.getString("linux") :"0" );
				zvm = Integer.parseInt((null != rs.getString("zvm") && !"".equals(rs.getString("zvm")))? rs.getString("zvm") :"0" );
				zos = Integer.parseInt((null != rs.getString("zos") && !"".equals(rs.getString("zos")))? rs.getString("zos") :"0" );
				vios = Integer.parseInt((null != rs.getString("vios") && !"".equals(rs.getString("vios")))? rs.getString("vios") :"0" );
				iSeries =Integer.parseInt((null != rs.getString("iSeries") && !"".equals(rs.getString("iSeries")))? rs.getString("iSeries") :"0" );
				appliance = Integer.parseInt((null != rs.getString("appliance") && !"".equals(rs.getString("appliance")))? rs.getString("appliance") :"0" );
				solaris =Integer.parseInt((null != rs.getString("solaris") && !"".equals(rs.getString("solaris")))? rs.getString("solaris") :"0" );
				ESX =Integer.parseInt((null != rs.getString("ESX") && !"".equals(rs.getString("ESX")))? rs.getString("ESX") :"0" );
				AIX =Integer.parseInt((null != rs.getString("AIX") && !"".equals(rs.getString("AIX")))? rs.getString("AIX") :"0" );
				Other =Integer.parseInt((null != rs.getString("Other") && !"".equals(rs.getString("Other")))? rs.getString("Other") :"0" );
			}
			rs.close();
			rs = null;
			nps.close();
			nps = null;
		} catch( Exception  ex ) {
			logger.warn("Unable to get cmdb server Operating System chart data", ex);
		}
		//chart.getMetric().add(new DashboardChartData("Discovered", total, "", null, 1));
		chart.getMetric().add(new DashboardChartData("Win", Win, "", null, 1));
		chart.getMetric().add(new DashboardChartData("Linux", linux, "", null, 2));
		chart.getMetric().add(new DashboardChartData("ZVM", zvm, "", null, 3));
		chart.getMetric().add(new DashboardChartData("ZOS", zos, "", null, 4));
		chart.getMetric().add(new DashboardChartData("VIOS", vios, "", null, 5));
		chart.getMetric().add(new DashboardChartData("iSeries", iSeries, "", null, 6));
		chart.getMetric().add(new DashboardChartData("Appliance", appliance, "", null, 7));
		chart.getMetric().add(new DashboardChartData("Solaris", solaris, "", null, 8));
		chart.getMetric().add(new DashboardChartData("ESX", ESX, "", null, 9));
		chart.getMetric().add(new DashboardChartData("AIX", AIX, "", null, 10));
		chart.getMetric().add(new DashboardChartData("Other", Other, "", null, 11));
		
		return chart;
	}
	public DashboardChart getCMDBServerVirVsPhyChartData() {
		DashboardChart chart = new DashboardChart("cmdb.server.virtualVsPhysical", "Virtual Vs Physical", "pie", 3, new ArrayList<String>(), new ArrayList<DashboardChartData>(), null);
		String sql = "SELECT (SUM(phy)+SUM(vir)) AS Total,   (SUM(vir))"
				+ " AS virtual,   SUM(phy) AS physical "
				+ " FROM   (SELECT NULL phy ,     m.metric_count vir   FROM AR_METRICES m   "
				+ " INNER JOIN     "
				+ " (SELECT METRIC_NAME,       MAX(METRIC_DATE) AS METRIC_DATE     FROM AR_METRICES     GROUP BY METRIC_NAME     ) m1   "
				+ " ON m.METRIC_NAME     = m1.METRIC_NAME   AND m.METRIC_DATE    = m1.METRIC_DATE   "
				+ " WHERE m.metric_name IN ( 'server.virtual' )   UNION   SELECT m.metric_count phy,     NULL vir   "
				+ " FROM AR_METRICES m   INNER JOIN     (SELECT METRIC_NAME,       MAX(METRIC_DATE) AS METRIC_DATE     "
				+ " FROM AR_METRICES     GROUP BY METRIC_NAME     ) m1   ON m.METRIC_NAME     = m1.METRIC_NAME   AND m.METRIC_DATE    = m1.METRIC_DATE   "
				+ " WHERE m.metric_name IN ( 'server.physical' )   )";

		int total = 0;
		int virtual = 0;
		int physical = 0;
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = nps.executeQuery();
			while( rs.next() ) {
				total = Integer.parseInt((null != rs.getString("Total") && !"".equals(rs.getString("Total")))? rs.getString("Total") :"0" );
				virtual = Integer.parseInt((null != rs.getString("virtual") && !"".equals(rs.getString("virtual")))? rs.getString("virtual") :"0" );
				physical = Integer.parseInt((null != rs.getString("physical") && !"".equals(rs.getString("physical")))? rs.getString("physical") :"0" );
			}
			rs.close();
			rs = null;
			nps.close();
			nps = null;
		} catch( Exception  ex ) {
			logger.warn("Unable to get server discovery chart data", ex);
		}
		chart.getMetric().add(new DashboardChartData("Virtual", virtual, "", null, 1));
		chart.getMetric().add(new DashboardChartData("Physical", physical, "", null, 2));
		
		return chart;
	}
/*
 * @ End
 * @ May/12/2014
 * */	
}
