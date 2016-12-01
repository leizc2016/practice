package com.lzc.zk;

import java.io.IOException;
import java.util.Set;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;

public class ZoopKeeperService implements Runnable {

	public final static String ZK_URL = "127.0.0.1:2181";

	private static ZoopKeeperService instance = null;

	private ZooKeeper zooKeeper = null;

	private Set<String> set = new ConcurrentHashSet<String>();

	ZoopKeeperService() {
		connect();
	}

	public static ZoopKeeperService getInstance() {
		if (instance == null) {
			instance = new ZoopKeeperService();
		}

		return instance;
	}

	public Watcher getWatcher(final String key) {
		set.add(key);
		System.out.println("Watcher [" + key + "] 被注册！");
		Watcher watcher = new Watcher() {
			public void process(WatchedEvent event) {
				EventType type = event.getType();
				System.out.println("--------------key:" + key + " ,节点："
						+ event.getPath() + " ,事件类型：" + type);
				set.remove(key);
				System.out.println(" Watcher [" + key + "] 被移除!");

			}
		};

		return watcher;
	}

	/*
	 * 连接
	 */
	private void connect() {
		try {
			zooKeeper = new ZooKeeper(ZK_URL, 50000000, null);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建节点
	 * 
	 * @param path
	 * @param ephemeral
	 */
	public void simpleCreate(String path, boolean ephemeral) throws Exception {

		if (ephemeral) {
			System.out.println("创建临时节点：" + path);
			zooKeeper.create(path, null, Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL);
		} else {
			System.out.println("创建永久节点：" + path);
			zooKeeper.create(path, null, Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		}

	}

	/**
	 * 创建节点
	 * 
	 * @param path
	 * @param ephemeral
	 */
	public void updateData(String path) throws Exception {

		zooKeeper.setData(path, "leizhicheheng".getBytes(), -1);

	}

	/**
	 * 考虑到watcher一次性，如果watcher被使用了，需要重新注册watcher
	 * 
	 * @param path
	 * @param ephemeral
	 */
	public void registerWatcher(String path) throws Exception {
		String existsKey = path + ":exists";
		String getDataKey = path + ":getData";
		String getChildrenKey = path + ":getChildren";
		if (!set.contains(existsKey)) {
			zooKeeper.exists(path, getWatcher(existsKey));
		}

		if (!set.contains(getDataKey)) {
			if (zooKeeper.exists(path, true) != null) {
				zooKeeper.getData(path, getWatcher(getDataKey), null);
			}
		}

		if (!set.contains(getChildrenKey)) {
			if (zooKeeper.exists(path, true) != null) {
				zooKeeper.getChildren(path, getWatcher(getChildrenKey));
			}
		}

	}

	public void run() {

		while (true) {
			try {
				Thread.sleep(1000);
				registerWatcher("/service");
				registerWatcher("/service/userService");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
