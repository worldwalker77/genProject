package com.zhaogang.pricesoa.service.hessianservice.impl;


import com.zhaogang.pricesoa.client.HessianTest;
import com.zhaogang.pricesoa.clientdomain.HessianTestDto;
import com.zhaogang.pricesoa.clientdomain.HessianTestVo;

public class HessianTestImpl implements HessianTest{

	public HessianTestVo getHessianTestVo(HessianTestDto hessianTestDto) {
		HessianTestVo hessianTestVo = new HessianTestVo();
		hessianTestVo.setFieldA("a");
		hessianTestVo.setFieldB(1);
		return hessianTestVo;
	}
	
}
