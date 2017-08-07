package com.sumscope.bab.quote.dao;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.model.model.JoiningUserModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 * 子账户
 */
@Repository("UserJoiningDao")
public class UserJoiningDaoImpl implements UserJoiningDao {
    private static final String SEARCH_JOINING_USER = "com.sumscope.bab.quote.mapping.UserJoiningMapper.queryJoiningUser";
    private static final String SEARCH_USER_JOINING = "com.sumscope.bab.quote.mapping.UserJoiningMapper.queryUserJoin";

    private static final String INSERT_JOINING_USER = "com.sumscope.bab.quote.mapping.UserJoiningMapper.insertJoiningUser";
    private static final String DEL_JOINING_USER = "com.sumscope.bab.quote.mapping.UserJoiningMapper.deleteJoiningUser";
    private static final String SEARCH_ALL_JOINING_USER = "com.sumscope.bab.quote.mapping.UserJoiningMapper.queryAllJoiningUser";

    @Autowired
    @Qualifier(value = Constant.BUSINESS_SQL_SESSION_TEMPLATE)
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<JoiningUserModel> getUserJoiningRelation(String userId) {
        return sqlSessionTemplate.selectList(SEARCH_JOINING_USER,userId);
    }

    @Override
    public List<JoiningUserModel> getJoinUserRelation(String userId) {
        return sqlSessionTemplate.selectList(SEARCH_USER_JOINING,userId);
    }

    @Override
    public void setUserJoiningRelations(JoiningUserModel joiningUserModel) {
        sqlSessionTemplate.insert(INSERT_JOINING_USER,joiningUserModel);
    }

    @Override
    public void deleteJoiningUser(JoiningUserModel joiningUserModel) {
        sqlSessionTemplate.delete(DEL_JOINING_USER,joiningUserModel);
    }

    @Override
    public List<JoiningUserModel> getAllUserJoiningRelation() {
        return sqlSessionTemplate.selectList(SEARCH_ALL_JOINING_USER);
    }

    @Override
    public void flushDaoCache() {

    }

}
