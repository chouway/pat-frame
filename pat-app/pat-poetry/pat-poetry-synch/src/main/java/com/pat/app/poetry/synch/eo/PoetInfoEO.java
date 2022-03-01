package com.pat.app.poetry.synch.eo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

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

    @Field(type= FieldType.Text,analyzer = "ik_max_word")
    private String title;

    @Field(type= FieldType.Text,analyzer = "ik_max_word")
    private String subString;

    @Field(type= FieldType.Text,analyzer = "ik_max_word")
    private String content;

    @Field(type= FieldType.Keyword)
    private String author;

    @Field(type= FieldType.Keyword)
    private List<String> propKeys;

    @Field(type= FieldType.Object)
    private Map<String,String> properties;
}
