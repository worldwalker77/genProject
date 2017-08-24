package com.zhaogang.dragonstrike.client.container;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.zhaogang.dragonstrike.client.model.Server;

/**
 * 服务节点存储容器类
 * @author jinfeng.liu
 *
 */
public class ServiceContainer {
	
	private  Map<String, List<Server>> serverMap = new HashMap<String, List<Server>>();
	
	private Map<String, Map<String, Integer>> weightMap;
	/**读写锁*/
	private ReadWriteLock  readwritelLock = new ReentrantReadWriteLock();
	
	private Stat stat = new Stat();
	
	private static ServiceContainer serviceContainer = new ServiceContainer();
	
	/**测试统计调用测试*/
	private static Map<String, Integer> countMap = new HashMap<String, Integer>();
	
	/**
	 * 更新内存中的ServiceIpPortList
	 * @param zk
	 * @param groupNode
	 */
	public void updateServerList(ZooKeeper zk, String groupNode){
		
		/**测试统计调用测试*/
		countMap = new HashMap<String, Integer>();
		
		List<Server> newServerList = new ArrayList<Server>();
		/**加写锁*/
		readwritelLock.writeLock().lock();
		try {
			List<String> subList = zk.getChildren("/" + groupNode, true);
			for(String subNode : subList){
				byte[] data = zk.getData("/" + groupNode + "/" + subNode, false, stat);  
				try {
					String ipPort = new String(data, "utf-8");
					Server server = new Server(ipPort);
					if (null != weightMap && 0 != weightMap.size()) {
						Map<String, Integer> curWeightMap = weightMap.get(groupNode);
						server.setWeight(curWeightMap.get(ipPort));
						server.setEffectiveWeight(curWeightMap.get(ipPort));
						server.setCurrentWeight(0);
					}
					newServerList.add(server);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (CollectionUtils.isNotEmpty(newServerList)) {
				serverMap.put(groupNode, newServerList);
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			/**释放写锁*/
			readwritelLock.writeLock().unlock();
		}  
	}
	/**
	 * 获取ServiceIpPortList
	 * @return
	 */
	public List<Server> getServerList(String groupNode) {
		readwritelLock.readLock().lock();
		try {
			List<Server> serverList = serverMap.get(groupNode);
			return serverList;
		}finally{
			readwritelLock.readLock().unlock();
		}
	}
	public void setWeightMap(Map<String, Map<String, Integer>> weightMap) {
		this.weightMap = weightMap;
	}
	
	public static ServiceContainer getServiceContainer() {
		return serviceContainer;
	}
	
	public static Map<String, Integer> getCountMap() {
		return countMap;
	}
	
}
