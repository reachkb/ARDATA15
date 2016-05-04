package config.model;

import java.util.HashMap;
import java.util.Map;

public class Config {
	private Map<String, DatabaseConfig> databaseConfig = new HashMap<>();

	public Map<String, DatabaseConfig> getDatabaseConfig() {
		return databaseConfig;
	}

	public void setDatabaseConfig(Map<String, DatabaseConfig> databaseConfig) {
		this.databaseConfig = databaseConfig;
	}

	@Override
	public String toString() {
		return "Config [databaseConfig=" + databaseConfig + "]";
	}

	

}
