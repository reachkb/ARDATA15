package model;

import java.util.List;

public class TableMapping {
	private String targetTableName;
	private List<FieldMapping> fieldMapings;

	public TableMapping() {
	}
	
	public TableMapping(String targetTableName, List<FieldMapping> fieldMapings) {
		super();
		this.targetTableName = targetTableName;
		this.fieldMapings = fieldMapings;
	}
	public String getTargetTableName() {
		return targetTableName;
	}
	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}
	public List<FieldMapping> getFieldMapings() {
		return fieldMapings;
	}
	public void setFieldMapings(List<FieldMapping> fieldMapings) {
		this.fieldMapings = fieldMapings;
	}
	
	@Override
	public String toString() {
		return "TableMapping [targetTableName=" + targetTableName + ", fieldMapings=" + fieldMapings + "]";
	}

	
}
