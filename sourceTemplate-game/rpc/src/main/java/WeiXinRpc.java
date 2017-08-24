package ${groupId}.rpc.weixin;

import ${groupId}.domain.weixin.WeiXinUserInfo;

public interface WeiXinRpc {
	public WeiXinUserInfo getWeiXinUserInfo(String code);
}
