package com.zhaogang.dragonstrike.client.loadbalance;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections.CollectionUtils;

import com.zhaogang.dragonstrike.client.container.ServiceContainer;
import com.zhaogang.dragonstrike.client.model.Server;

/**
 * 轮询负载均衡器
 * @author jinfeng.liu
 *
 */
public class PollLoadBalancer implements LoadBalancer{
	
	/**服务节点容器*/
	private ServiceContainer serviceContainer = ServiceContainer.getServiceContainer();
	
	private Lock lock = new ReentrantLock();
	
	/**上一次请求调用所访问的服务器ip在列表中的位置*/
	private int preServiceIpPortIndex = 0;
	
	/**
	 * 负载均衡选择器，通过轮询的方式选出当前请求需要访问的服务器ip和端口
	 */
	public String loadBalance(String groupNode) {
		List<Server> serviceIpPortList = serviceContainer.getServerList(groupNode);
		if (CollectionUtils.isEmpty(serviceIpPortList)) {
			return null;
		}
		int ipPortTotalNum = serviceIpPortList.size();
		int curServiceIpPortIndex = 0;
		/**由于多个线程对共享变量preServiceIpPortIndex存在更新，所以需要加锁*/
		lock.lock();
		curServiceIpPortIndex = ++preServiceIpPortIndex;
		if (curServiceIpPortIndex >= ipPortTotalNum) {
			preServiceIpPortIndex = curServiceIpPortIndex%ipPortTotalNum;
			curServiceIpPortIndex = preServiceIpPortIndex;
		}
		/**解锁*/
		lock.unlock();
		String curIpPort =  serviceIpPortList.get(curServiceIpPortIndex).getIp();
		return curIpPort;
	}
	
}
