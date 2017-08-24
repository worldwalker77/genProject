package ${groupId}.service.game.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import ${groupId}.common.constant.Constant;
import ${groupId}.common.utils.JsonUtil;
import ${groupId}.domain.enums.MsgTypeEnum;
import ${groupId}.domain.game.PlayerInfo;
import ${groupId}.domain.game.RoomInfo;
import ${groupId}.domain.result.Result;
import ${groupId}.service.game.impl.CommonService;
import ${groupId}.service.session.SessionContainer;


public class OfflinePlayerCleanJob extends SingleServerJobByRedis{
	 
	@Autowired
	private CommonService commonService;
	/**
	 * 清除游戏房间中离线超过20分钟的玩家信息及房间信息
	 */
	@Override
	public void execute() {
		Map<String, String> map = jedisTemplate.hgetAll(Constant.jinhuaOfflinePlayerIdTimeMap);
		if (map != null) {
			Set<Entry<String, String>> entrySet = map.entrySet();
			for(Entry<String, String> entry : entrySet){
				String playerIdStr = entry.getKey();
				String value = entry.getValue();
				String[] arr = value.split("_");
				String roomIdStr = arr[0];
				Long offlineTime = Long.valueOf(arr[1]);
				Long diffTime = System.currentTimeMillis() - offlineTime;
				/**离线时间超过20分钟，则删除玩家及房间信息*/
				if (diffTime > 20*60*1000L) {
					System.out.println("playerId : " + playerIdStr);
					System.out.println("roomId : " + roomIdStr);
					String[] playerIds = new String[1];
					playerIds[0] = playerIdStr;
					RoomInfo roomInfo = SessionContainer.getRoomInfoFromRedis(Long.valueOf(roomIdStr));
					/**如果无房间信息，则说明可能其他离线玩家已经将房间删除，不需要再推送消息给其他玩家*/
					if (null == roomInfo) {
						SessionContainer.cleanPlayerAndRoomInfo(Long.valueOf(roomIdStr), playerIds);
						return;
					}
					System.out.println("roomInfo : " + JsonUtil.toJson(roomInfo));
					List<PlayerInfo> playerList = roomInfo.getPlayerList();
					Result result = new Result();
					result.setMsgType(MsgTypeEnum.dissolveRoomCausedByOffline.msgType);
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("playerId", playerIdStr);
					result.setData(data);
					SessionContainer.sendTextMsgByPlayerIdSet(Long.valueOf(roomIdStr), commonService.getPlayerIdSetWithoutSelf(playerList, Long.valueOf(playerIdStr)), result);
					SessionContainer.cleanPlayerAndRoomInfo(Long.valueOf(roomIdStr), playerIds);
				}
			}
		}
	}
	
}
