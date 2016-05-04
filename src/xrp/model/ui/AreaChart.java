package xrp.model.ui;

import java.util.List;


public class AreaChart {
	private String chartName;
	private int count;
	private int change;
	private int percentage;
	private List<AreaChartData> data;
	
	public AreaChart(String chartName, int count, int change, int percentage, List<AreaChartData> data) {
		super();
		this.chartName = chartName;
		this.count = count;
		this.change = change;
		this.percentage = percentage;
		this.data = data;
	}
	public String getChartName() {
		return chartName;
	}
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getChange() {
		return change;
	}
	public void setChange(int change) {
		this.change = change;
	}
	public int getPercentage() {
		return percentage;
	}
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	public List<AreaChartData> getData() {
		return data;
	}
	public void setData(List<AreaChartData> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "AreaChart [chartName=" + chartName + ", count=" + count + ", change=" + change + ", percentage="
				+ percentage + ", data=" + data + "]";
	}
	
}