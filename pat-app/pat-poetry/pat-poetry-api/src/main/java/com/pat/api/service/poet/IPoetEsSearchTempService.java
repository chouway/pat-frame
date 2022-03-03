package com.pat.api.service.poet;

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
     * @param tempId
     * @param paramsJson
     * @param indexName
     * @return
     */
    public String searchByTemp(String tempId,String paramsJson,String indexName);
}
