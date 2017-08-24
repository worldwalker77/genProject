package com.zhaogang.mpay.rpc.hessiantest.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhaogang.mpay.rpc.hessiantest.HessianTestClient;
import com.zhaogang.pricesoa.client.HessianTest;
import com.zhaogang.pricesoa.clientdomain.HessianTestDto;
import com.zhaogang.pricesoa.clientdomain.HessianTestVo;

@Service("hessianTestClient")
public class HessianTestClientImpl implements HessianTestClient{
	
	@Autowired
	private HessianTest hessianTestService;
	
	public Map<String, Object> getHessianTestResult(Integer type) {
		Map<String, Object> result = new HashMap<String, Object>();
		HessianTestDto hessianTestDto = new HessianTestDto(); 
		hessianTestDto.setType(type);
		HessianTestVo hessianTestVo = hessianTestService.getHessianTestVo(hessianTestDto);
		result.put("fieldA", hessianTestVo.getFieldA());
		result.put("fieldB", hessianTestVo.getFieldB());
		return result;
	}
	
}
