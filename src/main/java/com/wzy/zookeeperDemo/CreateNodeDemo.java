package com.wzy.zookeeperDemo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class CreateNodeDemo {
	
	public static final String AUTH_INFO = "root:root"; // 进行连接的授权信息
	public static final String GROUPNODE = "/node1"; // 描述的是根节点
	public static final String SUBNODE = GROUPNODE + "/test"; // 描述的是子节点
	
	
	public static void main(String[] args) throws Exception {
		
		// 多个连接地址之间使用“,”分割，如果不写端口号就是2181
		String connectString = "54.249.79.156:2181,54.249.79.156:2182,54.249.79.156:2183"; // 配置所有的连接地址
		int sessionTimeout = 2000; // 两秒为超时时间，也就是说如果超过2秒还没有连接成功，则表示连接失败
		
		ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout,
				new Watcher() { // 对监听进行处理控制
					public void process(WatchedEvent event) {
						System.out.println("*** 【监听事件处理】path = "
								+ event.getPath() + "、type = " + event.getType()
								+ "、状态 = " + event.getState());
					}
				});
		zkClient.addAuthInfo("digest", AUTH_INFO.getBytes()); // 进行认证授权
		
		//如果根节点不存在
		if (zkClient.exists(GROUPNODE, false) == null) { // null表示节点不存在
			
			// 所有保存在节点中的数据一定要是字节，而且节点的数据千万别设置中文
			zkClient.create(GROUPNODE, "HelloData".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);   //OPEN_ACL_UNSAFE 表示所有人都可以访问
		}
		
		//如果子节点不存在
		if (zkClient.exists(SUBNODE, false) == null) {
			zkClient.create(SUBNODE, "HelloSub".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} 
		zkClient.close(); // 释放连接
	}
}
