package com.zhaogang.mpay.web.test;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zhaogang.mpay.domain.Test;
import com.zhaogang.mpay.domain.result.Result;
import com.zhaogang.mpay.service.test.TestService;

@Controller
@RequestMapping("test/")
public class TestController {
	
	@Autowired
	private TestService testService;
	
	@RequestMapping("testIndex")
	public ModelAndView testIndex(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("test/testIndex");
		return mv;
	}
	
	@RequestMapping("doTest")
	@ResponseBody
	public Result doTest(HttpServletRequest request,HttpServletResponse response){
		Result result = new Result();
		Test test = new Test();
		test.setMerchantId(123);
		List<Test> list = testService.myTest(test);
		result.setData(list);
		return result;
	}
	
	@RequestMapping("doTest1")
	@ResponseBody
	public Result doTest1(HttpServletRequest request,HttpServletResponse response){
		Result result = new Result();
		Map<String, Object> map = testService.myTest1();
		result.setData(map);
		return result;
	}
}
