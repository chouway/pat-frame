package com.pat.starter.dao;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.starter.BeetlSqlStater;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.DefaultPropertySourceFactory;

import javax.sql.DataSource;

/**
 * PatApiStarter
 *
 * https://blog.csdn.net/qq_21150865/article/details/83504780
 * 使用springboot自定义starter
 *
 * https://blog.csdn.net/bluerebel/article/details/110196219
 * SpringBoot 配置篇 - 加载指定YML文件
 *
 * https://www.toyaml.com/index.html
 * yml properties 互转
 *
 * @author zhouyw
 * @date 2021.03.20
 */
@Slf4j
@Configuration
@PropertySource(value = "classpath:pat-api.properties", factory = DefaultPropertySourceFactory.class)
@AutoConfigureBefore(BeetlSqlStater.class)
public class PatDaoStarter {

    @Primary
    @Bean(name = "ds")
    public DataSource datasource(Environment env) {
        HikariDataSource ds = new HikariDataSource();
        String dbUrl = env.getProperty("spring.datasource.url");
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(env.getProperty("spring.datasource.username"));
        ds.setPassword(env.getProperty("spring.datasource.password"));
        ds.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        log.info("datasource-->dbUrl={}", dbUrl);
        return ds;
    }



}
