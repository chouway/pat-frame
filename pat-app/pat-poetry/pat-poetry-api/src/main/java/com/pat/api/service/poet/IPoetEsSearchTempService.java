package com.pat.api.service.poet;

import java.util.Map;

/**
 * IPoetEsTemplateService
 *
 * @author chouway
 * @date 2022.03.03
 */
public interface IPoetEsSearchTempService {

    /**
     * 全量推送查询模版到ES
     * @return
     */
    public int pushSearchTemp2Es();

    /**
     * 获取es 搜索模版清单
     * @return
     */
    public String getEsSearchTemps();

    /**
     * 获取ES模版渲染结果
     * @param indexName
     * @param paramsJson
     * @return
     */
    public String renderSearchTemp(String tempId,Object params);

    /**
     * 本机直接mustache渲染结果
     * @param tempId
     * @param params
     * @return
     */
    public String renderSearchTempLocal(String tempId,Object params);

    /**
     * 获取搜索结果
     * @param indexName
     * @param paramsJson
     * @param tempId
     * @return
     */
    public String searchByTemp(String indexName,Object params,String tempId);

    /**
     * 局部更新文档
     * @return
     */
    public String updateIndex(String indexName, Map<String,Object> params, Long docId);
    /**
     * 根据文id获取信息
     * @param id
     * @return
     */
    String get(String indexName,Long id);
}
