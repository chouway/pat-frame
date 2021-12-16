package com.pat.devops.code.generate.custom;

import org.beetl.core.Template;
import org.beetl.sql.gen.BaseProject;
import org.beetl.sql.gen.Entity;
import org.beetl.sql.gen.SourceConfig;
import org.beetl.sql.gen.simple.MapperSourceBuilder;

import java.io.Writer;
import java.util.Arrays;

/**
 * CustomMapperSB
 *
 * @author chouway
 * @date 2021.03.19
 */
public class CustomMapperSB extends MapperSourceBuilder {

    public static String mapperPath = "custom-mapper.btl";

    public static String SUB_FIX = "Mapper";

    public CustomMapperSB() {
        super();
    }

    @Override
    public void generate(BaseProject project, SourceConfig config, Entity entity) {
        Template template = groupTemplate.getTemplate(mapperPath);
        String mapperClass = entity.getName() + SUB_FIX;
        template.binding("className", mapperClass);
        template.binding("package", project.getBasePackage(this.name));
        template.binding("entityClass", entity.getName());
        String entityPkg = project.getBasePackage("entity");
        String mapperHead = entityPkg + ".*";
        template.binding("imports", Arrays.asList(mapperHead));
        Writer writer = project.getWriterByName(this.name, entity.getName() +  SUB_FIX + ".java");
        template.renderTo(writer);
    }
}
