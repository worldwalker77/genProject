package ${groupId}.web.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ${groupId}.common.utils.redis.JedisTemplate;
import ${groupId}.domain.result.Result;

@Controller
@RequestMapping("redis/")
public class RedisController {
	
	@Autowired
	private JedisTemplate jedisTemplate;
	
	@RequestMapping("get")
	@ResponseBody
	public Result get(String key){
		Result result = new Result();
		result.setData(jedisTemplate.get(key));
		return result;
	}
	
	@RequestMapping("set")
	@ResponseBody
	public Result set(String key, String value){
		Result result = new Result();
		jedisTemplate.set(key, value);
		result.setData(jedisTemplate.get(key));
		return result;
	}
	
	@RequestMapping("hgetAll")
	@ResponseBody
	public Result hgetAll(String key){
		Result result = new Result();
		result.setData(jedisTemplate.hgetAll(key));
		return result;
	}
	
	@RequestMapping("del")
	@ResponseBody
	public Result del(String key){
		Result result = new Result();
		jedisTemplate.del(key);
		return result;
	}
	
	@RequestMapping("hdel")
	@ResponseBody
	public Result hdel(String key, String field){
		Result result = new Result();
		jedisTemplate.hdel(key, field);
		return result;
	}
}
