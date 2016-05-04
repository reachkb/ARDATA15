package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import db.DBConnect;

public class DbUtil {
	
	public static String addDateSuffix( String data ) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyymmdd");
		return String.format("%s_%s", data, sdf.format(new Date()));
	}
	
	public static void renameTable( String oldTableName, String newTableName ) throws Exception {
		String sqlStatement = String.format("RENAME TABLE %s TO %s", oldTableName, newTableName);
		executeSql( sqlStatement );
	}
	
	public static void dropTable( String tableName ) throws Exception {
		String sqlStatement = String.format("DROP TABLE %", tableName);
		executeSql( sqlStatement );
	}

	public static boolean isTablePresent( String tableName ) throws Exception {
		String sqlStatement = String.format("SELECT * FROM ALL_TABLES WHERE TABLE_NAME = '%s'", tableName.toUpperCase());
		ResultSet rs = executeSqlResultset( sqlStatement );
		int rowCount = 0;
		while( rs.next()) {
			rowCount++;
		}
		if( rowCount > 0 ) {
			return true;
		} else {
			return false;
		}
	}	
	
	private static void executeSql( String sqlStatement ) throws Exception {
		Connection connection = DBConnect.getConnection("xpr");
		Statement statement = connection.createStatement();
		statement.execute(sqlStatement);
		statement.close();
	}
	
	private static ResultSet executeSqlResultset( String sqlStatement ) throws Exception {
		Connection connection = DBConnect.getConnection("xpr");
		Statement statement = connection.createStatement();
		return statement.executeQuery(sqlStatement);
	}
	
	
	
}
