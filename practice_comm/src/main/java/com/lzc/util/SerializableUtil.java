package com.lzc.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.lzc.bean.User;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 序列化
 * 
 * 二进制数据要转成字符串来传输是需要经过编码(例如BASE64)处理才可以的;
 * 
 * 1.在发送端用BASE64Encoder将二进制数据编码成字符串后再发送; byte[] bt = <bt是读取到的图片的二制数据>; String
 * temp = new sun.misc.BASE64Encoder().encodeBuffer(bt);
 * 
 * 2.在接收端用BASE64Decoder对接收到的字符串解码成二进制数据;再输出生成图片; byte[] bt = new
 * sun.misc.BASE64Decoder().decodeBuffer(temp);
 * 
 * BASE64编码参见: http://baike.baidu.com/view/1485202.htm
 * 
 * @author LeiZhicheng
 * 
 * @date：2017-1-23
 */
public class SerializableUtil {

	private static String decoder(Object obj) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oi = new ObjectOutputStream(out);
		oi.writeObject(obj);
		byte[] bs = out.toByteArray();
		String content = new BASE64Encoder().encode(bs);
		out.close();
		oi.close();

		return content;

	}

	private static Object encoder(String content) throws Exception {
		Object obj = null;
		byte[] bs = new BASE64Decoder().decodeBuffer(content);
		InputStream in = new ByteArrayInputStream(bs);
		ObjectInputStream oin = new ObjectInputStream(in);
		obj = oin.readObject();
		in.close();
		oin.close();
		return obj;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String decoder = "rO0ABXNyABFjb20ubHpjLmJlYW4uVXNlcgAAAAAAAAABAgAGSQADYWdlSQACaWRMAARuYW1ldAAS\r\n"
				+ "TGphdmEvbGFuZy9TdHJpbmc7TAADcHdkcQB+AAFMAAN0ZWxxAH4AAUwABXVuYW1lcQB+AAF4cAAA\r\n"
				+ "AAAAAAAAdAADTFpDdAAIMDQzMjQxMDhwcA==";
		User user = new User();
		user.setName("LZC");
		user.setPwd("04324108");
		try {

			String content = decoder(user);
			System.out.println(content);

			Object obj = encoder(decoder);
			System.out.println(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
