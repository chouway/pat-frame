package com.pat.devops.code.generate.custom;

import org.beetl.core.ReThrowConsoleErrorHandler;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.gen.BaseProject;
import org.beetl.sql.gen.SourceBuilder;
import org.beetl.sql.gen.SourceConfig;
import org.beetl.sql.gen.simple.EntitySourceBuilder;
import org.beetl.sql.gen.simple.MDSourceBuilder;
import org.beetl.sql.gen.simple.SimpleMavenProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomBeetsqlGeneral
 *
 * @author chouway
 * @date 2021.03.19
 */
@Component
public class CustomBeetsqlGeneral{

    private  String BASE_PACKAGE = "com.aibk.api";

    private  String BASE_PROJECT = "aibk-api";

    @Autowired
    private SQLManager sqlManager;

    public SourceConfig sourceConfig;

    private BaseProject consoleOnlyProject = null;

    private BaseProject simpleMavenProject = null;

//  https://blog.csdn.net/nrsc272420199/article/details/95033223
//  【bean的生命周期】详解InitializingBean、initMethod和@PostConstruct
    @PostConstruct
    public void init(){
        List<SourceBuilder> sourceBuilder = new ArrayList<SourceBuilder>();
        SourceBuilder entityBuilder = new CustomEntitySB();
        SourceBuilder mapperBuilder = new CustomMapperSB();
        SourceBuilder mdBuilder = new MDSourceBuilder();

        sourceBuilder.add(entityBuilder);
        sourceBuilder.add(mapperBuilder);
        sourceBuilder.add(mdBuilder);

        this.sourceConfig = new SourceConfig(sqlManager,sourceBuilder);
        //如果有错误，抛出异常而不是继续运行1
        EntitySourceBuilder.getGroupTemplate().setErrorHandler(new ReThrowConsoleErrorHandler() );

        this.consoleOnlyProject = new CustomConsoleOnlyProject(this.BASE_PACKAGE);
        this.simpleMavenProject = new SimpleMavenProject(BASE_PACKAGE);

        String root = new File(this.simpleMavenProject.getRoot()).getParentFile().getParentFile().getAbsolutePath()+
                        File.separator+ BASE_PROJECT;
        this.simpleMavenProject.setRoot(root);
        System.out.println("root="+simpleMavenProject.toString());
    }

    

    /**
     * 仅控制台输出
     */
    public void console(String tableName){
        this.sourceConfig.gen(tableName,this.consoleOnlyProject);
    }

    /**
     * 生成相应的代码文件
     */

    public void general(String tableName){
        this.sourceConfig.gen(tableName,this.simpleMavenProject);
    }

    public String getBASE_PACKAGE() {
        return BASE_PACKAGE;
    }

    public void setBASE_PACKAGE(String BASE_PACKAGE) {
        this.BASE_PACKAGE = BASE_PACKAGE;
    }

    public String getBASE_PROJECT() {
        return BASE_PROJECT;
    }

    public void setBASE_PROJECT(String BASE_PROJECT) {
        this.BASE_PROJECT = BASE_PROJECT;
    }
}
