package com.pat.poetry.oauth.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.pat.api.entity.PatUser;
import com.pat.api.mapper.PatUserMapper;
import com.pat.poetry.oauth.PoetOauthTest;
import com.pat.starter.oauth.server.service.PatUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * UserDetailServiceTest
 *
 * @author chouway
 * @date 2022.04.04
 */
public class UserDetailServiceTest extends PoetOauthTest {

    @Autowired
    private PatUserMapper patUserMapper;
    @Test
    public void get(){
        PatUser patUser = patUserMapper.createLambdaQuery().andEq(PatUser::getUserName, "test").singleSimple();
        log.info("get-->patUser={}", JSON.toJSONString(patUser));
        String today = DateUtil.today();
        DateTime dateTime = DateUtil.parse(today, DatePattern.NORM_DATE_FORMAT);
        log.info("get-->dateTime={}", JSON.toJSONString(dateTime));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void recodePwd(){

        PatUser patUser = patUserMapper.createLambdaQuery().andEq(PatUser::getUserName, "test").singleSimple();
        log.info("get-->patUser={}", JSON.toJSONString(patUser));
        String today = DateUtil.today();
        DateTime dateTime = DateUtil.parse(today, DatePattern.NORM_DATE_FORMAT);
        log.info("get-->dateTime={}", JSON.toJSONString(dateTime));

        patUser.setPwdFailDay(new Date(dateTime.getTime()));
        patUserMapper.updateTemplateById(patUser);
        this.get();
    }
}
