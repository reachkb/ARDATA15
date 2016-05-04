package xrp.dao;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import db.DBConnect;
import db.NamedParameterStatement;

public class DiscoveryDao {
	
	public int getNonDiscoveredServerTotal() {
		String sql = "SELECT COUNT(*) AS COUNT FROM AR_XRP_SERVER WHERE XRP_SOURCE = 'UD'";
		int count = 0;
		try {
			NamedParameterStatement namedParameterStatement = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = namedParameterStatement.executeQuery();
			while( rs.next() ) {
				count = rs.getInt("COUNT");
			}
		} catch( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return count;
	}
	
	public int getNonDiscoveredServerRemediationTotal() {
		String sql = "SELECT COUNT(*) AS COUNT FROM AR_XRP_SERVER WHERE XRP_SOURCE = 'UD' AND LOGICAL_NAME NOT IN (SELECT HOSTNAME FROM AR_DISCOVERY_EXCEPTIONS WHERE ACTIVE='Y')";
		int count = 0;
		try {
			NamedParameterStatement namedParameterStatement = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = namedParameterStatement.executeQuery();
			while( rs.next() ) {
				count = rs.getInt("COUNT");
			}
		} catch( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return count;
	}
	
	public Map<String, Map<String, Integer>> getDiscoveryReconciliationData( String type ) {
		String sql = 	"SELECT XRP_SOURCE, LOCATION_CODE AS CODE, COUNT(*) AS COUNT FROM AR_XRP_SERVER GROUP BY LOCATION_CODE, XRP_SOURCE " + 
						"UNION " +
						"SELECT 'EXCEPTION' AS XRP_SOURCE, LOCATION_CODE AS CODE, COUNT(*) AS COUNT FROM AR_XRP_SERVER S INNER JOIN AR_DISCOVERY_EXCEPTIONS E ON E.HOSTNAME = S.LOGICAL_NAME AND E.ACTIVE='Y'GROUP BY LOCATION_CODE, XRP_SOURCE " + 
						"ORDER BY XRP_SOURCE, COUNT DESC ";
		if( "business".equalsIgnoreCase(type)) {
			sql = 	"SELECT XRP_SOURCE, 'Cigna - All' AS CODE, COUNT(*) AS COUNT FROM AR_XRP_SERVER GROUP BY XRP_SOURCE " + 
					"UNION " +
					"SELECT 'EXCEPTION' AS XRP_SOURCE, 'Cigna - All' AS CODE, COUNT(*) AS COUNT FROM AR_XRP_SERVER S INNER JOIN AR_DISCOVERY_EXCEPTIONS E ON E.HOSTNAME = S.LOGICAL_NAME AND E.ACTIVE='Y'GROUP BY XRP_SOURCE " + 
					"ORDER BY XRP_SOURCE, COUNT DESC ";
			
		}
		System.out.println(">>>" + sql );
		int count = 0;
		Map<String, Map<String, Integer>> map = new HashMap<>();
		try {
			NamedParameterStatement namedParameterStatement = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = namedParameterStatement.executeQuery();
			while( rs.next() ) {
				String location = rs.getString("CODE");
				String xrpSource = rs.getString("XRP_SOURCE");
				count = rs.getInt("COUNT");
				
				if( "WDC".equalsIgnoreCase(location)) {
					System.out.println( String.format("Location: %s, source: %s, count: %s", location, xrpSource, count ));
				}
				
				Map<String, Integer> locationMap = map.get(location);
				if( locationMap == null ) {
					locationMap = new HashMap<>();
					map.put(location, locationMap);
				}
				
				if( xrpSource == null ) {
					xrpSource = "unknown";
				} else {
					xrpSource = xrpSource.toLowerCase();
				}
				locationMap.put(xrpSource, new Integer(count));
			}
		} catch( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return map;	
	}
	
}
