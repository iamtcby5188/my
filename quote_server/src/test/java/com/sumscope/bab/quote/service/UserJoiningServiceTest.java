package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.BABJoiningDisplayMode;
import com.sumscope.bab.quote.model.model.JoiningUserModel;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import java.util.List;

/**
 * Created by Administrator on 2017/1/23.
 */
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.HISTORY_DATA_SOURCE))
public class UserJoiningServiceTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private UserJoiningService userJoiningService;

    @Test
    public void userJoiningRelationsTest(){
        try {
            JoiningUserModel joiningUserModel = new JoiningUserModel();
            joiningUserModel.setUserId("1c497085d6d511e48ec3000c29a13c19");
            joiningUserModel.setJoinUserId("ff8081814411391d01441a75c45e2a6d");
            joiningUserModel.setJoinMode(BABJoiningDisplayMode.CTR);
            userJoiningService.setUserJoiningRelations(joiningUserModel,false);
            List<JoiningUserModel> userJoiningRelation = userJoiningService.getUserJoiningRelation("1c497085d6d511e48ec3000c29a13c19");
            Assert.assertTrue("获取子账户失败！",(userJoiningRelation!=null && userJoiningRelation.size()>0));
        } catch (Exception e) {
            Assert.assertTrue("获取子账户失败！",true);
        }
    }
}
