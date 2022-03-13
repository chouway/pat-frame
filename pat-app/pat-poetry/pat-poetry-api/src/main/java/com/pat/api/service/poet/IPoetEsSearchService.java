package com.pat.api.service.poet;

import com.pat.api.bo.EsSearchBO;
import com.pat.api.bo.EsSuggestBO;

import java.util.List;
import java.util.Map;

/**
 * PatEsSearchService
 *
 * @author chouway
 * @date 2022.03.02
 */
public interface IPoetEsSearchService {

    /**
     * 获取ES搜索结果
     *
     * @param esSearchBO
     * @return
     */
    String search(EsSearchBO esSearchBO);

    /**
     * 获取聚合结果
     * @param esSearchBO
     * @return
     */
    String aggs(EsSearchBO esSearchBO);

    /**
     * 获取推荐词
     * @param esSuggestBO
     * @return
     */
    Map<Long,String> suggest(EsSuggestBO esSuggestBO);
    /**
     * 根据id获取文信息
     * @param id
     * @return
     */
    String get(Long id);
}
