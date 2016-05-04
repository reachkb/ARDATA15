package xrp.model;

import java.util.Arrays;

public class DashboardChartData {
	private String name;
	private int share;
	private String color;
	private Integer[] shareenv;
	private int order;
	
	public DashboardChartData(String name, int share, String color, Integer[] shareenv, int order) {
		super();
		this.name = name;
		this.share = share;
		this.color = color;
		this.shareenv = shareenv;
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}	
	
	public Integer[] getShareenv() {
		return shareenv;
	}

	public void setShareenv(Integer[] shareenv) {
		this.shareenv = shareenv;
	}

	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "DashboardChartData [name=" + name + ", share=" + share + ", color=" + color + ", shareenv="
				+ Arrays.toString(shareenv) + ", order=" + order + "]";
	}

	
}
