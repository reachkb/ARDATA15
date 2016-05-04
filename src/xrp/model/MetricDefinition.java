package xrp.model;

public class MetricDefinition {
	private String name;
	private String title;
	private int order;
	private String description;
	private String type;
	private String active;
	
	public MetricDefinition(String name, String title, int order, String description, String type, String active) {
		super();
		this.name = name;
		this.title = title;
		this.order = order;
		this.description = description;
		this.type = type;
		this.active = active;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	@Override
	public String toString() {
		return "MetricDefinition [name=" + name + ", title=" + title + ", order=" + order + ", description="
				+ description + ", type=" + type + ", active=" + active + "]";
	}
	
}
