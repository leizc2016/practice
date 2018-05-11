package com.lzc.dubbofiltermonitor;

public interface Filter {

	public int invoke(Invoker invoker);

}
