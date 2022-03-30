package com.pat.starter.oauth.server.provider;

import com.pat.starter.oauth.common.constant.PatOauthConstant;
import com.pat.starter.oauth.server.token.PatCodeAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 邮件模式认证提供者，邮件验证码模式认证工作通过该类完成
 * @author: ywzhou
 * @create: 2020-06-30 16:36
 **/
@Slf4j
public class PatEmailCodeAuthProvider implements AuthenticationProvider, MessageSourceAware {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserDetailsService userDetailsService;

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    /**
     * 是否隐藏用户未发现异常，默认为true,为true返回的异常信息为BadCredentialsException
     */
    private boolean hideUserNotFoundExceptions = true;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = (String) authentication.getPrincipal();
        if (email == null) {
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Missing email"));
        }
        String code = (String) authentication.getCredentials();
        if (code == null) {
            log.error("缺失code参数");
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Missing code"));
        }
        String cacheCode = stringRedisTemplate.opsForValue().get(PatOauthConstant.CACHE_EMAIL_CODE + email);
        if (cacheCode == null || !cacheCode.equals(code)) {
            log.error("邮件验证码错误");
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Invalid code"));
        }
        //清除redis中的验证码
        //stringRedisTemplate.delete(RedisConstant.EMAIL_CODE_PREFIX + email);
        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException var6) {
            log.info("邮件:" + email + "未查到用户信息");
            if (this.hideUserNotFoundExceptions) {
                throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
            throw var6;
        }
        check(user);
        PatCodeAuthenticationToken authenticationToken = new PatCodeAuthenticationToken(user, code, user.getAuthorities());
        authenticationToken.setDetails(authenticationToken.getDetails());
        return authenticationToken;
    }

    /**
     * 指定该认证提供者验证Token对象
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return PatCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

    /**
     * 账号禁用、锁定、超时校验
     *
     * @param user
     */
    private void check(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            throw new LockedException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
        } else if (!user.isEnabled()) {
            throw new DisabledException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
        } else if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
        }
    }

    public void setHideUserNotFoundExceptions(boolean hideUserNotFoundExceptions) {
        this.hideUserNotFoundExceptions = hideUserNotFoundExceptions;
    }

}
