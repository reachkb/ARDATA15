package xrp.service;

import java.util.ArrayList;
import java.util.List;

import xrp.dao.DashboardDao;
import xrp.model.DashboardChart;

public class DashboardService {
	
	public String[] getDashboardChartDefinitions() {
		String[] dashboardCharts = {"dashboard_server_trend", "dashboard_server_virtualization", "dashboard_server_discovery", "dashboard_server_os", "dashboard_server_environment", "dashboard_database_manufacturer", "dashboard_database_environment" };
		return dashboardCharts;
	}

	public List<DashboardChart> getDashboardChartData() {
		List<DashboardChart> charts = new ArrayList<>();
		charts.add(getServerTrendChart());
		charts.add(getServerVirtualizationChartData());
		charts.add(getServerDiscoveryChartData());
		charts.add(getServerOSChartData());
		charts.add(getServerEnvironmentChartData());
		charts.add(getDatabaseManufacturerChartData());
		charts.add(getDatabaseEnvironmentChartData());
		return charts;
	}
	
	public DashboardChart getServerTrendChart() {
		return (new DashboardDao()).getServerTrendChartData();
	}

	public DashboardChart getServerVirtualizationChartData() {
		return (new DashboardDao()).getServerVirtualizationChartData();
	}

	public DashboardChart getServerDiscoveryChartData() {
		return (new DashboardDao()).getServerDiscoveryChartData();
	}	
	
	public DashboardChart getServerOSChartData() {
		return (new DashboardDao()).getServerOSChartData();
	}

	public DashboardChart getServerEnvironmentChartData() {
		return (new DashboardDao()).getServerEnvironmentChartData();
	}

	public DashboardChart getDatabaseManufacturerChartData() {
		return (new DashboardDao()).getDatabaseManufacturerChartData();
	}
	
	public DashboardChart getDatabaseEnvironmentChartData() {
		return (new DashboardDao()).getDatabaseEnvironmentChartData();
	}
	
	
	
}
