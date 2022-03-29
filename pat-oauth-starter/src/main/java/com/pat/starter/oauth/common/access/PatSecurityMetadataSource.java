package com.pat.starter.oauth.common.access;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * createby zhouyw on 2020.07.01
 * 自定义的元数据源类，用来提供鉴权过程中，访问资源所需的角色
 */
@Slf4j
public class PatSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    //本方法返回访问资源所需的角色集合
    AntPathMatcher antPathMatcher = new AntPathMatcher();

//    @Autowired
//    private PoetResourceMapper  ;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//        //从object中得到需要访问的资源，即网址
//        String requestUrl = ((FilterInvocation) object).getRequestUrl();
//        //从数据库中得到所有资源，以及对应的角色
//        List<MyResourceBean> resourceBeans = resourceMapper.selectAllResource();
//        for (MyResourceBean resource : resourceBeans) {
//            //首先进行地址匹配
//            if (antPathMatcher.match(resource.getUrl(), requestUrl)
//                    && resource.getRolesArray().length > 0) {
//                return SecurityConfig.createList(resource.getRolesArray());
//            }
//        }
        //匹配不成功返回一个特殊的ROLE_NONE
        return SecurityConfig.createList("ROLE_NONE");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
