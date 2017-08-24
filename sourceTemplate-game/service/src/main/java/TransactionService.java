package ${groupId}.service.game;

import ${groupId}.domain.enums.RoomCardOperationEnum;
import ${groupId}.domain.result.Result;

public interface TransactionService {
	public Result doDeductRoomCard(Long playerId, Integer payType, Integer totalGames, RoomCardOperationEnum operationEnum);
}
