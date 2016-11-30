package com.lzc.ds;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(DynamicDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		String key = DataSourceContextHolder.getDSKey();
		System.out.println("dataSource---->" + key);
		return key;
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}
