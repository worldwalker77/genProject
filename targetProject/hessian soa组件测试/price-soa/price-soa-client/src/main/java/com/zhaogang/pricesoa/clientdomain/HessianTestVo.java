package com.zhaogang.pricesoa.clientdomain;

import java.io.Serializable;

public class HessianTestVo implements Serializable{

	private static final long serialVersionUID = -3489449530072196795L;
	
	private String fieldA;
	private Integer fieldB;
	public String getFieldA() {
		return fieldA;
	}
	public void setFieldA(String fieldA) {
		this.fieldA = fieldA;
	}
	public Integer getFieldB() {
		return fieldB;
	}
	public void setFieldB(Integer fieldB) {
		this.fieldB = fieldB;
	}
	
}
