package com.pat.starter.oauth.client.config;

import com.pat.starter.oauth.common.access.PatAccessDecisionManager;
import com.pat.starter.oauth.common.access.PatAccessDeniedHandler;
import com.pat.starter.oauth.common.access.PatSecurityMetadataSource;
import com.pat.starter.oauth.common.service.PatResourceService;
import com.pat.starter.oauth.common.service.PatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * PatResourceServerConfig
 *
 * @author chouway
 * @date 2022.04.18
 */
@Configuration
//启用资源服务
@EnableResourceServer
//启用方法级权限控制
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(prefix = "app.oauth.client", name = "enabled", havingValue = "true")
public class PatResourceClientConfig extends ResourceServerConfigurerAdapter{

    @Value("${app.oauth.client.resource_id}")
    private String RESOURCE_ID;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redis = new RedisTokenStore(connectionFactory);
        return redis;
    }


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

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 配置不需要安全拦截url
                .antMatchers("/poetry/**","/client/**").permitAll().and()
                //配置需要保护的资源接口
                .requestMatchers().antMatchers("/poet-api/**")
                .and().authorizeRequests()
//              .antMatchers("/test/**").access("#oauth2.hasScope('sever')")//可以整体加上权限限制
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {       // 重写做权限判断
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(securityMetadataSource());    // 元数据抽取
                        o.setAccessDecisionManager(accessDecisionManager());      // 权限判断
                        return o;
                    }
                })
                .anyRequest().authenticated()
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
       /*
        //外部系统无缓存时 采用的方式
        RemoteTokenServices tokenService = new RemoteTokenServices();//需要启多个认证服务时，可通过nginx负载均衡来
        tokenService.setCheckTokenEndpointUrl("http://127.0.0.1:8840/oauth/check_token");
        tokenService.setClientId("patweb");
        tokenService.setClientSecret("yaohw");
        resources.tokenServices(tokenService);
        */
    }
}
