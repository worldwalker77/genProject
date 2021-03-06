package ${groupId}.service.game.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ${groupId}.common.cards.CardRule;
import ${groupId}.common.constant.Constant;
import ${groupId}.common.utils.JsonUtil;
import ${groupId}.common.utils.redis.JedisTemplate;
import ${groupId}.dao.log.RoomCardLogDao;
import ${groupId}.dao.log.UserFeedbackDao;
import ${groupId}.dao.log.UserRecordDao;
import ${groupId}.dao.user.UserDao;
import ${groupId}.domain.enums.DissolveStatusEnum;
import ${groupId}.domain.enums.MsgTypeEnum;
import ${groupId}.domain.enums.OnlineStatusEnum;
import ${groupId}.domain.enums.PayTypeEnum;
import ${groupId}.domain.enums.PlayerStatusEnum;
import ${groupId}.domain.enums.RoomCardConsumeEnum;
import ${groupId}.domain.enums.RoomCardOperationEnum;
import ${groupId}.domain.enums.RoomStatusEnum;
import ${groupId}.domain.game.Msg;
import ${groupId}.domain.game.PlayerInfo;
import ${groupId}.domain.game.RoomCardOperationFailInfo;
import ${groupId}.domain.game.RoomInfo;
import ${groupId}.domain.model.UserFeedbackModel;
import ${groupId}.domain.model.UserModel;
import ${groupId}.domain.model.UserRecordModel;
import ${groupId}.domain.result.Result;
import ${groupId}.domain.result.ResultCode;
import ${groupId}.service.game.TransactionService;
import ${groupId}.service.session.SessionContainer;


public class CommonService {
	
	private static final Log log = LogFactory.getLog(CommonService.class);
	
	@Autowired
	private UserRecordDao userRecordDao;
	@Autowired
	private UserFeedbackDao userFeedbackDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoomCardLogDao roomCardLogDao;
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private JedisTemplate jedisTemplate;
	/**
	 * 设置玩家状态
	 * @param playerList
	 * @param playerId
	 * @param statusEnum
	 */
	public void setPlayerStatus(List<PlayerInfo> playerList, Long playerId, PlayerStatusEnum statusEnum){
		for(PlayerInfo player : playerList){
			if (player.getPlayerId().equals(playerId)) {
				player.setStatus(statusEnum.status);
			}
		}
	}
	
	/**
	 * 设置玩家解散状态
	 * @param playerList
	 * @param playerId
	 * @param statusEnum
	 */
	public void setDissolveStatus(List<PlayerInfo> playerList, Long playerId, DissolveStatusEnum statusEnum){
		for(PlayerInfo player : playerList){
			if (player.getPlayerId().equals(playerId)) {
				player.setDissolveStatus(statusEnum.status);
			}
		}
	}
	
	/**
	 * 设置玩家在线状态
	 * @param playerList
	 * @param playerId
	 * @param statusEnum
	 */
	public void setOnlineStatus(List<PlayerInfo> playerList, Long playerId, OnlineStatusEnum onlineStatusEnum){
		for(PlayerInfo player : playerList){
			if (player.getPlayerId().equals(playerId)) {
				player.setOnlineStatus(onlineStatusEnum.status);
			}
		}
	}
	
	
	/**
	 * 设置玩家状态
	 * @param playerList
	 * @param playerId
	 * @param statusEnum
	 */
	public Integer getPlayerStatus(List<PlayerInfo> playerList, Long playerId){
		for(PlayerInfo player : playerList){
			if (player.getPlayerId().equals(playerId)) {
				return player.getStatus();
			}
		}
		return null;
	}
	
	public long genPlayerId(){
		int max=999999;
		int min=100000;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
		return s;
	}
	
	public long genRoomId(){
		int max=999999;
        int min=100000;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
		return s;
	}
	
	public Set<Long> getPlayerIdSet(List<PlayerInfo> playerList){
		Set<Long> set = new HashSet<Long>();
		for(PlayerInfo player : playerList){
			set.add(player.getPlayerId());
		}
		return set;
	}
	
	public String[] getPlayerIds(List<PlayerInfo> playerList){
		int size = playerList.size();
		String[] players = new String[size];
		for(int i = 0; i < size; i++){
			players[i] = String.valueOf(playerList.get(i).getPlayerId());
		}
		return players;
	}
	
	public Set<Long> getPlayerIdSetWithoutSelf(List<PlayerInfo> playerList, Long playerId){
		Set<Long> set = new HashSet<Long>();
		for(PlayerInfo player : playerList){
			if (!player.getPlayerId().equals(playerId)) {
				set.add(player.getPlayerId());
			}
		}
		return set;
	}
	
	/**
	 * 计算各玩家得分及赢家
	 * @param roomInfo
	 */
	public void calScoresAndWinner(RoomInfo roomInfo){
		List<PlayerInfo> playerList = roomInfo.getPlayerList();
		/**在活着的玩家里面找出赢家*/
		PlayerInfo curWinnerPlayer = CardRule.comparePlayerCards(getAlivePlayerList(playerList));
		roomInfo.setCurWinnerId(curWinnerPlayer.getPlayerId());
		/**设置下一小局的庄家*/
		roomInfo.setRoomBankerId(curWinnerPlayer.getPlayerId());
		/**计算每个玩家当前局得分*/
		for(PlayerInfo player : playerList){
			if (!player.getPlayerId().equals(curWinnerPlayer.getPlayerId())) {
				player.setCurScore(player.getCurScore() - player.getCurTotalStakeScore() - 1);
				curWinnerPlayer.setCurScore(curWinnerPlayer.getCurScore() + player.getCurTotalStakeScore() + 1);
				player.setLoseTimes((player.getLoseTimes()==null?0:player.getLoseTimes()) + 1);
				if (player.getCardType() > player.getMaxCardType()) {
					player.setMaxCardType(player.getCardType());
				}
			}else{
				curWinnerPlayer.setWinTimes((curWinnerPlayer.getWinTimes()==null?0:curWinnerPlayer.getWinTimes()) + 1);
				if (curWinnerPlayer.getCardType() > curWinnerPlayer.getMaxCardType()) {
					curWinnerPlayer.setMaxCardType(curWinnerPlayer.getCardType());
				}
			}
		}
		/**计算每个玩家总得分*/
		for(PlayerInfo player : playerList){
			player.setTotalScore((player.getTotalScore()==null?0:player.getTotalScore()) + player.getCurScore());
		}
		
		/**设置房间的总赢家*/
		Long totalWinnerId = playerList.get(0).getPlayerId();
		Integer maxTotalScore = playerList.get(0).getTotalScore()==null?0:playerList.get(0).getTotalScore();
		for(PlayerInfo player : playerList){
			Integer tempTotalScore = player.getTotalScore()==null?0:player.getTotalScore();
			if (tempTotalScore > maxTotalScore) {
				maxTotalScore = tempTotalScore;
				totalWinnerId = player.getPlayerId();
			}
		}
		roomInfo.setTotalWinnerId(totalWinnerId);
		/**如果当前局数小于总局数，则设置为当前局结束*/
		if (roomInfo.getCurGame() < roomInfo.getTotalGames()) {
			roomInfo.setStatus(RoomStatusEnum.curGameOver.status);
		}else{/**如果当前局数等于总局数，则设置为一圈结束*/
			roomInfo.setStatus(RoomStatusEnum.totalGameOver.status);
			addUserRecord(roomInfo.getRoomId(), playerList);
		}
		/**如果是第一局结束，则扣除房卡;扣除房卡异常不影响游戏进行，会将异常数据放入redis中，由定时任务进行补偿扣除*/
		if (roomInfo.getCurGame() == 1) {
			//TODO
//			deductRoomCard(roomInfo);
		}
	}
	
	public void deductRoomCard(RoomInfo roomInfo){
		Result result = new Result();
		result.setMsgType(MsgTypeEnum.roomCardNumUpdate.msgType);
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		List<PlayerInfo> playerList = roomInfo.getPlayerList();
		Integer payType = roomInfo.getPayType();
		Integer totalGames = roomInfo.getTotalGames();
		Result re = null;
		if (PayTypeEnum.roomOwnerPay.type.equals(roomInfo.getPayType())) {/**房主付费*/
			try {
				re = transactionService.doDeductRoomCard(roomInfo.getRoomOwnerId(), payType, totalGames, RoomCardOperationEnum.consumeCard);
				if (ResultCode.SUCCESS.code == re.getCode()) {
					data.put("playerId", roomInfo.getRoomOwnerId());
					data.put("roomCardNum", (Integer)re.getData());
					SessionContainer.sendTextMsgByPlayerId(roomInfo.getRoomOwnerId(), result);
				}else{
					addRoomCardOperationFailInfoToRedis(new RoomCardOperationFailInfo( roomInfo.getRoomOwnerId(), 
																					payType, 
																					totalGames, 
																					RoomCardOperationEnum.consumeCard.type));
				}
				
			} catch (Exception e) {
				log.error(ResultCode.ROOM_CARD_DEDUCT_EXCEPTION.returnDesc 
						 + ", roomOwnerId:" + roomInfo.getRoomOwnerId() 
						 + ", payType:" + payType 
						 + ", totalGames:" + totalGames, e);
			}
		}else{/**AA制付费*/
			for(PlayerInfo player : playerList){
				try {
					transactionService.doDeductRoomCard(player.getPlayerId(), payType, totalGames, RoomCardOperationEnum.consumeCard);
					if (ResultCode.SUCCESS.code == re.getCode()) {
						data.put("playerId", player.getPlayerId());
						data.put("roomCardNum", (Integer)re.getData());
						SessionContainer.sendTextMsgByPlayerId(roomInfo.getRoomOwnerId(), result);
					}else{
						addRoomCardOperationFailInfoToRedis(new RoomCardOperationFailInfo( player.getPlayerId(), 
																						payType, 
																						totalGames, 
																						RoomCardOperationEnum.consumeCard.type));
					}
				} catch (Exception e) {
					log.error(ResultCode.ROOM_CARD_DEDUCT_EXCEPTION.returnDesc 
							 + ", roomOwnerId:" + roomInfo.getRoomOwnerId() 
							 + ", payType:" + payType 
							 + ", totalGames:" + totalGames, e);
				}
			}
		}
	}
	
	public void addRoomCardOperationFailInfoToRedis(RoomCardOperationFailInfo failInfo){
		try {
			jedisTemplate.lpush(Constant.jinhuaRoomCardOperationFailList, JsonUtil.toJson(failInfo));
		} catch (Exception e) {
			log.error("向redis添加房卡操作异常数据异常, failInfo:" + JsonUtil.toJson(failInfo), e);
			/**发送异常数据邮件预警，后续优化*/
		}
	}
	
	public Long getNextOperatePlayerId(List<PlayerInfo> playerList, Long curPlayerId){
		
		List<PlayerInfo> alivePlayerList = getAlivePlayerList(playerList);
		int size = alivePlayerList.size();
		Long nextOperatePlayerId = null;
		for(int i = 0; i < size; i++ ){
			PlayerInfo player = alivePlayerList.get(i);
			if (player.getPlayerId().equals(curPlayerId)) {
				if (i == size - 1) {
					nextOperatePlayerId = alivePlayerList.get(0).getPlayerId();
					break;
				}else{
					nextOperatePlayerId = alivePlayerList.get(i + 1).getPlayerId();
					break;
				}
			}
		}
		return nextOperatePlayerId;
	}
	
	public List<PlayerInfo> getAlivePlayerList(List<PlayerInfo> playerList){
		List<PlayerInfo> alivePlayerList = new ArrayList<PlayerInfo>();
		for(PlayerInfo player : playerList){
			if (player.getStatus().equals(PlayerStatusEnum.notWatch.status) || player.getStatus().equals(PlayerStatusEnum.watch.status)) {
				alivePlayerList.add(player);
			}
		}
		return alivePlayerList;
	}
	
	public int getAlivePlayerCount(List<PlayerInfo> playerList){
		int alivePlayerCount = 0;
		for(PlayerInfo player : playerList){
			if (player.getStatus().equals(PlayerStatusEnum.notWatch.status) || player.getStatus().equals(PlayerStatusEnum.watch.status)) {
				alivePlayerCount++;
			}
		}
		return alivePlayerCount;
	}
	
	public boolean isExistPlayerInRoom(Long playerId, List<PlayerInfo> playerList){
		for(PlayerInfo player : playerList){
			if (player.getPlayerId().equals(playerId)) {
				return true;
			}
		}
		return false;
	}
	
	public void addUserRecord(Long roomId, List<PlayerInfo> playerList){
		if (CollectionUtils.isEmpty(playerList)) {
			return;
		}
		List<String> nickNameList = new ArrayList<String>();
		for(PlayerInfo player : playerList){
			nickNameList.add(player.getNickName());
		}
		String nickNames = JsonUtil.toJson(nickNameList);
		List<UserRecordModel> modelList = new ArrayList<UserRecordModel>();
		Date createTime = new Date();
		for(PlayerInfo player : playerList){
			UserRecordModel model = new UserRecordModel();
			model.setPlayerId(player.getPlayerId());
			model.setRoomId(roomId);
			model.setScore(player.getTotalScore());
			model.setNickNames(nickNames);
			model.setCreateTime(createTime);
			modelList.add(model);
		}
		try {
			userRecordDao.batchInsertRecord(modelList);
		} catch (Exception e) {
			log.error("插入玩家得分记录异常，roomId:" + roomId + ", playerList:" + JsonUtil.toJson(playerList), e);
		}
	}
	
	public List<UserRecordModel> getUserRecord(Long playerId){
		return userRecordDao.getUserRecord(playerId);
	}
	
	public void addUserFeedback(Msg msg){
		UserFeedbackModel model = new UserFeedbackModel();
		model.setPlayerId(msg.getPlayerId());
		model.setMobilePhone(msg.getMobilePhone());
		model.setFeedBack(msg.getFeedBack());
		model.setType(msg.getFeedBackType());
		userFeedbackDao.insertFeedback(model);
	}
	/**
	 * 玩家房卡校验
	 * @param playerId
	 * @param payType
	 * @param totalGames
	 * @return
	 */
	public ResultCode roomCardCheck(Long playerId, Integer payType, Integer totalGames){
		
		RoomCardConsumeEnum consumeEnum = RoomCardConsumeEnum.getRoomCardConsumeEnum(payType, totalGames);
		if (null == consumeEnum) {
			return ResultCode.PARAM_ERROR;
		}
		UserModel userModel = userDao.getUserById(playerId);
		Integer roomCardNum = userModel.getRoomCardNum();
		if (roomCardNum < consumeEnum.needRoomCardNum) {
			return ResultCode.ROOM_CARD_NOT_ENOUGH;
		}
		return ResultCode.SUCCESS;
	}
	
	
}
