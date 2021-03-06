package com.pat.api.service.mo;

import com.pat.api.bo.EsPropBO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * PoetSearchPageMO
 *
 * @author chouway
 * @date 2022.03.03
 */
@Data
public class PoetSearchPageMO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 偏移位
     */
    private int from;

    /**
     * 数量
     */
    private int size;

    /**
     * 是否有关键字
     */
    private Boolean hasKey;

    /**
     * 是否有属性筛选
     */
    private Boolean hasProps;

    /**
     * 关键字
     */
    private String key;
    /**
     * 关键字Word
     */
    private String keyWord;

    /**
     * 特殊过滤 属性keys or vals 至少一个满足
     */
    private List<String> propSpecs;
    /**
     * 是否有特殊过滤
     */
    private Boolean hasPropSpecs;

    /**
     * 关键字包含
     */
    private List<Map<String, Map<String,String>>> propLikesList;
    /**
     * 是否有关键字包含
     */
    private Boolean hasPropLikes;

    /**
     * 是否有必选项
     */
    private Boolean hasMust;

    /**
     * 筛选的属性Keys
     */
    private List<EsPropBO> props;


    /**
     * 聚合搜索
     */
    private List<PoetAggsInfoMO> aggsInfos;


    /**
     * _source 限定返回部分字段
     */
    private Boolean noSources;

    /**
     * 是否需要高亮
     */
    private Boolean needHighLight;

}
