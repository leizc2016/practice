package com.lzc.dubbofiltermonitor;

import java.util.Arrays;
import java.util.List;

public class FilterChainTest {

	public static void main(String[] args) {
		List<Filter> filters = Arrays.asList(new Filter1(), new Filter2());

		Invoker last = new Invoker() {
			public int invoke() {
				System.out.println("invoker");
				return 0;
			}
		};

		for (int i = filters.size() - 1; i >= 0; i--) {
			// 获取filter
			final Filter filter = filters.get(i);
			final Invoker next = last;

			// 更新last
			last = new Invoker() {
				public int invoke() {
					return filter.invoke(next);
				}
			};
		}

		last.invoke();
	}

}
