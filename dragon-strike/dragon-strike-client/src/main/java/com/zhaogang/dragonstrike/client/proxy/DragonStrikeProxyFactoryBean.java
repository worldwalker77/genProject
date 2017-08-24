package com.zhaogang.dragonstrike.client.proxy;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import com.zhaogang.dragonstrike.client.loadbalance.LoadBalancer;
import com.zhaogang.dragonstrike.client.loadbalance.PollLoadBalancer;


/**
 * 
 * @author jinfeng.liu
 *
 */
public class DragonStrikeProxyFactoryBean extends HessianProxyFactoryBean{
	
	/**服务路径*/
	private String servicePathName;
	/**负载均衡器*/
	private LoadBalancer loadBalancer;
	/**hessian服务列表对应的zk上的组名称*/
	private String groupNode;
	
	private String prefix = "http://";
	
	private Lock lock = new ReentrantLock();
	
	/**反射方法调用*/
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		/**如果配置文件中没有指定负载均衡的策略，则默认使用轮询的策略*/
		if (null == loadBalancer) {
			lock.lock();
			if (null == loadBalancer) {
				loadBalancer = new PollLoadBalancer();
			}
			lock.unlock();
		}
		String curIpPort = loadBalancer.loadBalance(groupNode);
		System.out.println("当前请求服务器：" + curIpPort);
		this.setServiceUrl(prefix + curIpPort + servicePathName);
		super.afterPropertiesSet();
		return super.invoke(invocation);
	}
	
	
	
	@Override
	public void afterPropertiesSet() {
		/**此处如何绕开？*/
		this.setServiceUrl(prefix);
		super.afterPropertiesSet();
	}
	
	public void setServicePathName(String servicePathName) {
		this.servicePathName = servicePathName;
	}

	public void setGroupNode(String groupNode) {
		this.groupNode = groupNode;
	}

	public void setLoadBalancer(LoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
	}
	
}
