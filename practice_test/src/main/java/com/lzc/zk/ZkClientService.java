package com.lzc.zk;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZkClientService {

	public final static String ZK_URL = "127.0.0.1:2181";

	private static ZkClientService instance = null;

	private ZkClient zk = null;

	ZkClientService() {
		connect();
		sucribleChange();
	}

	public static ZkClientService getInstance() {
		if (instance == null) {
			instance = new ZkClientService();
		}

		return instance;
	}

	/*
	 * 连接
	 */
	private void connect() {
		zk = new ZkClient(ZK_URL);
	}

	private void sucribleChange() {
		//String[] paths = new String[] { "/service", "/service/userService" };
		String[] paths = new String[] { "/service" };
		for (String path : paths) {
			zk.subscribeChildChanges(path, new IZkChildListener() {

				public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
					System.out.println("Method: handleChildChange(), path: " + parentPath + ", CurrentChilds:");
					for (String currentChild : currentChilds) {
						System.out.println("\t" + currentChild);
					}

				}
			});

			zk.subscribeStateChanges(new IZkStateListener() {

				public void handleStateChanged(KeeperState state) throws Exception {
					System.out.println("Method: handleStateChanged(), KeeperState: " + state.name());

				}

				public void handleNewSession() throws Exception {
					System.out.println("Method: handleNewSession()");
				}
			});

			zk.subscribeDataChanges(path, new IZkDataListener() {

				public void handleDataDeleted(String dataPath) throws Exception {
					System.out.println("Method: handleDataDeleted(), dataPath: " + dataPath);
				}

				public void handleDataChange(String dataPath, Object data) throws Exception {
					System.out.println("Method: handleDataChange(), dataPath: " + dataPath + ", data: " + data);

				}
			});
		}
	}

	/**
	 * 递归创建节点 临时节点不允许有子节点
	 * 
	 * @param path
	 * @param ephemeral
	 */
	public void recursionCreate(String path, boolean ephemeral) {

		int i = path.lastIndexOf('/');
		if (i > 0) {
			recursionCreate(path.substring(0, i), false);
		}
		if (ephemeral) {
			System.out.println("创建临时节点：" + path);
			zk.createEphemeral(path);
		} else {
			System.out.println("创建永久节点：" + path);
			zk.createPersistent(path, true);
		}

	}

	/**
	 * 创建节点
	 * 
	 * @param path
	 * @param ephemeral
	 */
	public void simpleCreate(String path, boolean ephemeral) {

		if (ephemeral) {
			System.out.println("创建临时节点：" + path);
			zk.createEphemeral(path, "leizhicheng");
		} else {
			System.out.println("创建永久节点：" + path);
			zk.createPersistent(path, true);
			// zk.createPersistent(path, "leizhicheng");
		}

	}

	/**
	 * 创建节点
	 * 
	 * @param path
	 * @param ephemeral
	 */
	public void updateData(String path) {

		zk.writeData(path, "leizhicheng");

	}

}
