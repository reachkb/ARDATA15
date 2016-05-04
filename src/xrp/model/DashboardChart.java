package xrp.model;


import java.util.List;

public class DashboardChart {
	private String name;
	private String title;
	private String type;
	private int order;
	private List<String> category;
	private List<DashboardChartData> metric;
	private List<DashboardChartData> businessList;

	//{"category":["oracle instance","sql server instance","db2 udb instance","db2 subsystem","sybase instance","mq subsystem"],"businessList":[{"name":"Dbmanufacturers","share":0.0,"shareenv":[7666,1734,920,41,26,20]}]}
	//{"name":"Dbmanufacturers","share":0.0,"shareenv":[7666,1734,920,41,26,20]}

	public DashboardChart(String name, String title, String type, int order, List<String> category,
			List<DashboardChartData> metric, List<DashboardChartData> businessList) {
		super();
		this.name = name;
		this.title = title;
		this.type = type;
		this.order = order;
		this.category = category;
		this.metric = metric;
		this.businessList = businessList;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public List<String> getCategory() {
		return category;
	}

	public void setCategory(List<String> category) {
		this.category = category;
	}

	public List<DashboardChartData> getBusinessList() {
		return businessList;
	}

	public void setBusinessList(List<DashboardChartData> businessList) {
		this.businessList = businessList;
	}

	public List<DashboardChartData> getMetric() {
		return this.metric;
	}

	public void setMetric(List<DashboardChartData> metric) {
		this.metric = metric;
	}

	@Override
	public String toString() {
		return "DashboardChart [name=" + name + ", title=" + title + ", type=" + type + ", order=" + order
				+ ", category=" + category + ", metric=" + metric + ", businessList=" + businessList + "]";
	}
	
}
