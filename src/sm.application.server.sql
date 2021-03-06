select 
	UPSTREAMCI_LOGICAL_NAME,
	DOWNSTREAMCI_LOGICAL_NAME,
	UPSTREAMCI_TYPE,
	DOWNSTREAMCI_TYPE,
	UPSTREAMCI_SUBTYPE,
	DOWNSTREAMCI_SUBTYPE,
	RELATIONSHIP_TYPE,
	RELATIONSHIP_SUBTYPE,
	STATUS,
	UPSTREAMCI_ID,
	DOWNSTREAMCI_ID,
	CREATE_DATETIME,
	UPDATE_DATETIME,
	UPSTREAMCI_UCMDB_ID,
	DOWNSTREAMCI_UCMDB_ID,
	RELATIONSHIP_NAME
from SMDB.dbo.CIGCIRELATIONSHIP1TO1M1
where
  UPSTREAMCI_TYPE = 'application' and
  UPSTREAMCI_SUBTYPE = 'Business App' and
  DOWNSTREAMCI_TYPE = 'server' and
  STATUS = 'In Used'
