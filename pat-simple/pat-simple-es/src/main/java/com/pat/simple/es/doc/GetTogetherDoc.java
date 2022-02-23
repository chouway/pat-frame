package com.pat.simple.es.doc;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * https://blog.csdn.net/chengyuqiang/article/details/86135795
 * 完整教程：spring-boot-starter-data-elasticsearch整合elasticsearch 6.x
 * 待整合；  实现 数据库与es 一致;
 *
 * GetTogetherDoc
 *
 * @author chouway
 * @date 2022.02.23
 */
@Document(indexName = "get-together",type="get-together")
public class GetTogetherDoc implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String organizer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }
}
