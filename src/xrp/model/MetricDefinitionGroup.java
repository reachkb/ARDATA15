package xrp.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MetricDefinitionGroup {
	private String name;
	private int order;
	private List<MetricDefinition> metricDefinition;
	
	
	public MetricDefinitionGroup(String name, int order, List<MetricDefinition> metricDefinition) {
		super();
		this.name = name;
		this.order = order;
		this.metricDefinition = metricDefinition;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public List<MetricDefinition> getMetricDefinition() {
        Collections.sort(metricDefinition, new Comparator<MetricDefinition>() {
        	@Override
        	public int compare( MetricDefinition md1, MetricDefinition md2 ) {
        		return (md1.getOrder() > md2.getOrder()) ? 1 : -1;
        	}
		});
		return metricDefinition;
	}
	public void setMetricDefinition(List<MetricDefinition> metricDefinition) {
		this.metricDefinition = metricDefinition;
	}
	@Override
	public String toString() {
		return "MetricDefinitionGroup [name=" + name + ", order=" + order + ", metricDefinition=" + metricDefinition
				+ "]";
	}
	

}
