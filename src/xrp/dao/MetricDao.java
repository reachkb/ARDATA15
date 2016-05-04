package xrp.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DBConnect;
import db.NamedParameterStatement;
import util.DateUtil;
import xrp.model.Metric;

public class MetricDao {

	public List<String> getMetricDefinitions() {
		List<String> metricDefinitions = new ArrayList<>();
		String sql = "SELECT DISTINCT METRIC_NAME FROM AR_METRIC_DEF";
		try {
			NamedParameterStatement namedParameterStatement = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = namedParameterStatement.executeQuery();
			while( rs.next() ) {
				metricDefinitions.add(rs.getString("METRIC_NAME"));
			}
		} catch( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return metricDefinitions;		
	}
	
	public void saveMetrices( List<Metric> metrices ) {
		String sql = "INSERT INTO AR_METRICES( METRIC_DATE, T_BUSINESS, T_SUB_BUSINESS, METRIC_NAME, METRIC_VALUE, METRIC_COUNT, METRIC_TOTAL ) " +
				"VALUES( :METRIC_DATE, :T_BUSINESS, :T_SUB_BUSINESS, :METRIC_NAME, :METRIC_VALUE, :METRIC_COUNT, :METRIC_TOTAL )";
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			for( Metric metric : metrices ) {
				nps.setDate("METRIC_DATE", metric.getDate());
				nps.setString("T_BUSINESS", "All");
				nps.setString("T_SUB_BUSINESS", "Cigna - All");
				nps.setString("METRIC_NAME", metric.getName());
				nps.setString("METRIC_VALUE",  Integer.toString(metric.getPercentage()));
				nps.setInteger("METRIC_COUNT", metric.getCount());
				nps.setInteger("METRIC_TOTAL", metric.getTotal());
				nps.addBatch();
			}
			nps.executeBatch();
			
		} catch( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteMetrices() {
		deleteMetrices( new Date());
	}
	
	public void deleteMetrices( Date metricDate ) {
		String sql = "DELETE FROM AR_METRICES WHERE METRIC_DATE=:METRIC_DATE";
		try {
			NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			nps.setDate("METRIC_DATE", DateUtil.removeTime(metricDate));
			nps.getPreparedStatement().executeUpdate();
		} catch( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public Map<String, Metric> getCurrentMetrices() {
		Map<String, Metric> map = new HashMap<>();
		String sql = "SELECT m.* FROM  AR_METRICES m INNER JOIN (  SELECT METRIC_NAME METRIC_NAME, MAX(METRIC_DATE) AS METRIC_DATE from AR_METRICES GROUP BY METRIC_NAME )  m1 ON m.METRIC_NAME = m1.METRIC_NAME AND m.METRIC_DATE = m1.METRIC_DATE";
		try {
			NamedParameterStatement namedParameterStatement = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = namedParameterStatement.executeQuery();
			while( rs.next() ) {
				map.put(rs.getString("METRIC_NAME"), new Metric( rs.getDate("METRIC_DATE"), rs.getString("METRIC_NAME"), rs.getInt("METRIC_COUNT"), rs.getInt("METRIC_TOTAL")));
			}
		} catch( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}	

	public Map<String, Metric> getPreviousMetrices() {
		Map<String, Metric> map = new HashMap<>();
		String sql = 	"SELECT m.* FROM AR_METRICES m INNER JOIN ( " +
						"SELECT m1.METRIC_NAME, MAX(m1.METRIC_DATE) AS METRIC_DATE FROM AR_METRICES m1" +
						"  WHERE m1.METRIC_DATE NOT IN  ( SELECT MAX(m2.METRIC_DATE) FROM AR_METRICES m2 WHERE METRIC_NAME=m1.METRIC_NAME) " +
						"GROUP BY m1.METRIC_NAME  ) mm " +
						"  ON mm.METRIC_NAME = m.METRIC_NAME and mm.METRIC_DATE = m.METRIC_DATE";
		try {
			NamedParameterStatement namedParameterStatement = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = namedParameterStatement.executeQuery();
			while( rs.next() ) {
				map.put(rs.getString("METRIC_NAME"), new Metric( rs.getDate("METRIC_DATE"), rs.getString("METRIC_NAME"), rs.getInt("METRIC_COUNT"), rs.getInt("METRIC_TOTAL")));
			}
		} catch( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}		
	
	public Map<String, List<Metric>> getMetrices( int numberofDays ) {
		Map<String, List<Metric>> map = new HashMap<>();
		String sql = String.format("SELECT METRIC_NAME, METRIC_DATE, METRIC_VALUE, METRIC_COUNT, METRIC_TOTAL FROM AR_METRICES WHERE METRIC_DATE > TRUNC(SYSDATE - %s) ORDER BY METRIC_NAME, METRIC_DATE DESC", numberofDays );
		try {
			NamedParameterStatement namedParameterStatement = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
			ResultSet rs = namedParameterStatement.executeQuery();
			// SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			while( rs.next() ) {
				String metricName = rs.getString("METRIC_NAME");
				List<Metric> metricList = map.get(metricName);
				if( metricList == null ) {
					metricList = new ArrayList<>();
					map.put(metricName, metricList);
				}
				if( metricList.size() < 5 ) {
					// map.put(rs.getString("METRIC_NAME"), new Metric( rs.getDate("METRIC_DATE"), rs.getString("METRIC_NAME"), rs.getInt("METRIC_COUNT"), rs.getInt("METRIC_TOTAL")));
					Timestamp timestamp = rs.getTimestamp("METRIC_DATE");
					Date metricDate = new Date();
					if( timestamp != null )
						metricDate = new java.util.Date(timestamp.getTime());
					Metric metric = new Metric(metricDate, metricName, rs.getInt("METRIC_COUNT"), rs.getInt("METRIC_TOTAL"));
					metricList.add( metric );
					map.put(metricName, metricList);
				}
			}
		} catch( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}			
	
}
