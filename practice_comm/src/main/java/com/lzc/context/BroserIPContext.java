package com.lzc.context;


/**
 * 客户端ip缓存
 * @author LeiZhicheng 
 * @date 2016-10-13
 *
 */
public class BroserIPContext {

	static final ThreadLocal<String> broserIpCache = new ThreadLocal<String>();

	/**
	 * 获取用户真实IP地址
	 * @return
	 */
	public static String getBroserIP() {
		return broserIpCache.get();
	}

	/**
	 * 设置用户真实IP地址（可能经过中间代理）
	 * @return
	 */
	public static void setBroserIP(String broserIP) {
		broserIpCache.set(broserIP);
	}

}
