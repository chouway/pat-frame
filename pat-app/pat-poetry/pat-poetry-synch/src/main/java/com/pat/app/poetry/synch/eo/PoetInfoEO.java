package com.pat.app.poetry.synch.eo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * PoetInfoEO
 *
 * 当前版本为 poet-info_v0
 *
 * 初始化mapping时 为poet-info_v0  使用同名 poet-info
 * 升级修改setting时 在其它项中  复制添加 PoetInfoEO_V0 及 repo  以及 新版本 PoetInfoEO_V1 及 repo  ；  通知CRUD 迁移旧数据。之后修改修改 PoetInfoEO  。
 *
 * 开发环境 可以直接 移除 索引 直接重建。
 *
 * @author chouway
 * @date 2022.03.01
 */
@Data
@Setting(shards=2)
@Document(indexName = "poet-info")
public class PoetInfoEO {

    @Id
    private Long id;

    /**
     * 标题
     */
    @Field(type= FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String title;

    /**
     * 副标题
     */
    @CompletionField
    @Field(type= FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String subtitle;

    /**
     * 内容
     */
    @Field(type= FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String content;

    /**
     * 作者
     */
    @Field(type= FieldType.Keyword)
    private String author;

    /**
     * 属性key
     */
    @Field(type= FieldType.Keyword,store = true)
   private List<String> propKeys;


    /**
     * 属性key - Val
     */
   @Field(type= FieldType.Object,store = true)
   private Map<String,String> properties;


    /**
     * 文集 - 篇章 - 节选 序列值 setChapterSeciont index
     */
    @Field(type = FieldType.Integer)
    private int index;
}
