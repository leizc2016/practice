package com.lzc.zk.test;


import org.junit.BeforeClass;
import org.junit.Test;

import com.lzc.zk.ZoopKeeperService;

public class ZoopKeeperServiceJunitTest {
	
	private static ZoopKeeperService instance = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		instance = ZoopKeeperService.getInstance();
	}
	
	public static void wait(int second)
	{
		try {
			Thread.sleep(1000*second);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void create() throws Exception{
		new Thread(instance).start();
		wait(2);
		instance.simpleCreate("/service", false);
		wait(2);
		instance.simpleCreate("/service/userService", false);
		/*wait(2);
		instance.simpleCreate("/service/userService/userService@001", false);*/
		wait(5);
	}
	
	@Test
	public void updateData() throws Exception
	{
		new Thread(instance).start();
		wait(2);
		instance.updateData("/service");
		wait(2);
		instance.updateData("/service/userService");
		wait(5);
	}
	
	
	public static void main(String[] args) {
		ZoopKeeperService instance =ZoopKeeperService.getInstance();
		System.out.println("开始监听...");
		new Thread(instance).start();
		
	}

}
