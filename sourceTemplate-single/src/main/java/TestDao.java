package ${groupId}.dao;

import java.util.List;
import java.util.Map;

import ${groupId}.domain.TestModel;

public interface TestDao {
	
	public List<TestModel> test(Map<String, Object> map);
	
}
