package ${groupId}.service.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import ${groupId}.common.constant.Constant;
import ${groupId}.common.roomlocks.RoomLockContainer;
import ${groupId}.common.utils.JsonUtil;
import ${groupId}.common.utils.redis.JedisTemplate;
import ${groupId}.common.utils.redis.JedisTemplate.JedisAction;
import ${groupId}.domain.enums.GameTypeEnum;
import ${groupId}.domain.enums.MsgTypeEnum;
import ${groupId}.domain.enums.OnlineStatusEnum;
import ${groupId}.domain.game.GameRequest;
import ${groupId}.domain.game.PlayerInfo;
import ${groupId}.domain.game.RoomInfo;
import ${groupId}.domain.game.UserInfo;
import ${groupId}.domain.result.Result;
import ${groupId}.domain.result.ResultCode;
import ${groupId}.service.game.impl.CommonService;

@Component
public class SessionContainer {
	
	private static final Log log = LogFactory.getLog(SessionContainer.class);
	
	private static JedisTemplate jedisTemplate;
	
	private static CommonService commonService;
	
	private static Map<Long, Channel> sessionMap = new ConcurrentHashMap<Long, Channel>();
	
	public static void addChannel(ChannelHandlerContext ctx, Long playerId){
		sessionMap.put(playerId, ctx.channel());
	}
	
	public static Channel getChannel(Long playerId){
		return sessionMap.get(playerId);
	}
	
	public static boolean sendTextMsgByPlayerId(Long playerId, Result result){
		if ("1".equals(jedisTemplate.get(Constant.jinhuaLogInfoFuse))) {
			log.info("返回 ：" + JsonUtil.toJson(result));
		}
		Channel channel = getChannel(playerId);
		if (null != channel) {
			try {
				channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(result)));
			} catch (Exception e) {
				log.error("sendTextMsgByPlayerId error, playerId: " + playerId + ", result : " + JsonUtil.toJson(result), e);
				return false;
			}
			return true;
		}
		return false;
	}
	
	public static boolean sendTextMsgByPlayerId(Long roomId, Long playerId, Result result){
		if ("1".equals(jedisTemplate.get(Constant.jinhuaLogInfoFuse))) {
			log.info("返回 ：" + JsonUtil.toJson(result));
		}
		
		long msgId = 0;
		Channel channel = getChannel(playerId);
		if (null != channel) {
			/**从redis通过自增获取当前房间的msgId，这里不做异常捕获，交给MsgProcessDispatcher层进行统一捕获，*/
			msgId = jedisTemplate.hincrBy(Constant.jinhuaRoomIdMsgIdMap, String.valueOf(roomId), 1);
			result.setMsgId(msgId);
			try {
				channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(result)));
			} catch (Exception e) {
				log.error("sendTextMsgByPlayerId error, playerId: " + playerId + ", result : " + JsonUtil.toJson(result), e);
				return false;
			}
			return true;
		}
		return false;
	}
	
	public static long sendTextMsgByPlayerIdSet(Long roomId, Set<Long> playerIdSet, Result result){
		/**从redis通过自增获取当前房间的msgId，这里不做异常捕获，交给MsgProcessDispatcher层进行统一捕获，*/
		long msgId = jedisTemplate.hincrBy(Constant.jinhuaRoomIdMsgIdMap, String.valueOf(roomId), 1);
		result.setMsgId(msgId);
		if ("1".equals(jedisTemplate.get(Constant.jinhuaLogInfoFuse))) {
			log.info("返回 ：" + JsonUtil.toJson(result));
		}
		
		for(Long playerId : playerIdSet){
			Channel channel = getChannel(playerId);
			if (null != channel) {
				try {
					channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(result)));
				} catch (Exception e) {
					log.error("sendTextMsgByPlayerIdList error, playerId: " + playerId + ", result : " + JsonUtil.toJson(result), e);
				}
			}
		}
		return msgId;
	}
	
	public static void sendTextMsgByPlayerIdList(List<Long> playerIdList, Result result){
		if ("1".equals(jedisTemplate.get(Constant.jinhuaLogInfoFuse))) {
			log.info("返回 ：" + JsonUtil.toJson(result));
		}
		
		for(Long playerId : playerIdList){
			Channel channel = getChannel(playerId);
			if (null != channel) {
				try {
					channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(result)));
				} catch (Exception e) {
					log.error("sendTextMsgByPlayerIdList error, playerId: " + playerId + ", result : " + JsonUtil.toJson(result), e);
				}
			}
		}
	}
	
	public static void sendTextMsgToAllPlayer(Result result){
		if ("1".equals(jedisTemplate.get(Constant.jinhuaLogInfoFuse))) {
			log.info("返回 ：" + JsonUtil.toJson(result));
		}
		Set<Entry<Long, Channel>> set = sessionMap.entrySet();
		for(Entry<Long, Channel> entry : set){
			Long playerId = entry.getKey();
			Channel channel = entry.getValue();
			if (null != channel) {
				try {
					channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(result)));
				} catch (Exception e) {
					log.error("sendTextMsgByPlayerIdList error, playerId: " + playerId + ", result : " + JsonUtil.toJson(result), e);
				}
			}
		}
	}
	
	public static void sendTextMsg(ChannelHandlerContext ctx, Result result){
		if ("1".equals(jedisTemplate.get(Constant.jinhuaLogInfoFuse)) && !MsgTypeEnum.heartBeat.equals(MsgTypeEnum.getMsgTypeEnumByType(result.getMsgType()))) {
			log.info("返回 ：" + JsonUtil.toJson(result));
		}
		
		try {
			ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(result)));
		} catch (Exception e) {
			log.error("sendTextMsgByPlayerId error, result : " + JsonUtil.toJson(result), e);
		}
	}
	
	 public static Result sendErrorMsg(ChannelHandlerContext ctx, ResultCode resultCode, Integer msgType, GameRequest request){
		log.error(resultCode.insideDesc + ", request:" + JsonUtil.toJson(request));
		Result result = new Result(resultCode.code, resultCode.returnDesc, msgType, GameTypeEnum.jinhua.gameType);
		if ("1".equals(jedisTemplate.get(Constant.jinhuaLogInfoFuse))) {
			log.info("返回 ：" + JsonUtil.toJson(result));
		}
		
		try {
			sendTextMsg(ctx, result);
		} catch (Exception e) {
			log.error("sendErrorMsg error, result : " + JsonUtil.toJson(result), e);
		}
		return result;
	}
	
	public static void removeSession(ChannelHandlerContext ctx){
		if (sessionMap.isEmpty()) {
			return;
		}
		Long playerId = null;
		Set<Entry<Long, Channel>> entrySet = sessionMap.entrySet();
		for(Entry<Long, Channel> entry : entrySet){
			if (entry.getValue().equals(ctx.channel())) {
				playerId = entry.getKey();
				break;
			}
		}
		if (playerId != null) {
			sessionMap.remove(playerId);
			String roomId = jedisTemplate.hget(Constant.jinhuaPlayerIdRoomIdMap, String.valueOf(playerId));
			System.out.println("====>>>removeSession,玩家断线，playerId:" + playerId + "roomId:" + roomId);
			/**如果此掉线的playerId有房间信息*/
			if (StringUtils.isNotBlank(roomId)) {
				/**设置离线playerId与roomId的映射关系*/
				jedisTemplate.hset(Constant.jinhuaOfflinePlayerIdTimeMap, String.valueOf(playerId), roomId +"_" + System.currentTimeMillis() );
				/**设置当前玩家为离线状态并通知其他玩家此玩家离线*/
				RoomInfo roomInfo = getRoomInfoFromRedis(Long.valueOf(roomId));
				List<PlayerInfo> playerList = roomInfo.getPlayerList();
				commonService.setOnlineStatus(playerList, playerId, OnlineStatusEnum.offline);
				setRoomInfoToRedis(Long.valueOf(roomId), roomInfo);
				
				Result result = new Result();
				result.setMsgType(MsgTypeEnum.offlineNotice.msgType);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("playerId", playerId);
				result.setData(data);
				sendTextMsgByPlayerIdSet(Long.valueOf(roomId), commonService.getPlayerIdSetWithoutSelf(playerList, playerId), result);
			}
		}
	}
	
	
	public static void setUserInfoToRedis(String token, UserInfo userInfo){
		jedisTemplate.setex(token, JsonUtil.toJson(userInfo), 3600*2);
	}
	
	public static UserInfo getUserInfoFromRedis(String token){
		String temp = jedisTemplate.get(token);
		if (StringUtils.isNotBlank(temp)) {
			return JsonUtil.toObject(temp, UserInfo.class);
		}
		return null;
	}
	
	public static void expireUserInfo(String token){
		jedisTemplate.expire(token, 3600*2);
	}

	public static Map<Long, Channel> getSessionMap() {
		return sessionMap;
	}
	
	public static void cleanPlayerAndRoomInfo(Long roomId, String[] playerIds){
		jedisTemplate.hdel(Constant.jinhuaRoomIdRoomInfoMap, String.valueOf(roomId));
		jedisTemplate.hdel(Constant.jinhuaRoomIdMsgIdMap, String.valueOf(roomId));
		jedisTemplate.hdel(Constant.jinhuaPlayerIdRoomIdMap, playerIds);
		jedisTemplate.hdel(Constant.jinhuaOfflinePlayerIdTimeMap, playerIds);
		RoomLockContainer.delLockByRoomId(roomId);
	}
	
	public static RoomInfo getRoomInfoFromRedis(Long roomId){
		String roomInfoStr = jedisTemplate.hget(Constant.jinhuaRoomIdRoomInfoMap, String.valueOf(roomId));
		if (StringUtils.isBlank(roomInfoStr)) {
			return null;
		}
		return JsonUtil.toObject(roomInfoStr, RoomInfo.class);
	}
	
	public static void setRoomInfoToRedis(Long roomId, RoomInfo roomInfo){
		jedisTemplate.hset(Constant.jinhuaRoomIdRoomInfoMap, String.valueOf(roomId),JsonUtil.toJson(roomInfo));
	}

	
	 @Autowired(required = true)
	public void setJedisTemplate(JedisTemplate jedisTemplate) {
		SessionContainer.jedisTemplate = jedisTemplate;
	}
	 @Autowired(required = true)
	public void setCommonService(CommonService commonService) {
		SessionContainer.commonService = commonService;
	}

	public static synchronized boolean  isRunning(Long roomId) {
        try {
            long ttl = getKeyTtl(roomId);

            if (ttl > 30 || ttl < 0) { //TTL 可能返回-1 -2
                unlock(roomId); // 防呆,防止失效时间没设置成功
            }

            if (jedisTemplate.setnx(getRoomLockKey(roomId), "1")) {
                expire(getRoomLockKey(roomId), 30);
                
                return true;
            }
            return false;
        } catch (Exception e) {
        	log.error(getRoomLockKey(roomId) + " error",e);
            return false;
        }
    }
	 
	 public static  void unlock(Long roomId) {
	        try {
	        	String s=jedisTemplate.get(String.valueOf(roomId));
	            if (s != null) {
	                jedisTemplate.del(Constant.jinhuaRoomLockPrefix + roomId);

	            }
	        } catch (Exception e) {
	        	log.error("unlock error",e);
	            jedisTemplate.del(Constant.jinhuaRoomLockPrefix + roomId);
	        }
	    }
	 public static  Boolean expire(final String key, final int timeOut) {
	        return jedisTemplate.execute(new JedisTemplate.JedisAction<Boolean>() {

	            @Override
	            public Boolean action(Jedis jedis) {
	                int maxRetry = 5;
	                for (int i = 0; i < maxRetry; i++) {

	                    if (jedis.expire(key, timeOut) == 1l) {
	                        return true;

	                    }
	                }
	                log.error(" expire retry 5 times failure");
	                return false;
	            }
	        });
	    }
	 
	 public static  long getKeyTtl(final Long roomId ) {
			return jedisTemplate.execute(new JedisAction<Long>() {

				@Override
				public Long action(Jedis jedis) {
					return jedis.ttl(getRoomLockKey(roomId));
				}
			});
		}
	 
	 public static  String getRoomLockKey(Long roomId){
		 return Constant.jinhuaRoomLockPrefix + roomId;
	 }

	public static void main(String[] args) {
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("1", 1L);
		map.put("2", 2L);
		map.put("3", 3L);
		Set<Entry<String, Long>> set = map.entrySet();
		for(Entry<String, Long> entry : set){
			if (entry.getValue().equals(1L)) {
				map.remove(entry.getKey());
			}
		}
	}

}
