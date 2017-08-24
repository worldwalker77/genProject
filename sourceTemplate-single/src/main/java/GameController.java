package ${groupId}.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ${groupId}.common.exception.ExceptionEnum;
import ${groupId}.common.utils.JsonUtil;
import ${groupId}.common.utils.redis.RedisOperationService;
import ${groupId}.domain.Result;
import ${groupId}.domain.UserInfo;
import ${groupId}.domain.VersionModel;
import ${groupId}.service.GameService;
import ${groupId}.service.TestService;

@Controller
@RequestMapping(value="/backend")
public class GameController {
	
	protected static final Logger log = LoggerFactory.getLogger(GameController.class);
	
	@Autowired
	private GameService gameService;
	@Autowired
	private RedisOperationService redisOperationService;
	
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
		mv.setViewName("backend/index");
//		String a = null;
//		a.split("a");
		return mv;
	}
	
	@RequestMapping(value="/versioncontroll")
	public ModelAndView versionControll(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("backend/versioncontroll");
//		String a = null;
//		a.split("a");
		return mv;
	}
	
	/**
	 * 上传文件到tomcat指定目录
	 * @param multipartFiles
	 * @param clientVersion 版本号
	 * @param changeLog 新特性
	 * @param req
	 * @return
	 */
	@RequestMapping("/uploadClientfile")  
	@ResponseBody  
	public Result uploadFile(@RequestParam("multipartFiles") MultipartFile[] multipartFiles, String clientVersion, String changeLog, HttpServletRequest request, HttpServletResponse response) { 
		Result result = new Result();
		if ( multipartFiles == null || multipartFiles.length == 0) {
			result.setCode(1);
			result.setDesc("异常");
			return result;
		}
	    try{  
	    	String unrarPath = request.getSession().getServletContext().getRealPath(System.getProperty("file.separator") + "clientversion" +  System.getProperty("file.separator"));
	    	gameService.uploadClientFile(multipartFiles[0], changeLog, clientVersion, unrarPath);
	    } catch (Exception e) {  
	    	log.error("uploadfile error", e);
	    	result.setCode(1);
			result.setDesc("异常");
	    } 
	    return result;
	} 
	
	@RequestMapping(value="/getVersionList")
	@ResponseBody
	public Result getVersionList(VersionModel model){
		return gameService.getVersionList(model);
	}
	
	@RequestMapping(value="/getVersion")
	@ResponseBody
	public Map<String, Object> getVersion(HttpServletResponse response){
		VersionModel versionModel = gameService.getVersion();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code_url", versionModel.getCodeUrl());
		map.put("update_url", versionModel.getUpdateUrl());
		response.addHeader("Access-Control-Allow-Origin", "*");
		return map;
	}
	
	@RequestMapping(value = "/uploadVoice",method = RequestMethod.POST)
	@ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file, String token, Model model,HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (file == null) {
			log.info("请求, 上传语音文件， file:"+ ", token:" + token);
		}else{
			log.info("请求, 上传语音文件， file:"+ file.toString() + ", token:" + token);
		}
		if (StringUtils.isEmpty(token)) {
			token = request.getHeader("token");
		}
		response.addHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
        if (file == null || StringUtils.isEmpty(token)) {
        	result.setCode(ExceptionEnum.PARAMS_ERROR.code);
        	result.setDesc(ExceptionEnum.PARAMS_ERROR.desc);
        	return result;
		}
        UserInfo userInfo = redisOperationService.getUserInfo(token);
        if (userInfo == null) {
        	result.setCode(ExceptionEnum.NEED_LOGIN.code);
        	result.setDesc(ExceptionEnum.NEED_LOGIN.desc);
        	return result;
		}
        Integer roomId = userInfo.getRoomId();
        if (roomId == null) {
        	result.setCode(ExceptionEnum.NEED_ROOM_ID.code);
        	result.setDesc(ExceptionEnum.NEED_ROOM_ID.desc);
        	return result;
		}
		try {
			File dir=new File(request.getSession().getServletContext().getRealPath("/voicefiles/" + roomId));
			if(!dir.exists()){
			    dir.mkdirs();
			}
			String fileName = file.getOriginalFilename();
			String path = dir.getAbsolutePath() + "/" + fileName;
			file.transferTo(new File(path));
			result.setData("http://file.wyqp.worldwalker.cn/voicefiles/" + roomId + "/" + fileName);
		} catch (Exception e) {
			result.setCode(1);
			result.setDesc("系统异常");
			e.printStackTrace();
		}
		log.info("返回, 上传语音文件， result:" + JsonUtil.toJson(result));
        return result;
    }
	
}
