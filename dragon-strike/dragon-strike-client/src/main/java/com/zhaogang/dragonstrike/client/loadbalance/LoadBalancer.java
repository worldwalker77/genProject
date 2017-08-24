package com.zhaogang.dragonstrike.client.loadbalance;

public interface LoadBalancer {
	
	public String loadBalance(String groupNode);
	
}
