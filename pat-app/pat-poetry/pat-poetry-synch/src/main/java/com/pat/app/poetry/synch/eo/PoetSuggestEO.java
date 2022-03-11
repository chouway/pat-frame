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
@Setting(shards=2,settingPath= "eo-model/poet-suggest.setting")
@Mapping(mappingPath = "eo-model/poet-suggest.mapping")
@Document(indexName = "poet-suggest_v0")
public class PoetSuggestEO {

    private Long id;

    /**
     * 推荐词  completion 首拼音 字符符合匹配
     */
    private String keyword;

    /**
     * 推荐词  search_as_you_type
     */
    private String keyword2;

    /**
     * 类型： 00 诗人  01 作品名  02 诗人 作品名 03 诗句  04  其它
     */
    private String keyType;

    /**
     * 关联id
     */
    private Long relId;

    private Long count;

    private Date updateTs;
}
