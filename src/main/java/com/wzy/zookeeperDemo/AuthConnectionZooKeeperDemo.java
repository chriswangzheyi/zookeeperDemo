package com.wzy.zookeeperDemo;

import java.util.Iterator;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

//有ACL控制的连接Demo
public class AuthConnectionZooKeeperDemo {
	
	
	public static final String AUTH_INFO = "root:root" ;	// 进行连接的授权信息，授权账户：授权密码
	
	
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
		
		
		List<String> children = zkClient.getChildren("/", false); // 得到所有的根节点下的子节点
		Iterator<String> iter = children.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
		zkClient.close(); // 释放连接
	}
}
