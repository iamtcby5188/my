package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.FlushCacheEnum;
import com.sumscope.bab.quote.commons.exception.BABExceptionCode;
import com.sumscope.bab.quote.dao.UserJoiningDao;
import com.sumscope.bab.quote.externalinvoke.FlushCacheMsgSender;
import com.sumscope.bab.quote.model.model.JoiningUserModel;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserJoiningServiceImpl implements UserJoiningService {

    @Autowired
	private UserJoiningDao userJoiningDao;

	@Autowired
	private FlushCacheMsgSender flushCacheMsgSender;


	@Override
	public void setUserJoiningRelations(JoiningUserModel joiningUserModel,boolean delJoinUser) {
		if(delJoinUser){
			userJoiningDao.deleteJoiningUser(joiningUserModel);
			sendFlushCacheMsg();
		}else{
			//增加重复写入判断
			if (validateUserJoin(joiningUserModel.getUserId(), joiningUserModel.getJoinUserId())) {
				return;
			}
			userJoiningDao.setUserJoiningRelations(joiningUserModel);
			sendFlushCacheMsg();
		}

	}

	@Override
	public List<JoiningUserModel> getUserJoiningRelation(String userId) {
        return userJoiningDao.getUserJoiningRelation(userId);
	}

	@Override
	public List<JoiningUserModel> getJoiningUserRelation(String userId) {
		return userJoiningDao.getJoinUserRelation(userId);
	}

	private void sendFlushCacheMsg(){
		flushCacheMsgSender.sendMsgWithoutObject(FlushCacheEnum.USER_JOINING_FLUSH);

	}

	private boolean validateUserJoin(String userId, String childUserId) {
		List<JoiningUserModel> userJoiningRelation = userJoiningDao.getAllUserJoiningRelation();
		if(userJoiningRelation!=null && userJoiningRelation.size()>0){
			for(JoiningUserModel joinUser:userJoiningRelation){
				if(userId.equals(joinUser.getUserId()) && childUserId.equals(joinUser.getJoinUserId())){
					return true;
				}
				if(!userId.equals(joinUser.getUserId()) && childUserId.equals(joinUser.getJoinUserId())){
					throw new BusinessRuntimeException(BABExceptionCode.JOIN_USER_CHILD_ERROR, BABExceptionCode.JOIN_USER_CHILD_ERROR.getExceptionInfoCN());
				}
				if(childUserId.equals(joinUser.getUserId()) && !userId.equals(joinUser.getJoinUserId())){
					throw new BusinessRuntimeException(BABExceptionCode.JOIN_USER_PARENT_ERROR, BABExceptionCode.JOIN_USER_PARENT_ERROR.getExceptionInfoCN());
				}
			}
		}
		return false;
	}

	@Override
	public void flushCache() {
		userJoiningDao.flushDaoCache();
	}
}
