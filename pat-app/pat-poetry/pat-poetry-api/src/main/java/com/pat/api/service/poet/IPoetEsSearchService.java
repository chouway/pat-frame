package com.pat.api.service.poet;

import com.pat.api.bo.*;

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
     * 返回ES搜索结果 并处理BO
     * @param esSearchBO
     * @return
     */
    PoetSearchResultBO searchBO(EsSearchBO esSearchBO);

    /**
     * 获取聚合结果
     * @param esSearchBO
     * @return
     */
    String aggs(EsSearchBO esSearchBO);

    /**
     * 获取聚合结果 并处理BO
     * @param esSearchBO
     * @return
     */
    List<PoetAggsBO> aggsBO(EsSearchBO esSearchBO);


    /**
     * 获取推荐词
     * @param esSuggestBO
     * @return
     */
    List<EsSuggestBO> suggest(EsSuggestBO esSuggestBO);
    /**
     * 根据id获取文信息
     * @param id
     * @return
     */
    String get(Long id);

    /**
     * 获取聚合的propKey 默认10个
     * @param esSearchBO
     * @return
     */
    public Map<String,Object> aggsProKeys(EsSearchBO esSearchBO);

    /**
     * 获取聚合筛选keys   默认100个
     * @param esSearchBO
     * @return
     */
    List<PoetAggsKeyBO> getAggsKeys(EsSearchBO esSearchBO);

    /**
     * 获取筛选key 对应的vals  默认100个
     * @param esSearchBO
     * @return
     */
    List<PoetAggsValBO> getAggsKeyVals(EsSearchBO esSearchBO);
}
