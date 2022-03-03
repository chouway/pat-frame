package com.pat.api.service.poet;

import com.pat.api.bo.EsSearchBO;

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
}
