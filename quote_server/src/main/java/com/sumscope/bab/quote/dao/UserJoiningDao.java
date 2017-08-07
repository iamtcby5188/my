package com.sumscope.bab.quote.dao;

import java.util.List;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.model.model.JoiningUserModel;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * 客户链接关系Dao接口
 * 本接口定义使用缓存，缓存应在系统启动时初始化。
 */
@CacheConfig(cacheNames={Constant.USER_JOINING_DAO_CACHE})
public interface UserJoiningDao {

	/**
	 * 获取某个用户的对应关联用户母账户列表
	 */
//	@Cacheable(value = Constant.USER_JOINING_DAO_CACHE,key = "#root.methodName+#userId")
	List<JoiningUserModel> getUserJoiningRelation(String userId);

	/**
	 *获取某个用户的子账户用户列表
     */
	@Cacheable(value = Constant.USER_JOINING_DAO_CACHE,key = "#root.methodName+#userId")
	List<JoiningUserModel> getJoinUserRelation(String userId);

	/**
	 * 设置用户关联列表信息。设置信息后需要向总线发布信息。
	 */
	@CacheEvict(allEntries = true)
	void setUserJoiningRelations(JoiningUserModel joiningUserModel) ;

	/**
	 *删除JoinUser
     */
	@CacheEvict(allEntries = true)
	void deleteJoiningUser(JoiningUserModel joiningUserModel);

	@Cacheable(value = Constant.USER_JOINING_DAO_CACHE,key = "#root.methodName")
	List<JoiningUserModel> getAllUserJoiningRelation();

	@CacheEvict(allEntries = true)
	void flushDaoCache();
}
