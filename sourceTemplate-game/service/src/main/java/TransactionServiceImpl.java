package ${groupId}.service.game.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${groupId}.common.utils.JsonUtil;
import ${groupId}.dao.log.RoomCardLogDao;
import ${groupId}.dao.user.UserDao;
import ${groupId}.domain.enums.RoomCardConsumeEnum;
import ${groupId}.domain.enums.RoomCardOperationEnum;
import ${groupId}.domain.model.RoomCardLogModel;
import ${groupId}.domain.model.UserModel;
import ${groupId}.domain.result.Result;
import ${groupId}.domain.result.ResultCode;
import ${groupId}.service.game.TransactionService;

public class TransactionServiceImpl implements TransactionService{
	
	private static final Log log = LogFactory.getLog(TransactionServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoomCardLogDao roomCardLogDao;
	
	@Transactional
	@Override
	public Result doDeductRoomCard(Long playerId, Integer payType, Integer totalGames, RoomCardOperationEnum operationEnum){
		Result result = new Result();
		RoomCardConsumeEnum consumeEnum = RoomCardConsumeEnum.getRoomCardConsumeEnum(payType, totalGames);
		Map<String, Object> map = new HashMap<String, Object>();
		int re = 0;
		int reTryCount = 1;
		UserModel userModel = null;
		do {
			userModel = userDao.getUserById(playerId);
			map.put("id", playerId);
			map.put("deductNum", consumeEnum.needRoomCardNum);
			map.put("roomCardNum", userModel.getRoomCardNum());
			map.put("updateTime", userModel.getUpdateTime());
			re = userDao.deductRoomCard(map);
			if (re == 1) {
				break;
			}
			reTryCount++;
			log.info("扣除房卡重试第" + reTryCount + "次");
		} while (reTryCount < 4);/**扣除房卡重试三次*/
		if (reTryCount == 4) {
			log.error(ResultCode.ROOM_CARD_DEDUCT_THREE_TIMES_FAIL.returnDesc + ", map:" + JsonUtil.toJson(map));
			result.setCode(ResultCode.ROOM_CARD_DEDUCT_THREE_TIMES_FAIL.code);
			result.setDesc(ResultCode.ROOM_CARD_DEDUCT_THREE_TIMES_FAIL.returnDesc);
			return result;
		}
		RoomCardLogModel roomCardLogModel = new RoomCardLogModel();
		roomCardLogModel.setPlayerId(playerId);
		roomCardLogModel.setDiffRoomCardNum(consumeEnum.needRoomCardNum);
		roomCardLogModel.setPreRoomCardNum(userModel.getRoomCardNum());
		Integer curRoomCardNum = userModel.getRoomCardNum() - consumeEnum.needRoomCardNum;
		roomCardLogModel.setCurRoomCardNum(curRoomCardNum);
		roomCardLogModel.setOperatorId(playerId);
		roomCardLogModel.setOperatorType(operationEnum.type);
		roomCardLogModel.setCreateTime(new Date());
		roomCardLogDao.insertRoomCardLog(roomCardLogModel);
		result.setData(curRoomCardNum);
		return result;
	}
}
