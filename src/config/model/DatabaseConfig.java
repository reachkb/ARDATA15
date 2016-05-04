package config.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import encryption.Encryptor;

public class DatabaseConfig {

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
	
	@JsonIgnore
	public String getPasswordDecrypted() {
		if( isPasswordEncrypted() ) {
			return Encryptor.decrypt(this.password.substring(5));
		} else {
			return this.password;
		}
	}
	
	@JsonIgnore
	public void setPasswordEncrypted( String passwordNew ) {
		System.out.println("*** setDbPasswordEncrypted >>>> " + passwordNew);
		this.password = "[ENC]" + Encryptor.encrypt(passwordNew);
	}
	
	@JsonIgnore
	public boolean isPasswordEncrypted() {
		if( this.password != null && this.password.length() > 5 &&  this.password.startsWith("[ENC]") ) {
			return true;
		} else {
			return false;
		}		
	}	
	
	
	@Override
	public String toString() {
		return "DatabaseConfig [name=" + name + ", dbType=" + dbType + ", url=" + url + ", userName=" + userName + ", password="
				+ password + ", timeOut=" + timeOut + "]";
	}
	private String name;
	private String dbType;
	private String url;
	private String userName;
	private String password;
	private long timeOut;
	
	

}
