package config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.model.Config;
import util.FileUtil;


public class GlobalConfig {
	final static Logger logger = Logger.getLogger(GlobalConfig.class);
	
	private static GlobalConfig thisInstance;
	private Config config;
	
	private GlobalConfig() {
	}
	
	public static GlobalConfig getInstance() {
		if( thisInstance == null ) {
			thisInstance = new GlobalConfig();
		}
		return thisInstance;
	}
	
	public Config getConfig() {
		if( config == null ) {
			config = readConfig();
		}
		return this.config;
	}

	public void saveConfig( Config config ) {
		String configPath = getClass().getResource("/config.json").getFile().toString();
		if(logger.isDebugEnabled()){
		    logger.debug("Saving configuration file: " + configPath );
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
			File file = new File(configPath);
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(jsonInString);
			fileWriter.flush();
			fileWriter.close();
		} catch( IOException e) {
			logger.error("Error saving configuration file: " + e.getLocalizedMessage(), e);
			e.printStackTrace();
		}
	}		
	
	public Config readConfig() {
		if(logger.isDebugEnabled()){
		    //logger.debug("Loading configuration file: " + getClass().getResource("/config.json").getFile().toString());
			logger.debug("Loading configuration file: " + getClass().getResource("config.json").getFile().toString());
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			//Config config = mapper.readValue(getClass().getResourceAsStream("/config.json"), Config.class);
			//Config config = mapper.readValue(getClass().getResourceAsStream("config.json"), Config.class);

			String configFileData = FileUtil.getFileAsString("config.json");
			Config config = mapper.readValue(configFileData, Config.class);
			
			return config;
		} catch( IOException e) {
			logger.error("Error loacong configuration file: " + e.getLocalizedMessage(), e);
			e.printStackTrace();
		}
		return null;
	}	
	
	
}
