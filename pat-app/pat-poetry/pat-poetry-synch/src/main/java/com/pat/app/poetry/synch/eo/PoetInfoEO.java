package com.pat.app.poetry.synch.eo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * PoetInfoEO
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

    @Field(type= FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String title;

    @Field(type= FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String subtitle;

    @Field(type= FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String content;

    @Field(type= FieldType.Keyword)
    private String author;

    @Field(type= FieldType.Keyword,store = true)
    private List<String> propKeys;

    @Field(type= FieldType.Object,store = true)
    private Map<String,String> properties;
}
