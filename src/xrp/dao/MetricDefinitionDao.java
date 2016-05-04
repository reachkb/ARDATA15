package xrp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import db.DBConnect;
import xrp.model.MetricDefinition;
import xrp.model.MetricDefinitionGroup;

public class MetricDefinitionDao {
	
	private static Logger LOGGER = Logger.getLogger(MetricDefinitionDao.class);
	
	public List<MetricDefinitionGroup> getMetricDefinitionGroups() {
		List<MetricDefinitionGroup> list = new ArrayList<>();
		
		try {
			Connection connection = DBConnect.getConnection("xrp");
			
			String sqlStatement = "SELECT METRIC_NAME, METRIC_TITLE, METRIC_ORDER, GROUP_NAME, GROUP_ORDER, DESCRIPTION, METRIC_TYPE, ACTIVE from AR_METRIC_DEF WHERE ACTIVE='Y' order by GROUP_ORDER, METRIC_ORDER";
			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = preparedStatement.executeQuery();
			Map<String, MetricDefinitionGroup> groupMap = new HashMap<>();
			while( resultSet.next() ) {
				MetricDefinition md = new MetricDefinition( resultSet.getString("METRIC_NAME"), resultSet.getString("METRIC_TITLE"), resultSet.getInt("METRIC_ORDER"), resultSet.getString("DESCRIPTION"), resultSet.getString("METRIC_TYPE"), resultSet.getString("ACTIVE"));
				String currertGroup = resultSet.getString("GROUP_NAME");
				if( !groupMap.containsKey(currertGroup) ) {
					groupMap.put( currertGroup, new MetricDefinitionGroup(currertGroup, resultSet.getInt("GROUP_ORDER"), new ArrayList<MetricDefinition>()));
				}
				groupMap.get(currertGroup).getMetricDefinition().add(md);
			}
			
			list.addAll(groupMap.values());
		} catch( Exception ex ) {
			LOGGER.error( String.format("Unable to get metric definition" ), ex );
		}		
		return list;
	}
	
	public List<MetricDefinition> getMetricDefinitions() {
		List<MetricDefinition> list = new ArrayList<>();
		
		try {
			Connection connection = DBConnect.getConnection("xrp");
			
			String sqlStatement = "SELECT METRIC_NAME, METRIC_TITLE, METRIC_ORDER, GROUP_NAME, GROUP_ORDER, DESCRIPTION, METRIC_TYPE, ACTIVE from AR_METRIC_DEF WHERE ACTIVE='Y' order by GROUP_ORDER, METRIC_ORDER";
			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = preparedStatement.executeQuery();
			while( resultSet.next() ) {
				MetricDefinition md = new MetricDefinition( resultSet.getString("METRIC_NAME"), resultSet.getString("METRIC_TITLE"), resultSet.getInt("METRIC_ORDER"), resultSet.getString("DESCRIPTION"), resultSet.getString("METRIC_TYPE"), resultSet.getString("ACTIVE"));
				list.add(md);
			}
		} catch( Exception ex ) {
			LOGGER.error( String.format("Unable to get metric definition" ), ex );
		}		
		return list;
	}	

}
