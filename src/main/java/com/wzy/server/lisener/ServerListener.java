package com.wzy.server.lisener;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class ServerListener {
	// 如果要基于ZooKeeper访问，那么肯定需要知道ZooKeeper连接地址信息
	public static final String CONNECTION_URL = "54.249.79.156:2181,54.249.79.156:2182,54.249.79.156:2183";
	public static final int SESSION_TIMEOUT = 2000; // 连接超时时间
	public static final String AUTH_INFO = "root:root"; // 进行连接的授权信息
	public static final String GROUPNODE = "/servers"; // 根节点
	public static final String SUBNODE = GROUPNODE + "/server-"; // 子节点
	private ZooKeeper zkClient; // 定义一个ZOoKeeper客户端处理对象
	/**
	 * 在进行服务器信息列表的时候应该明确的知道当前服务器的名称，这个名称可以在程序运行的时候动态设置
	 * 
	 * @param serverName
	 *            服务器的名称
	 */
	public ServerListener(String serverName) throws Exception {
		this.connectZooKeeperServer(serverName);	// ZooKeeper连接控制，同时进行各个节点的创建
		this.handle(); // 连接准备好之后，要自动进行指定任务的调用
	}
	
	/**
	 * 定义真正要实现的一些具体的操作业务代码，例如：在服务器启动的时候有可能要通过某些系统记录出服务器的启动次数。
	 * @throws Exception 
	 */
	
	
	public void handle() throws Exception {
		System.out.println("**************** 要执行某些处理的操作 *****************");
		Thread.sleep(Long.MAX_VALUE); // 进行休眠
	}
	/**
	 * 取得一个ZooKeeper的连接信息
	 * 
	 * @param serverName
	 *            服务器的名称
	 */
	public void connectZooKeeperServer(String serverName) throws Exception {
		this.zkClient = new ZooKeeper(CONNECTION_URL, SESSION_TIMEOUT,
				new Watcher() {
					
					public void process(WatchedEvent event) {
					}
				});
		zkClient.addAuthInfo("digest", AUTH_INFO.getBytes()); // 进行认证授权
		
		//父节点
		if (this.zkClient.exists(GROUPNODE, false) == null) { // 现在一定要判断父节点是否存在，如果不存在要创建
			// 创建的父节点一定是一个持久化节点，而父节点里面的内容才属于瞬时节点
			this.zkClient.create(GROUPNODE, "SERVER-LIST".getBytes(),
					ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		
		//子节点
		if (this.zkClient.exists(SUBNODE, false) == null) { // 现在子节点不存在
			// 所有的子节点都应该是一个临时的节点信息，但是该节点信息里面保存有全部的服务器主机名称
			this.zkClient.create(SUBNODE, serverName.getBytes(),
					ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL_SEQUENTIAL);
		}
	}
}
