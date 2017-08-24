package ${groupId}.service.hessianservice.impl;


import ${groupId}.client.HessianTest;
import ${groupId}.clientdomain.HessianTestDto;
import ${groupId}.clientdomain.HessianTestVo;

public class HessianTestImpl implements HessianTest{

	public HessianTestVo getHessianTestVo(HessianTestDto hessianTestDto) {
		HessianTestVo hessianTestVo = new HessianTestVo();
		hessianTestVo.setFieldA("a");
		hessianTestVo.setFieldB(1);
		return hessianTestVo;
	}
	
}
