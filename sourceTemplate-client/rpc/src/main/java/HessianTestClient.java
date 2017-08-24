package ${groupId}.rpc.hessiantest;

import java.util.Map;

public interface HessianTestClient {
	public Map<String, Object> getHessianTestResult(Integer type);
}
