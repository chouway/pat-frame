package com.pat.starter.oauth.server.service;

import com.pat.api.entity.PatUser;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.PatUserMapper;
import com.pat.starter.oauth.common.constant.PatOauthConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * PatUserService
 *
 * @author chouway
 * @date 2022.03.29
 */
public class PatUserService implements UserDetailsService {

    @Autowired
    private PatUserMapper patUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PatUser patUser = patUserMapper.createLambdaQuery().andEq(PatUser::getUserName, username).singleSimple();
        if(patUser == null){
            throw new UsernameNotFoundException("not found user:" + username);
        }

        List<GrantedAuthority> grantedAuthorities = this.getGrantedAuthorities(patUser);
        User user = new User(patUser.getUserName(),patUser.getPassword(),grantedAuthorities);
        return user;
    }

    private List<GrantedAuthority> getGrantedAuthorities(PatUser patUser) {
        List<GrantedAuthority> grantedAuthorities= new ArrayList<GrantedAuthority>();
        String role = patUser.getRole();
        if(StringUtils.hasText(role)){
            String[] temRoles = role.split(",");
            for (String temRole : temRoles) {
                grantedAuthorities.add(new SimpleGrantedAuthority(temRole));
            }
        }else{
            role = PatOauthConstant.ROLE_USER;
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        return grantedAuthorities;
    }


}
