package xrp.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import xrp.dao.DiscoveryDao;
import xrp.model.ui.AreaChartData;

public class DiscoveryService {

	public Map<String, Integer> getDiscoveryDashboardData() {
		DiscoveryDao dd = new DiscoveryDao();
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		map.put("total", dd.getNonDiscoveredServerTotal());
		map.put("remediation", dd.getNonDiscoveredServerRemediationTotal());
		return map;
	}
	
	public Map<String, Map<String, Integer>> getDiscoveryReconciliationByLocation() {
		DiscoveryDao dd = new DiscoveryDao();
		Map<String, Map<String, Integer>> newMap = new HashMap<>();
		Map<String, Map<String, Integer>> map = dd.getDiscoveryReconciliationData("location");
		for( String location: map.keySet() ) {
			Map<String, Integer> locationData = map.get(location);
			Map<String, Integer> newLocationData = new HashMap<>();
			int sm = 0;
			int ud = 0;
			int both = 0;
			int exception = 0;
			int total  = 0;
			
			if( locationData.get("sm") != null) {
				sm = locationData.get("sm").intValue();
			}

			if( locationData.get("ud") != null) {
				ud = locationData.get("ud").intValue();
			}

			if( locationData.get("both") != null) {
				both = locationData.get("both").intValue();
			}

			if( locationData.get("exception") != null) {
				exception = locationData.get("exception").intValue();
			}
			
			total = sm + ud + both;
			newLocationData.put("server", new Integer(sm + both) );
			newLocationData.put("discovered", new Integer(both) );
			newLocationData.put("notDiscovered", new Integer(sm) );
			newLocationData.put("exception", new Integer(exception) );
			newLocationData.put("new", new Integer(ud) );
			newLocationData.put("total", new Integer(total) );
			
			newMap.put(location, newLocationData);
			
		}

		
		return newMap;
	}
	
	public Map<String, Map<String, Integer>> getDiscoveryReconciliationByBusiness() {
		DiscoveryDao dd = new DiscoveryDao();
		Map<String, Map<String, Integer>> newMap = new HashMap<>();
		Map<String, Map<String, Integer>> map = dd.getDiscoveryReconciliationData("business");
		System.out.println("#### " + map);
		for( String location: map.keySet() ) {
			Map<String, Integer> locationData = map.get(location);
			Map<String, Integer> newLocationData = new HashMap<>();
			int sm = 0;
			int ud = 0;
			int both = 0;
			int exception = 0;
			int total  = 0;
			
			if( locationData.get("sm") != null) {
				sm = locationData.get("sm").intValue();
			}

			if( locationData.get("ud") != null) {
				ud = locationData.get("ud").intValue();
			}

			if( locationData.get("both") != null) {
				both = locationData.get("both").intValue();
			}

			if( locationData.get("exception") != null) {
				exception = locationData.get("exception").intValue();
			}
			
			total = sm + ud + both;
			newLocationData.put("server", new Integer(sm + both) );
			newLocationData.put("discovered", new Integer(both) );
			newLocationData.put("notDiscovered", new Integer(sm) );
			newLocationData.put("exception", new Integer(exception) );
			newLocationData.put("new", new Integer(ud) );
			newLocationData.put("total", new Integer(total) );
			
			newMap.put(location, newLocationData);
			
		}

		
		return newMap;
	}
	
}
