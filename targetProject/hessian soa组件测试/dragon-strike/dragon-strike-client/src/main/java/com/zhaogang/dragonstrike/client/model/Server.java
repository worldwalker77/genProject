package com.zhaogang.dragonstrike.client.model;

import java.util.Date;

public class Server {
	
	private String ip;
	
	private int weight;
	
	private int effectiveWeight;
	
	private int currentWeight;
	
	private Date checkedDate;
	
	public Server(String ip, int weight){
		this.ip = ip;
		this.weight = weight;
		this.effectiveWeight = this.weight;
		this.currentWeight = 0;
	}
	
	public Server(String ip){
		this.ip = ip;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getEffectiveWeight() {
		return effectiveWeight;
	}
	public void setEffectiveWeight(int effectiveWeight) {
		this.effectiveWeight = effectiveWeight;
	}
	public int getCurrentWeight() {
		return currentWeight;
	}
	public void setCurrentWeight(int currentWeight) {
		this.currentWeight = currentWeight;
	}
	public Date getCheckedDate() {
		return checkedDate;
	}
	public void setCheckedDate(Date checkedDate) {
		this.checkedDate = checkedDate;
	}
	
}
