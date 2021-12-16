package com.pat.devops.code.generate.custom;

import org.beetl.core.Template;
import org.beetl.sql.gen.BaseProject;
import org.beetl.sql.gen.Entity;
import org.beetl.sql.gen.SourceConfig;
import org.beetl.sql.gen.simple.EntitySourceBuilder;

import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CustomEntitySB
 *
 * @author chouway
 * @date 2021.03.19
 */
public class CustomEntitySB extends EntitySourceBuilder {

    public static String pojoPath = "custom-pojo.btl";

    private boolean alias = false;

    public CustomEntitySB() {
        super();
    }

    public CustomEntitySB(boolean alias) {
        super(alias);
        this.alias = alias;
    }

    @Override
    public void generate(BaseProject project, SourceConfig config, Entity entity) {
        Template template = this.alias ? groupTemplate.getTemplate(pojoAliasPath) : groupTemplate.getTemplate(pojoPath);
        template.binding("attrs", entity.getList());
        template.binding("className", entity.getName());
        template.binding("table", entity.getTableName());
        if (!config.isIgnoreDbCatalog()) {
            template.binding("catalog", entity.getCatalog());
        }

        template.binding("package", project.getBasePackage(this.name));
        List<String> imports = entity.getImportPackage();
        imports.add("lombok.Data");
        imports.add("org.beetl.sql.annotation.entity.*");
        imports = imports.stream().distinct().collect(Collectors.toList());//:使用java8新特性stream进行List去重
        template.binding("imports", imports);
        template.binding("comment", entity.getComment());
        Writer writer = project.getWriterByName(this.name, entity.getName() + ".java");
        template.renderTo(writer);
    }
}
