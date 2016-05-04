package xrp.model.ui;

public class AreaChartData {
	private String date;
	private int metricValue;
	private int order;

	public AreaChartData(String date, int metricValue, int order ) {
		super();
		this.date = date;
		this.metricValue = metricValue;
		this.order = order;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getMetricValue() {
		return metricValue;
	}
	public void setMetricValue(int metricValue) {
		this.metricValue = metricValue;
	}
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "AreaChartData [date=" + date + ", metricValue=" + metricValue + ", order=" + order + "]";
	}

	
	
}