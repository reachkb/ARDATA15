SELECT   
  s.a_name AS host_name, 
  s.a_discovered_model dis_model,   
  s.a_domain_name,  
  s.a_os_vendor,
	s.a_discovered_os_version os_version, 
	s.a_discovered_vendor AS vendor, 
  s.a_discovered_os_name os_name,
	s.a_node_model nodemodel,  
  s.a_os_description osdesc, 
  s.a_os_family osfamily,
  s.a_os_vendor osvendor, 
  s.a_serial_number serialnumber, 
  s.a_host_osinstalltype osinstalltype,  
  s.a_host_osrelease osrelease,
	s.a_os_architecture osarchitecture, 
  s.a_vendor nodevendor,
	s.a_discovered_location LOCATION, 
  s.a_host_isvirtual AS virtual,
	s.a_bios_date, 
  s.a_bios_uuid, 
  s.a_bios_version,
	s.a_default_gateway_ip_address, 
  s.a_primary_dns_name,
  s.cmdb_id AS global_id,   
  s.a_memory_size,
  rt.a_create_time AS create_time,   
  rt.a_last_modified_time AS modified_time, 
  rt.a_root_lastaccesstime AS lastaccesstime,   
  
  
  MAX(c.a_display_label) AS cpu_name, 
  MAX(c.a_cpu_clock_speed) AS cpu_speed,
	MAX( c.a_core_number) AS max_cpu_core,
  COUNT (DISTINCT (c.cmdb_id)) cpu_count,

  SUM (f.a_disk_size) disk_size,
  COUNT (DISTINCT (f.cmdb_id)) disk_count,
  COUNT (DISTINCT (i.cmdb_id)) AS interface_count
FROM 
  cdm_zos_1 s
  INNER JOIN cdm_eview_1 a ON LOWER (s.cmdb_id) = a.a_root_container   
  LEFT OUTER JOIN cdm_cpu_1 c ON LOWER (s.cmdb_id) = c.a_root_container   
  LEFT OUTER JOIN cdm_file_system_1 f ON LOWER (s.cmdb_id) = f.a_root_container   
  LEFT OUTER JOIN cdm_interface_1 i ON LOWER (s.cmdb_id) = i.a_root_container 
  LEFT OUTER JOIN cdm_root_1 rt ON s.cmdb_id = rt.cmdb_id   

GROUP BY 
  s.a_name, 
  s.a_discovered_model,
  s.a_discovered_model,   
  s.a_domain_name,  
  s.a_os_vendor,
	s.a_discovered_os_version, 
	s.a_discovered_vendor, 
  s.a_discovered_os_name,
	s.a_node_model,  
  s.a_os_description, 
  s.a_os_family,
  s.a_os_vendor, 
  s.a_serial_number, 
  s.a_host_osinstalltype,  
  s.a_host_osrelease,
	s.a_os_architecture, 
  s.a_vendor,
	s.a_discovered_location, 
  s.a_host_isvirtual,
	s.a_bios_date, 
  s.a_bios_uuid, 
  s.a_bios_version,
	s.a_default_gateway_ip_address, 
  s.a_primary_dns_name,
  s.cmdb_id,
  s.a_memory_size,
	rt.a_create_time,   
  rt.a_last_modified_time, 
	rt.a_root_lastaccesstime
