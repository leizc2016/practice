package com.lzc.regex.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RegexJuniTest {

	@Test
	public void groupTest() {
		String ip = "aaaa127.0.0.1:8080vvv";
		String portRegex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.(\\d{1,3}):(\\d+))";
		Pattern pattern = Pattern.compile(portRegex);
		Matcher matcher = pattern.matcher(ip);
		if (matcher.find()) {
			System.out.println(matcher.groupCount());
			for (int i = 0; i < matcher.groupCount(); i++) {
				System.out.println(matcher.group(i + 1));
			}
		}
	}

	/**
	 * 1）非得话必须含[^]
	 * 2）|代表或 (ab|c) 这个代表ab或c
	 */
	@Test
	public void startEndTest() {
		String src = "ac123bc";
		String regex = "^[^abc]\\d{1,3}bc$";
		String regex1 = "^(a|b|c)\\d{1,3}bc$";
		String regex2 = "^(ab|c)\\d{1,3}bc$";
		String regex4 = "a(b[ac]|c[ab])";// (aba|abc|aca|acb) 等同
		Pattern pattern = Pattern.compile(regex2);
		Matcher matcher = pattern.matcher(src);
		if (matcher.matches()) {
			System.out.println("匹配");
		}
	}

}
