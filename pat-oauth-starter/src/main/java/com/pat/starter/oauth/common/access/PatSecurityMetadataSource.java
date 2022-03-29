package com.pat.starter.oauth.common.access;

import com.pat.api.entity.PatResource;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.PatResourceMapper;
import com.pat.starter.oauth.common.constant.PatOauthConstant;
import com.pat.starter.oauth.common.service.PatResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * createby zhouyw on 2020.07.01
 * 自定义的元数据源类，用来提供鉴权过程中，访问资源所需的角色
 */
@Slf4j
public class PatSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    //本方法返回访问资源所需的角色集合
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private PatResourceService patResourceService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
         //从object中得到需要访问的资源，即网址
         String requestUrl = ((FilterInvocation) object).getRequestUrl();
         String preKey = getFirstPath(requestUrl);
//        //从数据库中得到所有资源，以及对应的角色
         List<PatResource> all = patResourceService.getAll(preKey);

        for (PatResource resource : all) {
            //首先进行地址匹配
            if (antPathMatcher.match(resource.getUri(), requestUrl)
                    && resource.getRole().length() > 0) {
                return SecurityConfig.createList(resource.getRole().split(","));
            }
        }
        //匹配不成功返回一个特殊的ROLE_NONE
        return SecurityConfig.createList(PatOauthConstant.ROLE_NEMO);
    }

    /**
     * 获取第一个path 路径值
     * @param requestUrl
     * @return
     */
    private String getFirstPath(String requestUrl) {
        int index = requestUrl.indexOf("/");
        if(index==-1){
            throw new BusinessException("无效请求");
        }
        int index2 = requestUrl.indexOf("/",index);
        if(index2==-1&&index+1<index2){
            throw new BusinessException("无效请求");
        }
        return requestUrl.substring(index+1,index2);
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
