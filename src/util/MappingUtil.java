package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.FieldMapping;
import model.TableMapping;

public class MappingUtil {
	
	public static void createMappingTDiscovery() {
		//SELECT 'mapping.add( new FieldMapping("' || column_name || '", "' || '", "direct", "String"));' FROM   all_tab_cols WHERE  table_name = 'AR_DISCOVERY' order by column_id
		//SELECT '{ "targetName" : "' || column_name || '", "sourceName" : "' || column_name || '", "method" : "direct", "type" : "String", "defaultValue" : null },' FROM   all_tab_cols WHERE  table_name = 'AR_DISCOVERY' order by column_id
		/*
SELECT '{ "targetName" : "' || column_name || '", "sourceName" : "' || column_name || '", "method" : "direct", "type" : "String", "defaultValue" : null },' FROM   all_tab_cols WHERE  table_name = 'AR_CMDB_SERVER' order by column_id
SELECT column_name || ',' FROM   all_tab_cols WHERE  table_name = 'AR_CMDB_SERVER' order by column_id
SELECT ':' || column_name || ',' FROM   all_tab_cols WHERE  table_name = 'AR_CMDB_SERVER' order by column_id
		 */
		
		
		List<FieldMapping> mapping = new ArrayList<>();
		mapping.add( new FieldMapping("HOST_NAME", "", "direct", "String"));
		mapping.add( new FieldMapping("NODE_KEY", "", "direct", "String"));
		mapping.add( new FieldMapping("HPE_BUSINESS", "", "direct", "String"));
		mapping.add( new FieldMapping("BUSINESS", "", "direct", "String"));
		mapping.add( new FieldMapping("SUB_BUSINESS", "", "direct", "String"));
		mapping.add( new FieldMapping("A_DOMAIN_NAME", "", "direct", "String"));
		mapping.add( new FieldMapping("OS_NAME", "", "direct", "String"));
		mapping.add( new FieldMapping("OSINSTALLTYPE", "", "direct", "String"));
		mapping.add( new FieldMapping("OSRELEASE", "", "direct", "String"));
		mapping.add( new FieldMapping("OSDESC", "", "direct", "String"));
		mapping.add( new FieldMapping("OS_VERSION", "", "direct", "String"));
		mapping.add( new FieldMapping("OSARCHITECTURE", "", "direct", "String"));
		mapping.add( new FieldMapping("OSVENDOR", "", "direct", "String"));
		mapping.add( new FieldMapping("OSFAMILY", "", "direct", "String"));
		mapping.add( new FieldMapping("VIRTUAL", "", "direct", "String"));
		mapping.add( new FieldMapping("VENDOR", "", "direct", "String"));
		mapping.add( new FieldMapping("DIS_MODEL", "", "direct", "String"));
		mapping.add( new FieldMapping("NODEVENDOR", "", "direct", "String"));
		mapping.add( new FieldMapping("NODEMODEL", "", "direct", "String"));
		mapping.add( new FieldMapping("CPU_NAME", "", "direct", "String"));
		mapping.add( new FieldMapping("SERIALNUMBER", "", "direct", "String"));
		mapping.add( new FieldMapping("MEMORY_SIZE", "", "direct", "String"));
		mapping.add( new FieldMapping("CPU_COUNT", "", "direct", "String"));
		mapping.add( new FieldMapping("CPU_SPEED", "", "direct", "String"));
		mapping.add( new FieldMapping("TOTAL_CPU_CORE", "", "direct", "String"));
		mapping.add( new FieldMapping("DISC_COUNT", "", "direct", "String"));
		mapping.add( new FieldMapping("DISKSIZE", "", "direct", "String"));
		mapping.add( new FieldMapping("INTERFACE_COUNT", "", "direct", "String"));
		mapping.add( new FieldMapping("LOCATION", "", "direct", "String"));
		mapping.add( new FieldMapping("GLOBAL_ID", "", "direct", "String"));
		mapping.add( new FieldMapping("CREATE_TIME", "", "direct", "String"));
		mapping.add( new FieldMapping("LASTACCESSTIME", "", "direct", "String"));
		mapping.add( new FieldMapping("MODIFIED_TIME", "", "direct", "String"));
		mapping.add( new FieldMapping("POLE", "", "direct", "String"));
		mapping.add( new FieldMapping("ACTIVE", "", "direct", "String"));
		mapping.add( new FieldMapping("CREATION_DATE", "", "direct", "String"));
		mapping.add( new FieldMapping("LAST_UPDATE_DATE", "", "direct", "String"));
		mapping.add( new FieldMapping("SNOW_OS_ID", "", "direct", "String"));
		mapping.add( new FieldMapping("SNOW_OS_NAME", "", "direct", "String"));
		mapping.add( new FieldMapping("SNOW_MODEL_ID", "", "direct", "String"));
		mapping.add( new FieldMapping("SNOW_MODEL_NAME", "", "direct", "String"));
		mapping.add( new FieldMapping("BIOS_UUID", "", "direct", "String"));
		mapping.add( new FieldMapping("BIOS_VERSION", "", "direct", "String"));
		mapping.add( new FieldMapping("BIOS_DATE", "", "direct", "String"));
		mapping.add( new FieldMapping("DEFAULT_GATEWAY_IP_ADDRESS", "", "direct", "String"));
		mapping.add( new FieldMapping("PRIMARY_DNS_NAME", "", "direct", "String"));
		
		String mappingPath = "C:\\Users\\rehan\\Dropbox\\Work\\LSC\\Cigna\\Delivery\\XRP\\Queries\\mapping.json";
		
		saveTableMapping( new TableMapping("AR_DISCOVERY", mapping), mappingPath );
	}
	
	public static void createMapping() {
		String mappingPath = "C:\\Users\\rehan\\Dropbox\\Work\\LSC\\Cigna\\Delivery\\XRP\\Queries\\mapping.json";

		
		List<FieldMapping> mapping = new ArrayList<>();
		mapping.add( new FieldMapping("UCMDB_ID", "GLOBAL_ID", "direct", "RAW"));
		mapping.add( new FieldMapping("TYPE", "GLOBAL_ID", "constant", "String"));
		mapping.add( new FieldMapping("SUBTYPE", "GLOBAL_ID", "direct", "String"));
		mapping.add( new FieldMapping("IPADDRESS", "GLOBAL_ID", "direct", "String"));
		mapping.add( new FieldMapping("NETWORK_NAME", "HOST_NAME", "direct", "String"));
		mapping.add( new FieldMapping("OPERATING_SYSTEM", "OS_NAME", "direct", "String"));
		mapping.add( new FieldMapping("OWNER", "GLOBAL_ID", "direct", "String"));
		mapping.add( new FieldMapping("SUPPORT_CONTACTS", "GLOBAL_ID", "direct", "String"));
		mapping.add( new FieldMapping("LOCATION", "GLOBAL_ID", "direct", "String"));
		mapping.add( new FieldMapping("ENVIRONMENT", "GLOBAL_ID", "direct", "String"));
		mapping.add( new FieldMapping("SERIAL_NO_", "SERIALNUMBER", "direct", "String"));
		mapping.add( new FieldMapping("TCP_IP_NAME", "GLOBAL_ID", "direct", "String"));
		mapping.add( new FieldMapping("LOCATION_CODE", "GLOBAL_ID", "direct", "String"));
		mapping.add( new FieldMapping("ESTATUS", "GLOBAL_ID", "direct", "String"));
		mapping.add( new FieldMapping("ISTATUS", "GLOBAL_ID", "direct", "String", "Installed"));
		mapping.add( new FieldMapping("WMI_LASTSCAN", "GLOBAL_ID", "direct", "date"));
		
		
		saveTableMapping( new TableMapping("T_CMDB_SERVER_AR", mapping), mappingPath );
	}
	
	public static void saveTableMapping( TableMapping tableMapping, String filePath ) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString( tableMapping );
			File file = new File(filePath);
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(jsonInString);
			fileWriter.flush();
			fileWriter.close();
		} catch( IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static TableMapping getTableMapping( String mappingFileName ){
		ObjectMapper mapper = new ObjectMapper();
		try {
			TableMapping tableMapping = mapper.readValue(MappingUtil.class.getClassLoader().getResourceAsStream(mappingFileName), TableMapping.class);
			
			return tableMapping;
		} catch( IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
