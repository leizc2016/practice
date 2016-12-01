package com.lzc.zk.test;


import org.junit.BeforeClass;
import org.junit.Test;

import com.lzc.zk.ZkClientService;

public class ZkClientServiceJunitTest {

	private static ZkClientService instance = null;
	
	public static void wait(int second)
	{
		try {
			Thread.sleep(1000*second);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		instance = ZkClientService.getInstance();
	}

	@Test
	public void create() {
		instance.simpleCreate("/service", false);
		wait(5);
		instance.simpleCreate("/service/userService/userService@001", false);
		wait(5);
	}
	
	public static void main(String[] args) {
		ZkClientService instance = ZkClientService.getInstance();
		System.out.println("开始监听...");
		wait(2);
		instance.updateData("/service/userService");
		wait(1000);
	}

}
