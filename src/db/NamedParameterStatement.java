package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import model.FieldMapping;
import oracle.sql.RAW;

public class NamedParameterStatement {
	private PreparedStatement preparedStatement;
    private List<String> fields = new ArrayList<String>();
    
    public NamedParameterStatement(Connection connection, String sqlStatement ) throws SQLException {
        int pos;
        while((pos = sqlStatement.indexOf(":")) != -1) {
        	
        	int end = sqlStatement.substring(pos).indexOf(" ");
        	if( end == -1 ) {
        		end = sqlStatement.length();
        	}
        	int endComma = sqlStatement.substring(pos).indexOf(",");
        	if( endComma == -1) {
        		endComma = sqlStatement.substring(pos).length();
        	}
        	if( endComma < end ) {
        		end = endComma;
        	}
        	int endBracket = sqlStatement.substring(pos).indexOf(")");
        	if( endBracket == -1) {
        		endBracket = sqlStatement.substring(pos).length();
        	}
        	if( endBracket < end ) {
        		end = endBracket;
        	}
        	
        	int endNewLine = sqlStatement.substring(pos).indexOf("\r");
        	if( endNewLine == -1) {
        		endNewLine = sqlStatement.substring(pos).length();
        	}
        	if( endNewLine < end ) {
        		end = endNewLine;
        	}         	
        	
            fields.add(sqlStatement.substring(pos+1,pos+end));
            sqlStatement = sqlStatement.substring(0, pos) + "?" + sqlStatement.substring(pos+end);
            
        }   
        //System.out.println(sqlStatement);
        //System.out.println(fields);
        preparedStatement = connection.prepareStatement(sqlStatement);
    }
    
    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public ResultSet executeQuery() throws SQLException {
        return preparedStatement.executeQuery();
    }
    
    public void addBatch() throws SQLException {
    	preparedStatement.addBatch();
    }
    
    public int[] executeBatch() throws SQLException {
    	return preparedStatement.executeBatch();
    }
    
    public void close() throws SQLException {
    	preparedStatement.close();
    }

    //public void setInt(String name, int value) throws SQLException {        
    //	preparedStatement.setInt(getIndex(name), value);
    //}
    
    public void setInteger(String name, Integer value) throws SQLException {        
    	preparedStatement.setInt(getIndex(name), value);
    }    
    
    public void setDouble(String name, Double value) throws SQLException {        
    	preparedStatement.setDouble( getIndex(name), value);
    }
    
    public void setString(String name, String value) throws SQLException {        
    	preparedStatement.setString(getIndex(name), value);
    }    

    public void setDate(String name, Date value) throws SQLException {
    	if( value != null ) {
    		preparedStatement.setDate(getIndex(name), new java.sql.Date(value.getTime()));
    	} else {
    		preparedStatement.setDate(getIndex(name), null);
    	}
    }
    
    public void setObject(String name, Object value) throws SQLException {        
    	preparedStatement.setObject(getIndex(name), value);
    } 
    
    public void setData2( FieldMapping fieldMapping, Map<String, Object> dataMap) throws SQLException {
    	//System.out.println("Processing: " + fieldMapping.getTargetName() + ": " +  dataMap.get(fieldMapping.getSourceName()) );

    	String method = fieldMapping.getMethod();
    	if( method == null ) {
    		method = "direct";
    	}
    	if( "direct".equalsIgnoreCase(method) ) {
			if( fieldMapping.getSourceName() == null ) {
				setObject(fieldMapping.getTargetName(), null);
			} else {
				if( dataMap.get(fieldMapping.getSourceName()) == null) {
					if( fieldMapping.getDefaultValue() == null ) {
						setObject(fieldMapping.getTargetName(), null);
					} else {
						setString(fieldMapping.getTargetName(), fieldMapping.getDefaultValue());
					}
				} else {
					if( "String".equalsIgnoreCase(fieldMapping.getType())) {
						setString(fieldMapping.getTargetName(), dataMap.get(fieldMapping.getSourceName()).toString());
					} else if( "Date".equalsIgnoreCase(fieldMapping.getType())) {
						setDate(fieldMapping.getTargetName(), (Date) dataMap.get(fieldMapping.getSourceName()));
					} else if( "Number".equalsIgnoreCase(fieldMapping.getType())) {
						if( dataMap.get(fieldMapping.getSourceName()) instanceof Integer ){
							setInteger(fieldMapping.getTargetName(), (Integer) dataMap.get(fieldMapping.getSourceName()));
						} else if( dataMap.get(fieldMapping.getSourceName()) instanceof Double ){
							setDouble(fieldMapping.getTargetName(), (Double) dataMap.get(fieldMapping.getSourceName()));
						} else {
							setObject(fieldMapping.getTargetName(), dataMap.get(fieldMapping.getSourceName()));
						}
						
					} else if( "RAW".equalsIgnoreCase(fieldMapping.getType())) {
						setString(fieldMapping.getTargetName(), RAW.newRAW(dataMap.get(fieldMapping.getSourceName())).stringValue());
					} else {
						setObject(fieldMapping.getTargetName(), dataMap.get(fieldMapping.getSourceName()));
					}
				}
			}
    	} else if( "constant".equalsIgnoreCase(method) ) {
    		if(fieldMapping.getDefaultValue() != null ) {
				if( "String".equalsIgnoreCase(fieldMapping.getType())) {
					setString(fieldMapping.getTargetName(), fieldMapping.getDefaultValue());
				} else if( "Date".equalsIgnoreCase(fieldMapping.getType())) {
					if( "SYSDATE".equalsIgnoreCase(fieldMapping.getDefaultValue()) ) {
						setDate(fieldMapping.getTargetName(), new Date() );
					} else {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("YYYY-DD-MM");
							setObject(fieldMapping.getTargetName(), sdf.parse(fieldMapping.getDefaultValue()));
						} catch( Exception ex ) {
							setObject(fieldMapping.getTargetName(), null);
						}
					}
				} else if( "Number".equalsIgnoreCase(fieldMapping.getType())) {
					try {
						setInteger(fieldMapping.getTargetName(), new Integer(fieldMapping.getDefaultValue()));
					} catch( Exception ex ) {
						setInteger(fieldMapping.getTargetName(), new Integer(0));
					}
				} else {
					setObject(fieldMapping.getTargetName(), fieldMapping.getDefaultValue());
				}    			
    			
    		} else {
    			setObject(fieldMapping.getTargetName(), null);
    		}
    	} else {
    		setObject(fieldMapping.getTargetName(), null);
    	}
    		
    }
    
    public void setData( FieldMapping fieldMapping, Map<String, Object> dataMap) throws SQLException {
    	System.out.println("Processing: " + fieldMapping.getTargetName() );
    	if( "String".equalsIgnoreCase(fieldMapping.getType())) {
    		if( fieldMapping.getSourceName() == null ) {
    			setString(fieldMapping.getTargetName(), null);
    		}
    		if( dataMap.get(fieldMapping.getSourceName()) != null) {
    			if( dataMap.get(fieldMapping.getSourceName()) != null) {
    				setString(fieldMapping.getTargetName(), dataMap.get(fieldMapping.getSourceName()).toString());
    			} else {
    				setString(fieldMapping.getTargetName(), null);
    			}
    			
    		} else {
    			if( fieldMapping.getDefaultValue() != null ) {
    				setString(fieldMapping.getTargetName(), fieldMapping.getDefaultValue());
    			} else {
    				setString(fieldMapping.getTargetName(), null);
    			}
    		}
    	}
    	
    	if( "Date".equalsIgnoreCase(fieldMapping.getType())) {
    		if( fieldMapping.getSourceName() == null ) {
    			setDate(fieldMapping.getTargetName(), null);
    		}    		
    		if( dataMap.get(fieldMapping.getSourceName()) != null) {
    			if( dataMap.get(fieldMapping.getSourceName()) != null) {
    				
    				setDate(fieldMapping.getTargetName(), (Date) dataMap.get(fieldMapping.getSourceName()));
    			} else {
    				setDate(fieldMapping.getTargetName(), null);
    			}
    			
    		} else {
    			if( fieldMapping.getDefaultValue() != null ) {
    				//TODO
    				//setDate(fieldMapping.getTargetName(), fieldMapping.getDefaultValue());
    				setDate(fieldMapping.getTargetName(), null);
    			} else {
    				setDate(fieldMapping.getTargetName(), null);
    			}
    		}
    	}    	
    	
    	if( "Number".equalsIgnoreCase(fieldMapping.getType())) {
    		if( fieldMapping.getSourceName() == null ) {
    			setInteger(fieldMapping.getTargetName(), null);
    		}    		
    		if( dataMap.get(fieldMapping.getSourceName()) != null) {
    			if( dataMap.get(fieldMapping.getSourceName()) != null) {
    				setInteger(fieldMapping.getTargetName(), (Integer) dataMap.get(fieldMapping.getSourceName()));
    			} else {
    				setInteger(fieldMapping.getTargetName(), null);
    			}
    			
    		} else {
    			if( fieldMapping.getDefaultValue() != null ) {
    				//TODO
    				//setDate(fieldMapping.getTargetName(), fieldMapping.getDefaultValue());
    				setInteger(fieldMapping.getTargetName(), null);
    			} else {
    				setInteger(fieldMapping.getTargetName(), null);
    			}
    		}
    	} 
    	
    	if( "RAW".equalsIgnoreCase(fieldMapping.getType())) {
    		if( fieldMapping.getSourceName() == null ) {
    			setString(fieldMapping.getTargetName(), null);
    		}      		
    		if( dataMap.get(fieldMapping.getSourceName()) != null) {
    			if( dataMap.get(fieldMapping.getSourceName()) != null) {
    				setString(fieldMapping.getTargetName(), RAW.newRAW(dataMap.get(fieldMapping.getSourceName())).stringValue());
    			} else {
    				setString(fieldMapping.getTargetName(), null);
    			}
    			
    		} else {
    			if( fieldMapping.getDefaultValue() != null ) {
    				setString(fieldMapping.getTargetName(), fieldMapping.getDefaultValue());
    			} else {
    				setString(fieldMapping.getTargetName(), null);
    			}
    		}
    	}     	
    	
    }    
    
    private int getIndex(String name) {
        return fields.indexOf(name)+1;
    }    
	
}
