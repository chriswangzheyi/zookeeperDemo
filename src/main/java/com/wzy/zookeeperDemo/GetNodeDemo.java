package com.wzy.zookeeperDemo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class GetNodeDemo {
	public static final String AUTH_INFO = "root:root"; // 进行连接的授权信息
	public static final String GROUPNODE = "/node1"; // 描述的是根节点
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
		if (zkClient.exists(GROUPNODE, false) != null) { // 现在节点存在了
			Stat stat = new Stat() ;
			// 获取指定路径的数据信息，数据信息会以字节数组的形式返回
			String data = new String(zkClient.getData(GROUPNODE, false, stat)) ;
			System.out.println(data);
		} 
		zkClient.close(); // 释放连接
	}
}
