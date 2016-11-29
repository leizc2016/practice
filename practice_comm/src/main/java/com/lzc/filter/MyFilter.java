package com.lzc.filter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;



public class MyFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest req, ServletResponse rep,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		System.out.println("filter executed!");

		String ip = getLocalIP();
		System.out.println("ip=" + ip);

		String broserIP = getRemoteIpAddress(request);
		System.out.println("broserIP--->" + broserIP);

		chain.doFilter(req, rep);

	}

	public void init(FilterConfig config) throws ServletException {

	}

	/**
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();

		}

		ip = transferIP(ip);

		return ip;
	}

	private static String transferIP(String ip) {
		if (ip != null
				&& (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1"))) {
			InetAddress addr;
			try {
				addr = InetAddress.getLocalHost();
				String realIP = addr.getHostAddress().toString();
				if (realIP != null) {
					return realIP;
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

		}

		return ip;

	}

	private static final Pattern IP_PATTERN = Pattern
			.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

	

	public static String getLocalIP() {
		InetAddress localAddress = getLocalAddress0();
		return localAddress == null ? "127.0.0.1" : localAddress
				.getHostAddress();
	}
	
	private static boolean isValidAddress(InetAddress address) {

		if (address == null || address.isLoopbackAddress())
			return false;
		String ip = address.getHostAddress();

		if (ip != null && !ip.startsWith("0.0") && !"127.0.0.1".equals(ip)
				&& IP_PATTERN.matcher(ip).matches()) {
			return true;
		}

		return false;
	}

	private static InetAddress getLocalAddress0() {
		InetAddress localAddress = null;
		try {
			localAddress = InetAddress.getLocalHost();
			if (isValidAddress(localAddress)) {
				System.out.println("getLocalHost"+localAddress.getHostAddress());
				//return localAddress;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			if (interfaces != null) {
				while (interfaces.hasMoreElements()) {

					NetworkInterface network = interfaces.nextElement();
					Enumeration<InetAddress> addresses = network
							.getInetAddresses();
					if (addresses != null) {
						while (addresses.hasMoreElements()) {
							InetAddress address = addresses.nextElement();
							if (isValidAddress(address)) {
								System.out.println("address"+address.getHostAddress());
								//return address;
							}
						}
					}
				}

			}
		} catch (Exception e) {
			System.out.println("Failed to retriving ip address, "
					+ e.getMessage());
		}
		System.out
				.println("Could not get local host ip address, will use 127.0.0.1 instead.");
		return localAddress;
	}

}
