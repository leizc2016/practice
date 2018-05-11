package com.lzc.proxy.dynamic;

public class MyInvocationHandlerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MyInvocationHandler myInvocationHandler = new MyInvocationHandler((MyService)new MyServiceImpl());
		MyService myservice=(MyService) myInvocationHandler.getProxy();
		System.out.println(myservice.getClass().getDeclaredMethods());
		myservice.add();

	}

}
