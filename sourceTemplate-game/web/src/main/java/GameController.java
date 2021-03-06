package ${groupId}.web.game;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ${groupId}.common.constant.Constant;
import ${groupId}.common.utils.JsonUtil;
import ${groupId}.common.utils.redis.JedisTemplate;
import ${groupId}.domain.game.GameRequest;
import ${groupId}.domain.game.Msg;
import ${groupId}.domain.result.Result;
import ${groupId}.domain.result.ResultCode;
import ${groupId}.service.game.GameService;

@Controller
@RequestMapping("game/")
public class GameController {
	@Autowired
	private JedisTemplate jedisTemplate;
	
	@Autowired
	private GameService gameService;
	
	@RequestMapping("login")
	@ResponseBody
	public Result login(String code, String deviceType, HttpServletResponse response, HttpServletRequest request){
		response.addHeader("Access-Control-Allow-Origin", "*");
		Result result = null;
		if ("1".equals(jedisTemplate.get(Constant.jinhuaLoginFuse))) {
			result = gameService.login(code, deviceType, request);
		}else{
			result = gameService.login1(code, deviceType, request);
		}
		return result;
	}
	
	@RequestMapping("getIpByRoomId")
	@ResponseBody
	public Result getIpByRoomId(String token, Long roomId, HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "*");
		return gameService.getIpByRoomId(token, roomId);
		
	}
	
	@RequestMapping("notice")
	@ResponseBody
	public Result notice(@RequestBody Msg msg){
		Result result = new Result();
		
		if (null == msg || msg.getNoticeType() == null || StringUtils.isBlank(msg.getNoticeContent())) {
			result.setCode(ResultCode.PARAM_ERROR.code);
			result.setDesc(ResultCode.PARAM_ERROR.returnDesc);
			return result;
		}
		GameRequest request = new GameRequest();
		request.setMsg(msg);
		return gameService.notice(null, request);
		
	}
	public static void main(String[] args) {
		Msg msg = new Msg();
		msg.setFeedBack("投诉建议测试");
		msg.setMobilePhone("13006339011");
		msg.setFeedBackType(1);
		System.out.println(JsonUtil.toJson(msg));
	}
	
}
