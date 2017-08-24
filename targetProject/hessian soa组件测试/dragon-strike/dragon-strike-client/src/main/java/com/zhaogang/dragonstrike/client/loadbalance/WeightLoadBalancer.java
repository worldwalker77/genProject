package com.zhaogang.dragonstrike.client.loadbalance;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.InitializingBean;

import com.zhaogang.dragonstrike.client.container.ServiceContainer;
import com.zhaogang.dragonstrike.client.model.Server;
import com.zhaogang.dragonstrike.common.utils.JsonUtil;

/**
 * 权重负载均衡器
 * @author jinfeng.liu
 *
 */
public class WeightLoadBalancer implements LoadBalancer, InitializingBean{
	
	/**服务节点容器*/
	private ServiceContainer serviceContainer = ServiceContainer.getServiceContainer();
	
	/**每个服务各个节点访问的权重配置*/
	private Map<String, Map<String, Integer>> weightMap;
	
	private Lock lock = new ReentrantLock();
	
	
	public String loadBalance(String groupNode) {
		List<Server> serverList = serviceContainer.getServerList(groupNode);
		Server bestServer = getBestServer(serverList);
		if (null != bestServer) {
			if (ServiceContainer.getCountMap().containsKey(bestServer.getIp())) {
				ServiceContainer.getCountMap().put(bestServer.getIp(), ServiceContainer.getCountMap().get(bestServer.getIp()) + 1);
			}else{
				ServiceContainer.getCountMap().put(bestServer.getIp(), 1);
			}
			System.out.println(JsonUtil.obj2json(ServiceContainer.getCountMap()));
			return bestServer.getIp();
		}
		return null;
		
	}
	
	/**
	 * 权重负载均衡算法
	 * @param serverList
	 * @return
	 * @author jinfeng.liu
	 * @date 2016年4月12日 下午3:10:35
	 */
	private Server getBestServer(List<Server> serverList){
		Server server = null;
		Server best = null;
		int total = 0;
		lock.lock();
		try {
			for(int i=0,len=serverList.size();i<len;i++){
				/**当前服务器对象*/
				server = serverList.get(i);
				server.setCurrentWeight(server.getCurrentWeight() + server.getEffectiveWeight());
				total += server.getEffectiveWeight();
				if(server.getEffectiveWeight() < server.getWeight()){
					server.setEffectiveWeight(server.getEffectiveWeight() + 1);
				}
				if(best == null || server.getCurrentWeight()>best.getCurrentWeight()){
					best = server;
				}
			}
			if (best == null) {
				return null;
			}
			best.setCurrentWeight(best.getCurrentWeight() - total);
			best.setCheckedDate(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
		return best;
	}
	
	public void afterPropertiesSet() throws Exception {
		serviceContainer.setWeightMap(weightMap);
	}

	public void setWeightMap(Map<String, Map<String, Integer>> weightMap) {
		this.weightMap = weightMap;
	}

}
