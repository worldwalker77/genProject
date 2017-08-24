package ${groupId}.dao.log;

import java.util.List;

import ${groupId}.domain.model.UserRecordModel;

public interface UserRecordDao {
	
	public long insertRecord(UserRecordModel model);
	
	public long batchInsertRecord(List<UserRecordModel> modelList);
	
	public List<UserRecordModel> getUserRecord(Long playerId);
	
}
