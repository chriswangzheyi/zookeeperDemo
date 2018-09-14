package com.wzy.server.lisener;



public class StartServerMain {
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {	// 现在执行程序的时候没有传递参数
			System.out.println("错误的执行程序，应该在执行的时候输入服务器名称，例如：java 程序类 服务器名称。");
			System.exit(1); // 系统退出
		}
		new ServerListener(args[0]) ;	// 启动ZooKeeper注册
	}
}
