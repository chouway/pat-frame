package com.pat.devops.code.generate.custom;

import org.beetl.sql.gen.simple.ConsoleOnlyProject;

/**
 * CustomConsoleOnlyProject
 *
 * @author chouway
 * @date 2021.03.19
 */
public class CustomConsoleOnlyProject extends ConsoleOnlyProject {

    private String basePackage;

    public CustomConsoleOnlyProject(String basePackage){
        this.basePackage = basePackage;
    }

    @Override
    public String getBasePackage(String sourceBuilderName) {
        return basePackage + "." + sourceBuilderName;
    }
}
