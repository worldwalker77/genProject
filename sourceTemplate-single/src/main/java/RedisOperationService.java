package ${groupId}.common.utils.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ${groupId}.common.utils.JsonUtil;
import ${groupId}.domain.UserInfo;

@Component
public class RedisOperationService {
	@Autowired
	private JedisTemplate jedisTemplate;
	public UserInfo getUserInfo(String token){
		String temp = jedisTemplate.get(token);
		if (StringUtils.isEmpty(temp)) {
			return JsonUtil.toObject(temp, UserInfo.class);
		}
		return null;
	}
}
