package ${groupId}.service.game.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ${groupId}.common.constant.Constant;
import ${groupId}.common.utils.JsonUtil;
import ${groupId}.domain.enums.MsgTypeEnum;
import ${groupId}.domain.enums.RoomCardOperationEnum;
import ${groupId}.domain.game.RoomCardOperationFailInfo;
import ${groupId}.domain.result.Result;
import ${groupId}.domain.result.ResultCode;
import ${groupId}.service.game.TransactionService;
import ${groupId}.service.game.impl.CommonService;
import ${groupId}.service.session.SessionContainer;


public class RoomCardOperationFailProcessJob extends SingleServerJobByRedis {
	
	private final static Log log = LogFactory.getLog(RoomCardOperationFailProcessJob.class);
	
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private CommonService commonService;
	
	@Override
	public void execute() {
		Result result = new Result();
		result.setMsgType(MsgTypeEnum.roomCardNumUpdate.msgType);
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		boolean flag = true;
		RoomCardOperationFailInfo failInfo = null;
		String failInfoStr = null;
		Result re = null;
		do {
			failInfoStr = jedisTemplate.rpop(Constant.jinhuaRoomCardOperationFailList);
			flag = StringUtils.isNotBlank(failInfoStr);
			if (flag) {
				failInfo = JsonUtil.toObject(failInfoStr, RoomCardOperationFailInfo.class);
				if (RoomCardOperationEnum.consumeCard.type.equals(failInfo.getRoomCardOperationType())) {
					try {
						re = transactionService.doDeductRoomCard(failInfo.getPlayerId(), failInfo.getPayType(), failInfo.getTotalGames(), RoomCardOperationEnum.jobCompensateConsumeCard);
						if (ResultCode.SUCCESS.code == re.getCode()) {
							data.put("playerId", failInfo.getPlayerId());
							data.put("roomCardNum", (Integer)re.getData());
							SessionContainer.sendTextMsgByPlayerId(failInfo.getPlayerId(), result);
						}else{/**如果扣除失败则重新加入redis*/
							commonService.addRoomCardOperationFailInfoToRedis(new RoomCardOperationFailInfo( failInfo.getPlayerId(), 
																											 failInfo.getPayType(), 
																											 failInfo.getTotalGames(), 
																							RoomCardOperationEnum.consumeCard.type));
						}
					} catch (Exception e) {
						log.error(ResultCode.ROOM_CARD_DEDUCT_EXCEPTION.returnDesc 
								 + ", playerId:" + failInfo.getPlayerId() 
								 + ", payType:" + failInfo.getPayType() 
								 + ", totalGames:" + failInfo.getTotalGames(), e);
						commonService.addRoomCardOperationFailInfoToRedis(new RoomCardOperationFailInfo( failInfo.getPlayerId(), 
								 failInfo.getPayType(), 
								 failInfo.getTotalGames(), 
								 RoomCardOperationEnum.consumeCard.type));
						break;
					}
				}
			}
		} while (flag);
	}

}
