package com.pat.starter.oauth.server.config;

import com.pat.starter.oauth.common.access.PatAccessDecisionManager;
import com.pat.starter.oauth.common.access.PatAccessDeniedHandler;
import com.pat.starter.oauth.common.access.PatSecurityMetadataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * 资源服务配置
 * @author: ywzhou
 * @create: 2019-10-08 10:04
 **/
@Configuration
//启用资源服务
@EnableResourceServer
//启用方法级权限控制
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(prefix = "app.oauth.server", name = "enabled", havingValue = "true")
@Log4j2
public class PatResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "auth-server";


    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new PatAccessDeniedHandler();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource(){
        return new PatSecurityMetadataSource();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager(){
        return  new PatAccessDecisionManager();
    }

    /**
     *  配置资源接口安全，http.authorizeRequests()针对的所有url，但是由于登录页面url包含在其中，这里配置会进行token校验，校验不通过返回错误json，
     *  而授权码模式获取code时需要重定向登录页面，重定向过程并不能携带token，所有不能用http.authorizeRequests()，
     *  而是用requestMatchers().antMatchers("")，这里配置的是需要资源接口拦截的url数组
     * @param http
     * @return void
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http    //配置需要保护的资源接口
                .requestMatchers().antMatchers("/api/*").and()
                .authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {       // 重写做权限判断
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(securityMetadataSource());    // 元数据抽取
                        o.setAccessDecisionManager(accessDecisionManager());      // 权限判断
                        return o;
                    }
                }).anyRequest().authenticated()
                .and()
                //自定义异常处理
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                ;
    }

    /**
     * 这个是跟服务绑定的，注意要跟client配置一致，如果客户端没有，则不能访问
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID).stateless(true);
    }


}
