package com.pat.devops.code.generate;

import com.pat.devops.code.generate.custom.CustomBeetsqlGeneral;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * SchemaTest
 *
 * @author zhouyw
 * @date 2021.03.20
 */
@SpringBootTest(classes = PatCodeGenerateApplication.class)
public abstract  class CodeGenerateTest {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomBeetsqlGeneral customBeetsqlGeneral;


    //eg: 生成的表
    private String TABLE_NAME = "pat_test_info";

    public abstract String getTableName();

    @Test
    public void run(){
        console();
        general();
    }

    public void console(){
        customBeetsqlGeneral.console(getTableName());
        log.info("console:{} -> {} -> {}",customBeetsqlGeneral.getBASE_PROJECT(),customBeetsqlGeneral.getBASE_PROJECT(),TABLE_NAME);
    }


    public void general(){
        customBeetsqlGeneral.general(getTableName());
        log.info("general:{} -> {} -> {}",customBeetsqlGeneral.getBASE_PROJECT(),customBeetsqlGeneral.getBASE_PROJECT(),TABLE_NAME);
    }

}
