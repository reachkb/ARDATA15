package connector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DBConnect;
import db.NamedParameterStatement;
import model.FieldMapping;
import model.TableMapping;
import util.FileUtil;
import util.MappingUtil;

public abstract class DataConnector {
	
	protected final int  BATCHSIZE = 1000;
	
	protected List<Map<String, Object>> getData( String connectorName, String sqlFileName ) throws Exception {
		Connection connection = DBConnect.getConnection( connectorName );
		Statement statement = connection.createStatement();
		//System.out.println(sqlFileName);
		ResultSet resultSet = statement.executeQuery(FileUtil.getFileAsString(sqlFileName));
		List<Map<String, Object>> servers = resultSetToArrayList( resultSet );
		resultSet.close();
		statement.close();
		connection.close();
		return servers;
	}
	
	protected void saveData( List<Map<String, Object>> data, String connectorName, String entityName ) throws Exception {
		TableMapping tableMapping = MappingUtil.getTableMapping(String.format("%s.%s.mapping.json", connectorName, entityName ));
		
		NamedParameterStatement nps = new NamedParameterStatement(DBConnect.getConnection("xrp"), String.format("TRUNCATE TABLE %s", tableMapping.getTargetTableName()));
		nps.getPreparedStatement().executeUpdate();
		
		int counter = 0;
		String sql = FileUtil.getFileAsString(String.format("%s.%s.insert.sql", connectorName, entityName ));
		NamedParameterStatement namedParameterStatement = new NamedParameterStatement(DBConnect.getConnection("xrp"), sql);
		for( Map<String, Object> row : data ) {
			for( FieldMapping fieldMapping : tableMapping.getFieldMapings()) {
				//System.out.println(">>> " + fieldMapping.getTargetName() );
				namedParameterStatement.setData2(fieldMapping, row);
			}
			namedParameterStatement.addBatch();
			if( ++counter > BATCHSIZE ) {
				counter = 0;
				namedParameterStatement.executeBatch();
			}
		}
		namedParameterStatement.executeBatch();
	}	
	
	
	protected List<Map<String, Object>> resultSetToArrayList(ResultSet resultSet) throws SQLException{
		ResultSetMetaData md = resultSet.getMetaData();
		int columns = md.getColumnCount();
		ArrayList<Map<String,Object>> list = new ArrayList<>();
		while (resultSet.next()){
			HashMap<String, Object> row = new HashMap<>(columns);
		    for(int i=1; i<=columns; ++i){           
		    	row.put(md.getColumnName(i),resultSet.getObject(i));
		    }
		    list.add(row);
		}
		return list;
	}

}
