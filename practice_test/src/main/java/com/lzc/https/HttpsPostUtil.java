package com.lzc.https;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsPostUtil {

	/**
	 * 统一编码为UTF-8
	 */
	private static final String CHARSET = "UTF-8";

	/**
	 * post mothed for https protocol
	 * 
	 * @param url
	 *            api url
	 * @param content
	 *            post parameters
	 * @param charset
	 *            encode type
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public static byte[] post(String url, String content, String charset) throws NoSuchAlgorithmException,
			KeyManagementException, IOException {
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());

		URL console = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
		conn.setSSLSocketFactory(sc.getSocketFactory());
		conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
		conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		conn.setDoOutput(true);
		conn.connect();
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.write(content.getBytes(charset));
		// refresh and close
		out.flush();
		out.close();
		InputStream is = conn.getInputStream();
		if (is != null) {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			is.close();
			return outStream.toByteArray();
		}
		return null;
	}

	/**
	 * post mothed for https protocol(use when login-in)
	 * 
	 * @param url
	 *            api url
	 * @param content
	 *            username&password
	 * @param charset
	 *            encode type
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public static String postLoginIn(String url, String content, String charset) throws NoSuchAlgorithmException,
			KeyManagementException, IOException {
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());

		URL console = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
		conn.setSSLSocketFactory(sc.getSocketFactory());
		conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(true);
		conn.setRequestProperty("Content-Type ", " application/x-www-form-urlencoded ");

		conn.connect();

		DataOutputStream out = new DataOutputStream(conn.getOutputStream());

		out.writeBytes(content);

		out.flush();
		out.close();

		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		String line;
		StringBuilder result = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}
		reader.close();
		conn.disconnect();
		return result.toString();
	}

	/**
	 * 实现证书信任管理器类
	 * 
	 * @author gaoxiang
	 * 
	 */
	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			System.out.println("checkClientTrusted     authType=" + authType);
			for (X509Certificate chain_ : chain) {
				System.out.println("\t" + chain.getClass());
			}
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			System.out.println("checkServerTrusted     authType=" + authType);
			for (X509Certificate chain_ : chain) {
				System.out.println("\t" + chain.getClass());
			}
		}

		public X509Certificate[] getAcceptedIssuers() {
			System.out.println("getAcceptedIssuers");
			return new X509Certificate[] {};
		}
	}

	/**
	 * 实现对证书域名检查
	 * 
	 * @author gaoxiang
	 * 
	 */
	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			System.out.println("hostname=" + hostname + " SSLSession=");
			String[] keys = session.getValueNames();
			for (String key : keys) {
				System.out.println("key=" + key + ",vlaue=" + session.getValue(key));
			}
			return true;
		}
	}

	/**
	 * 使用Get方式获取数据
	 * 
	 * @param url
	 *            URL包括参数，http://HOST/XX?XX=XX&XXX=XXX
	 * @param charset
	 * @return
	 */
	public static String sendGet(Map<String, String> params, String url) {
		String result = "";
		BufferedReader in = null;
		try {
			StringBuffer buffer = new StringBuffer();
			if (params != null && !params.isEmpty()) {
				buffer.append("?");
				for (Map.Entry<String, String> entry : params.entrySet()) {
					// 完成转码操作
					buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), CHARSET))
							.append("&");
				}
				buffer.deleteCharAt(buffer.length() - 1);
			}
			URL realUrl = new URL(url + buffer.toString());
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 将一个输入流转换成指定编码的字符串
	 * 
	 * @param inputStream
	 * @param encode
	 * @return
	 */
	private static String changeInputStream(InputStream inputStream, String encode) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		String result = "";
		if (inputStream != null) {
			try {
				while ((len = inputStream.read(data)) != -1) {
					outputStream.write(data, 0, len);
				}
				result = new String(outputStream.toByteArray(), encode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void main(String[] args) {
		String url = "https://10.112.1.91:8443/practice_front/user/login?uname=lzc&pwd=0432410";
		String result = null;
		try {
			result = new String(HttpsPostUtil.post(url, "", CHARSET));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

}