package com.tsingda.smd.test;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tsingda.smd.config.AppConfig;
import com.tsingda.smd.config.MvcConfig;
import com.tsingda.smd.dao.mapper.UserMapper;
import com.tsingda.smd.model.User;
import com.tsingda.smd.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MvcConfig.class })
@WebAppConfiguration
@Transactional("transactionManager")
@Rollback(true)
@ActiveProfiles({ "dev" })
public class BaseJunit4Test extends AbstractTransactionalJUnit4SpringContextTests {

    private static final Logger logger = LoggerFactory.getLogger(BaseJunit4Test.class);

    @Resource
    UserMapper userMapper;

    @Before
    public void beforeTest() {
        logger.info("测试方法开始时间 ：{}", new Date());
    }

    @After
    public void afterTest() {
        logger.info("测试方法结束时间 ：{}", new Date());
    }

    @Test
    public void insertUser() throws JsonProcessingException {
        int countBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "user");
        logger.info("total count in user table : {}", countBefore);
        User u = new User();
        String ids = UUID.randomUUID().toString().replace("-", "");
        u.setIds(ids);
        u.setName("test");
        u.setAge(25);
        u.setAddress("清大世纪大厦");
        userMapper.insert(u);

        User ur = userMapper.selectByPrimaryKey(ids);
        logger.info(JsonUtil.stringify(ur));

        int countAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "user");
        logger.info("total count in user table : {}", countAfter);
    }
}
