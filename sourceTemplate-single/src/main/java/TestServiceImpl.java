package ${groupId}.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${groupId}.common.utils.redis.JedisTemplate;
import ${groupId}.dao.TestDao;
import ${groupId}.domain.TestModel;
@Service
public class TestServiceImpl implements TestService{
	@Autowired
	private TestDao testDao;
	@Autowired
	private JedisTemplate jedisTemplate;
	@Override
	public List<TestModel> getTest() {
		
		System.out.println(jedisTemplate.get("ddddd"));
		List<TestModel> list = testDao.test(new HashMap<String, Object>());
		return list;
	}
	
}
