package com.pat.app.poetry.synch.eo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;

/**
 * SuggestEO
 *
 * 建议词
 *
 * 用户没输入动态展示历史记录 这个由前端各自完成；
 * 用户输入文字后 动态获取推荐词  用户点击时 前端往后台发送推荐词id
 *
 * 目前keyword 主要由
 * 作者
 * 作者 作品标题
 * 作品标题
 * 诗句
 * 等  详见 PoetSuggest
 *
 * 首次推送需要判定es是否有该keyword  没有则添加，否则不添加
 * 前台请求累计需要上送推荐词id
 *
 *
 * @author chouway
 * @date 2022.03.09
 */
@Data
@Setting(shards=2,settingPath= "eo-model/suggest.setting")
@Mapping(mappingPath = "eo-model/suggest.mapping")
@Document(indexName = "suggest_v0")
public class SuggestEO {

    private String id;

    private String keyword;

    private String keyType;

    private Long count;

    private Date udpateTs;
}
