package com.lzc.dubbofiltermonitor;

public class Filter2 implements Filter {

	public int invoke(Invoker invoker) {
		System.out.println("Filter2");
		invoker.invoke();
		System.out.println("Filter2 end");
		return 2;
	}

}
