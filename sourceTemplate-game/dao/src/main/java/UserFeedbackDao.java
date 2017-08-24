package ${groupId}.dao.log;

import ${groupId}.domain.model.UserFeedbackModel;

public interface UserFeedbackDao {
	public long insertFeedback(UserFeedbackModel model);
}
