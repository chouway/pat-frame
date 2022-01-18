package com.pat.starter.common;


import com.pat.starter.common.aop.PatAnnoAspect;
import com.pat.starter.common.aop.PatServiceAop;
import com.pat.starter.common.aop.PatTimingAspect;
import com.pat.starter.common.config.PatCommonData;
import com.pat.starter.common.util.PatErrorUtil;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AibkCommonStarter
 * <p>
 * https://blog.csdn.net/itguangit/article/details/103345666
 * 实用：如何将aop中的pointcut值从配置文件中读取
 * <p>
 * http://www.xmhzd.com/study/article/view-1648.html
 * SpringBoot开发自己的Starter
 * <p>
 * https://www.cnblogs.com/FraserYu/p/11261916.html
 *
 * @author zhouyw
 * @ConfigurationProperties 注解使用姿势，这一篇就够了
 * <p>
 * https://blog.csdn.net/m0_54138989/article/details/112567096?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_title-0&spm=1001.2101.3001.4242
 * springboot加载application配置文件的优先级顺序
 * <p>
 * https://blog.csdn.net/sunwei_pyw/article/details/77972625
 * Perf4j的使用
 *
 * https://www.iteye.com/blog/conkeyn-2354644
 * 在自定义spring aop中使用el获取拦截方法的变量值。
 * @date 2021.03.20
 */
@Configuration
@EnableConfigurationProperties(PatCommonData.class)
@ConditionalOnProperty(prefix = "app.common", name = "enabled", havingValue = "true")
public class PatCommonStarter {

    @Autowired
    private PatCommonData patCommonData;

    @Bean
    public PatErrorUtil patErrorUtil(){
        return new PatErrorUtil();
    }

    @Bean
    public PatTimingAspect patTimingAspect() {
        return new PatTimingAspect();
    }

    @Bean
    public PatServiceAop patServiceAop() {
        return new PatServiceAop();
    }

    @Bean
    public PatAnnoAspect patAnnoAspect() {
        return new PatAnnoAspect();
    }

    @Bean
    public AspectJExpressionPointcutAdvisor configurabledvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(patCommonData.getAopService());
        advisor.setAdvice(patServiceAop());
        return advisor;
    }
}
