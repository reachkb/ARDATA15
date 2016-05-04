package model;

public class FieldMapping {
	
	private String targetName;
	private String sourceName;
	private String method;
	private String type;
	private String defaultValue;
	
	public FieldMapping() {
	}
	public FieldMapping(String targetName, String sourceName, String method, String type) {
		super();
		this.targetName = targetName;
		this.sourceName = sourceName;
		this.method = method;
		this.type = type;
	}
	
	public FieldMapping(String targetName, String sourceName, String method, String type, String defaultValue ) {
		super();
		this.targetName = targetName;
		this.sourceName = sourceName;
		this.method = method;
		this.type = type;
		this.defaultValue = defaultValue;
	}

	
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method ) {
		this.method = method;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Override
	public String toString() {
		return "FieldMapping [targetName=" + targetName + ", sourceName=" + sourceName + ", method=" + method
				+ ", type=" + type + ", defaultValue=" + defaultValue + "]";
	}	

	
}
