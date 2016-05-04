package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import config.GlobalConfig;
import config.model.Config;

/**
 * @author ramesh.jaladanki
 * this class gets database connection
 */
public class DBConnect {
	
	/**
	 * @return conn
	 * @throws Exception 
	 * 
	 * 
	 */
	public static Connection getConnection( String connectorName ) throws Exception {
		Connection conn = null;
		
		Config config = GlobalConfig.getInstance().readConfig();

		try {
			String userName = config.getDatabaseConfig().get(connectorName).getUserName();
			String passWord = config.getDatabaseConfig().get(connectorName).getPasswordDecrypted();
			String driverName = "";
			if( config.getDatabaseConfig().get(connectorName).getDbType().equalsIgnoreCase("MSSQL") ) {
				driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			} else if ( config.getDatabaseConfig().get(connectorName).getDbType().equalsIgnoreCase("ORACLE") ) { 
				driverName = "oracle.jdbc.OracleDriver";
			}
			String url = config.getDatabaseConfig().get(connectorName).getUrl();
			Class.forName(driverName);
			conn = DriverManager.getConnection(url, userName, passWord);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
			throw new Exception (sqlex);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception (e);
		}
		
		return conn;
	}
	
	/*
	public static Connection getSQLServerConnection() throws Exception{
		
		Connection mcon = null;
		
		try
		{
			String userName = DBConstants.MS_DEV_DB_USER;
			String passWord = DBConstants.MS_DEV_DB_PASSWORD;
			String driverName = DBConstants.MS_DB_DRIVER;
			String url = DBConstants.MS_DEV_DB_URL;
			Class.forName(driverName);
			mcon = DriverManager.getConnection(url, userName, passWord);

		}catch (SQLException sqlex) {
			sqlex.printStackTrace();
			throw new Exception (sqlex);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception (e);
		}
		
		return mcon;
		
	}
	
	
	public static Connection getSQLServerConnectionSMDB() throws Exception{
		
		Connection mcon = null;
		
		try
		{
			String userName = DBConstants.SMDB_MS_DEV_DB_USER;
			String passWord = DBConstants.SMDB_MS_DEV_DB_PASSWORD;
			String driverName = DBConstants.MS_DB_DRIVER;
			String url = DBConstants.SMDB_MS_DEV_DB_URL;
			Class.forName(driverName);
			mcon = DriverManager.getConnection(url, userName, passWord);

		}catch (SQLException sqlex) {
			sqlex.printStackTrace();
			throw new Exception (sqlex);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception (e);
		}
		
		return mcon;
		
	}
	
	*/
	
}