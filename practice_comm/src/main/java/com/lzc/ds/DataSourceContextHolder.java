package com.lzc.ds;

public class DataSourceContextHolder {

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setDSKey(String ds) {
		
		contextHolder.set(ds);
	}

	public static String getDSKey() {
		return contextHolder.get();
	}

	public static void remove() {
		contextHolder.remove();
	}

}
