package com.pat.starter.oauth.server.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.pat.api.entity.PatUser;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.PatUserMapper;
import com.pat.starter.oauth.common.constant.PatOauthConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PatUserService
 *
 * @author chouway
 * @date 2022.03.29
 */
@Slf4j
public class PatUserService implements UserDetailsService {

    @Autowired
    private PatUserMapper patUserMapper;

    /**
     * 一天最大失败次数  （仅在通过验证码 并核验密码失败时 累积）
     */
    private final Integer MAX_FAIL_NUM_DAY = 20;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PatUser patUser = patUserMapper.createLambdaQuery().andEq(PatUser::getUserName, username).singleSimple();
        if (patUser == null) {
            throw new UsernameNotFoundException("not found user:" + username);
        }
        List<GrantedAuthority> grantedAuthorities = null;
        boolean isAccountNonLocked = this.isAccountNonLocked(patUser);
        if(isAccountNonLocked){
            grantedAuthorities = this.getGrantedAuthorities(patUser);
        }else{
            grantedAuthorities = new ArrayList<GrantedAuthority>();
        }
        User user = new User(patUser.getUserName(), patUser.getPassword(),true,true,true,isAccountNonLocked, grantedAuthorities);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recodePwdFail(String username){
        PatUser patUser = patUserMapper.createLambdaQuery().andEq(PatUser::getUserName, username).singleSimple();
        if(patUser == null){
            return;
        }
        Integer pwdFailCount = patUser.getPwdFailCount();

        String today = DateUtil.today();
        DateTime todayTime = DateUtil.parse(today, DatePattern.NORM_DATE_FORMAT);
        Date pwdFailDay = patUser.getPwdFailDay();
        if(pwdFailDay == null){
            pwdFailDay = todayTime;
            patUser.setPwdFailDay(new Date(todayTime.getTime()));
        }
        if(pwdFailCount == null){
            pwdFailCount = 0 ;
        }
        if(!todayTime.equals(pwdFailDay)){
            pwdFailCount = 0 ;
            patUser.setPwdFailDay(new Date(todayTime.getTime()));
        }
        patUser.setPwdFailCount(++pwdFailCount);
        patUserMapper.updateTemplateById(patUser);
    }

    /**
     * 如果此时密码当天已锁定 （可能存在密码攻击，用户正常的退出行为，可重置密码锁定）
     * @param username
     */

    @Transactional
    public void resetPwdLocked(String username){
        PatUser patUser = patUserMapper.createLambdaQuery().andEq(PatUser::getUserName, username).singleSimple();
        if(patUser == null){
            return;
        }
        String today = DateUtil.today();
        DateTime todayTime = DateUtil.parse(today, DatePattern.NORM_DATE_FORMAT);
        Date pwdFailDay = patUser.getPwdFailDay();
        Integer pwdFailCount = patUser.getPwdFailCount();
        if(todayTime.equals(pwdFailDay)&& pwdFailCount !=null && pwdFailCount > 0){
            patUser.setPwdFailCount(0);
            patUserMapper.updateTemplateById(patUser);
        }
    }

    /**
     * 账户是否密码登录锁定
     * 当当天密码错误达到指定次数时 锁定
     * @param patUser
     * @return
     */
    private boolean isAccountNonLocked(PatUser patUser) {
        boolean isAccountNonLocked = true;
        String today = DateUtil.today();
        DateTime todayTime = DateUtil.parse(today, DatePattern.NORM_DATE_FORMAT);
        Date pwdFailDay = patUser.getPwdFailDay();
        if(pwdFailDay == null){
            pwdFailDay = todayTime;
        }
        if (pwdFailDay.equals(todayTime)) {
            Integer pwdFailCount = patUser.getPwdFailCount();
            if (pwdFailCount > MAX_FAIL_NUM_DAY) {
                log.info("isAccountNonLocked-->pwdFailCount={},patUser.getUsername={}", pwdFailCount,patUser.getUserName());

                isAccountNonLocked = false;
            }
        }
        return isAccountNonLocked;
    }

    private List<GrantedAuthority> getGrantedAuthorities(PatUser patUser) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        String role = patUser.getRole();
        if (StringUtils.hasText(role)) {
            String[] temRoles = role.split(",");
            for (String temRole : temRoles) {
                grantedAuthorities.add(new SimpleGrantedAuthority(temRole));
            }
        } else {
            role = PatOauthConstant.ROLE_USER;
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        return grantedAuthorities;
    }


}
