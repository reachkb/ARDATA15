package xrp.model;

import java.util.Date;

import util.DateUtil;

public class Metric {
	private Date date;
	private String name;
	private int count;
	private int total;

	private int countDelta;
	private int totalDelta;
	
	public Metric(Date date, String name, int count, int total, int countDelta, int totalDelta ) {
		super();
		this.date = DateUtil.removeTime(date);
		this.name = name;
		this.count = count;
		this.total = total;
		this.countDelta = countDelta;
		this.totalDelta = totalDelta;
	}
	
	public Metric(Date date, String name, int count, int total ) {
		super();
		this.date = DateUtil.removeTime(date);
		this.name = name;
		this.count = count;
		this.total = total;
	}
	
	public void setDelta( int countDelta, int totalDelta ) {
		this.countDelta = countDelta;
		this.totalDelta = totalDelta;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = DateUtil.removeTime(date);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCountDelta() {
		return countDelta;
	}

	public void setCountDelta(int countDelta) {
		this.countDelta = countDelta;
	}

	public int getTotalDelta() {
		return totalDelta;
	}

	public void setTotalDelta(int totalDelta) {
		this.totalDelta = totalDelta;
	}
	
	public int getPercentage() {
		if( total != 0 ) {
			return (count*100)/total;
		} else {
			return 0;
		}
	}
	
	public int getPercentageDelta() {
		if( totalDelta != 0 ) {
			return (countDelta*100)/totalDelta;  
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "Metric [date=" + date + ", name=" + name + ", count=" + count + ", total=" + total + ", countDelta="
				+ countDelta + ", totalDelta=" + totalDelta + ", getPercentage()=" + getPercentage()
				+ ", getPercentageDelta()=" + getPercentageDelta() + "]";
	}

}
