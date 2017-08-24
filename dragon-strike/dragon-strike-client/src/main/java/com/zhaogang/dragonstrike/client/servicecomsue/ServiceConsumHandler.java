package com.zhaogang.dragonstrike.client.servicecomsue;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.InitializingBean;

import com.zhaogang.dragonstrike.client.container.ServiceContainer;

public class ServiceConsumHandler implements InitializingBean{
	
	/**服务节点容器*/
	private ServiceContainer serviceContainer = ServiceContainer.getServiceContainer();
	/**zookeeper实例*/
	private ZooKeeper zk;
	/**zk地址*/
	private String zkAddress;
	/**zk上服务组节点名称list*/
	private List<String> groupNodeList;
	
	public void afterPropertiesSet() throws Exception {
		connectZookeeper();
	}
	
	private void connectZookeeper(){
		try {
			zk = new ZooKeeper(zkAddress, 5000, new Watcher() {
				public void process(WatchedEvent event) {
					for(String groupNode : groupNodeList){
						if (event.getType() == EventType.NodeChildrenChanged && ("/" + groupNode).equals(event.getPath())) {  
							serviceContainer.updateServerList(zk, groupNode);
						}
					}
					
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String groupNode : groupNodeList){
			serviceContainer.updateServerList(zk, groupNode);
		}
	}
	
	public void setZkAddress(String zkAddress) {
		this.zkAddress = zkAddress;
	}

	public void setGroupNodeList(List<String> groupNodeList) {
		this.groupNodeList = groupNodeList;
	}
	
}
