package com.lzc.dubbofiltermonitor;

public class Filter1 implements Filter {

	public int invoke(Invoker invoker) {
		System.out.println("Filter1");
		invoker.invoke();
		System.out.println("Filter1 end");
		return 1;
	}

}
