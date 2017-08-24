package ${groupId}.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ${groupId}.domain.Result;
import ${groupId}.service.TestService;

@Controller
@RequestMapping(value="/test")
public class TestController {
	protected static final Logger logger = LoggerFactory.getLogger(TestController.class);
	@Autowired
	private TestService testService;
	@RequestMapping(value="/getdata")
	@ResponseBody
	public Result getdata(){
		Result result = new Result();
		result.setData(testService.getTest());
		return result;
	}
	
	@RequestMapping(value="/index")
	public ModelAndView index(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("test");
//		String a = null;
//		a.split("a");
		return mv;
	}
	
}
