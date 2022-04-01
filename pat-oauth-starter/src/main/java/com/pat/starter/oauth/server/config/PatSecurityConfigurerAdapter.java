package com.pat.starter.oauth.server.config;import com.pat.starter.oauth.server.provider.PatEmailCodeAuthProvider;import com.pat.starter.oauth.server.provider.PatMobileCodeAuthProvider;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import org.springframework.data.redis.core.StringRedisTemplate;import org.springframework.security.authentication.AuthenticationManager;import org.springframework.security.authentication.dao.DaoAuthenticationProvider;import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;import org.springframework.security.config.annotation.web.builders.HttpSecurity;import org.springframework.security.config.annotation.web.builders.WebSecurity;import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;import org.springframework.security.core.userdetails.UserDetailsService;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;import org.springframework.security.crypto.password.PasswordEncoder;/** * security web安全配置,spring-cloud-starter-oauth2依赖于security *  默认情况下SecurityConfigurerAdapter执行比ResourceServerConfig先 * @author: ywzhou * @create: 2019-09-25 16:49 */@Configuration@EnableWebSecurity@ConditionalOnProperty(prefix = "app.oauth.server", name = "enabled", havingValue = "true")public class PatSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {    @Autowired    private UserDetailsService userDetailsService;    @Autowired    private StringRedisTemplate stringRedisTemplate;    /**     * 配置认证管理器     *     * @return     * @throws Exception     */    @Bean    @Override    public AuthenticationManager authenticationManagerBean() throws Exception {        return super.authenticationManagerBean();    }    /**     * 配置密码加密对象（解密时会用到PasswordEncoder的matches判断是否正确）     * 用户的password和客户端clientSecret用到，所以存的时候存该bean encode过的密码     *     * @return     */    @Bean    public PasswordEncoder passwordEncoder() {        return new BCryptPasswordEncoder();    }    /**     * 这里是对认证管理器的添加配置     *     * @param auth     * @throws Exception     */    @Override    protected void configure(AuthenticationManagerBuilder auth) throws Exception {        DaoAuthenticationProvider daoAuthenticationProvider = daoProvider(userDetailsService);        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());        auth.authenticationProvider(emailProvider())                .authenticationProvider(mobileProvider())                .authenticationProvider(daoAuthenticationProvider)                .userDetailsService(userDetailsService)                .passwordEncoder(passwordEncoder());    }    @Override    public void configure(WebSecurity web) throws Exception {        web.ignoring().antMatchers("/css/**","/static/**");    }    /**     *  安全请求配置,这里配置的是security的部分，这里配置全部通过，安全拦截在资源服务的配置文件中配置，     *  要不然访问未验证的接口将重定向到登录页面，前后端分离的情况下这样并不友好，无权访问接口返回相关错误信息即可     * @param http     * @return void     */    @Override    protected void configure(HttpSecurity http) throws Exception {        http                //关闭https严格要求                .headers().httpStrictTransportSecurity().disable()                //需要保护的资源 页面                .and().authorizeRequests().antMatchers("/test/*").authenticated()                .and()                .formLogin().loginPage("/login")                .permitAll()                .defaultSuccessUrl("/test/loginSuccess");//                .and()//关闭打开的csrf保护   打开需要在登录中增加 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>//                .csrf().disable()//                .cors();//允许跨域    }    /**     * 自定义手机验证码认证提供者     *     * @return     */    @Bean    public PatMobileCodeAuthProvider mobileProvider() {        PatMobileCodeAuthProvider provider = new PatMobileCodeAuthProvider();        provider.setHideUserNotFoundExceptions(true);        return provider;    }    /**     * 自定义手机验证码认证提供者     *     * @return     */    @Bean    public PatEmailCodeAuthProvider emailProvider() {        PatEmailCodeAuthProvider provider = new PatEmailCodeAuthProvider();        provider.setHideUserNotFoundExceptions(true);        return provider;    }    @Bean    public DaoAuthenticationProvider daoProvider(UserDetailsService userDetailsService) {        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();        provider.setHideUserNotFoundExceptions(true);        provider.setUserDetailsService(userDetailsService);        return provider;    }}