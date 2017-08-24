package com.zhaogang.dragonstrike.server.serviceregistory;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;

import com.zhaogang.dragonstrike.common.utils.CommonUtil;

public class ServiceRegistoryHandler implements InitializingBean{
	
	/**zk地址*/
	private String zkAddress;
	/**服务组的zk路径*/
	private String groupNode;
	
	private String subNode;
	
	private Stat stat = new Stat();
	
	public void afterPropertiesSet() throws Exception {
		connectZookeeper();
	}
	
	private void connectZookeeper(){
		try {
			ZooKeeper zk = new ZooKeeper(zkAddress, 5000, new Watcher() {
				public void process(WatchedEvent event) {
					
				}
			});
			System.out.println( " Finished starting ZK:  "   +  zk);
			try {
				stat = zk.exists("/" + groupNode, null);
				if (stat == null) {
					zk.create("/" + groupNode, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
				zk.create("/" + groupNode + "/" + subNode, CommonUtil.getCurServerAddress().getBytes("utf-8"), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
				System.out.println("本机地址：" + CommonUtil.getCurServerAddress());
				List<String> subList = zk.getChildren("/" + groupNode, true);
				for(String subNode : subList){
					byte[] data = zk.getData("/" + groupNode + "/" + subNode, false, stat);
					System.out.println("subNode:" + subNode +",subNodeData:" + new String(data, "utf-8"));
				}
				
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void setZkAddress(String zkAddress) {
		this.zkAddress = zkAddress;
	}

	public void setGroupNode(String groupNode) {
		this.groupNode = groupNode;
	}

	public void setSubNode(String subNode) {
		this.subNode = subNode;
	}
}
