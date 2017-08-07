package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.model.model.JoiningUserModel;
import java.util.List;

/**
 * 客户链接关系服务接口
 * 
 */
public interface UserJoiningService {

	/**
	 * 设置用户关联列表信息。设置信息后需要向总线发布信息。
	 */
	void setUserJoiningRelations(JoiningUserModel joiningUserModel,boolean delJoinUser);

	/**
	 * 获取某个用户的对应关联用户列表
	 */
	List<JoiningUserModel> getUserJoiningRelation(String userId);

	/**
	 * 获取某个用户的对应关联用户列表
	 */
	List<JoiningUserModel> getJoiningUserRelation(String userId);

	/**
	 * 更新数据库级缓存，删除当前所有缓存信息。
	 */
	void flushCache();

}
